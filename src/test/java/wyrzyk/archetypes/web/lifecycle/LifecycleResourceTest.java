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
    public static final String LIFECYCLE_INSTALLED_PATH = "/lifecycle/installed";

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private LifecycleRepository lifecycleRepository;

    protected MockMvcRequestSpecification mockMvc;


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

    private MockMvcResponse createInstalled(LifecycleInstallRequestMock request) {
        return mockMvc.contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(LIFECYCLE_INSTALLED_PATH);
    }

    private LifecycleInstallRequestMock.LifecycleInstallRequestMockBuilder prepareDefaultRequestBuilder() {
        return LifecycleInstallRequestMock.builder()
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
