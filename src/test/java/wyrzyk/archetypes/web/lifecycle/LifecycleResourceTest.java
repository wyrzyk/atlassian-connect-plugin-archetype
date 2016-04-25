package wyrzyk.archetypes.web.lifecycle;

import com.jayway.restassured.module.mockmvc.response.MockMvcResponse;
import com.jayway.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import wyrzyk.archetypes.config.WebConfiguration;

import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.Assertions.assertThat;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebConfiguration.class})
public class LifecycleResourceTest {
    private static final String LIFECYCLE_PATH = "/lifecycle";
    private static final String LIFECYCLE_INSTALLED_PATH = LIFECYCLE_PATH + "/installed";
    private static final String LIFECYCLE_ENABLED_PATH = LIFECYCLE_PATH + "/enabled";
    private static final String LIFECYCLE_DISABLED_PATH = LIFECYCLE_PATH + "/disabled";
    private static final String LIFECYCLE_UNINSTALLED_PATH = LIFECYCLE_PATH + "/uninstalled";

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private LifecycleRepository lifecycleRepository;
    @Autowired
    private LifecycleService lifecycleService;

    private MockMvcRequestSpecification mockMvc;


    @Before
    public void setupMockMvc() {
        mockMvc = given().webAppContextSetup(webApplicationContext);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testIfInstalledMethodReturns200or204() throws Exception {
        createInstalled(prepareDefaultRequestBuilder().build())
                .then()
                .statusCode(Matchers.isOneOf(200, 204));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testIfInitializePayloadSaved() throws Exception {
        assertThat(lifecycleRepository.count()).isZero();
        createInstalled(prepareDefaultRequestBuilder()
                .build());
        assertThat(lifecycleRepository.count())
                .isEqualTo(1);
        assertThat(lifecycleRepository.findAll())
                .extracting("clientKey", String.class)
                .containsOnly("1");
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testTwoPayloadsSaved() throws Exception {
        assertThat(lifecycleRepository.count()).isZero();
        createInstalled(prepareDefaultRequestBuilder()
                .build());
        createInstalled(prepareDefaultRequestBuilder()
                .clientKey("2")
                .build());
        assertThat(lifecycleRepository.count())
                .isEqualTo(2);
        assertThat(lifecycleRepository.findAll())
                .extracting("clientKey", String.class)
                .contains("1", "2");
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testIfPayloadIsUpdated() throws Exception {
        assertThat(lifecycleRepository.count()).isZero();
        createInstalled(prepareDefaultRequestBuilder()
                .sharedSecret("old")
                .build());
        createInstalled(prepareDefaultRequestBuilder()
                .sharedSecret("new")
                .build());
        assertThat(lifecycleRepository.count())
                .isEqualTo(1);
        assertThat(lifecycleRepository.findAll())
                .extracting("sharedSecret", String.class)
                .contains("new");
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testWholeLifecycle() throws Exception {
        assertThat(lifecycleRepository.count()).isZero();
        final String clientKey = "clientKey";
        final LifecycleRequestMock preparedRequest = prepareDefaultRequestBuilder()
                .clientKey(clientKey)
                .build();
        createInstalled(preparedRequest);
        assertThat(lifecycleService.isInstalled(clientKey)).isTrue();

        createLifecycleRequest(LIFECYCLE_ENABLED_PATH, preparedRequest);
        assertThat(lifecycleService.isEnabled(clientKey)).isTrue();

        createLifecycleRequest(LIFECYCLE_DISABLED_PATH, preparedRequest);
        assertThat(lifecycleService.isEnabled(clientKey)).isFalse();

        createLifecycleRequest(LIFECYCLE_UNINSTALLED_PATH, preparedRequest);
        assertThat(lifecycleService.isInstalled(clientKey)).isFalse();
    }

    private MockMvcResponse createInstalled(LifecycleRequestMock request) {
        return createLifecycleRequest(LIFECYCLE_INSTALLED_PATH, request);
    }

    private MockMvcResponse createLifecycleRequest(String resourcePath, LifecycleRequestMock request) {
        return mockMvc.contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(resourcePath);
    }


    private LifecycleRequestMock.LifecycleRequestMockBuilder prepareDefaultRequestBuilder() {
        return LifecycleRequestMock.builder()
                .key("installed-addon-key")
                .clientKey("1")
                .publicKey("MIGfZRWzwIDAQAB")
                .sharedSecret("a-secret-key-not-to-be-lost")
                .serverVersion("server-version")
                .pluginsVersion("version-of-connect")
                .baseUrl("http://example.atlassian.net")
                .productType("jira")
                .description("Atlassian JIRA at https://example.atlassian.net")
                .serviceEntitlementNumber("SEN-number")
                .eventType("installed");
    }
}
