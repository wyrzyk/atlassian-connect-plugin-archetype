package it.wyrzyk.archetypes.web.lifecycle;

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
import wyrzyk.archetypes.web.lifecycle.LifecycleService;

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
        assertThat(lifecycleService.countClients()).isZero();
        createInstalled(prepareDefaultRequestBuilder()
                .clientKey("clientKey")
                .build());
        assertThat(lifecycleService.countClients())
                .isEqualTo(1);
        assertThat(lifecycleService.findClient("clientKey")).isNotEmpty();
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testTwoPayloadsSaved() throws Exception {
        assertThat(lifecycleService.countClients()).isZero();
        createInstalled(prepareDefaultRequestBuilder()
                .clientKey("1")
                .build());
        createInstalled(prepareDefaultRequestBuilder()
                .clientKey("2")
                .build());
        assertThat(lifecycleService.countClients())
                .isEqualTo(2);
        assertThat(lifecycleService.findClient("1")).isNotEmpty();
        assertThat(lifecycleService.findClient("2")).isNotEmpty();
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testIfPayloadIsUpdated() throws Exception { //todo: determine if it's a bug or expected behaviour
        assertThat(lifecycleService.countClients()).isZero();
        createInstalled(prepareDefaultRequestBuilder()
                .clientKey("1")
                .sharedSecret("old")
                .build());
        createInstalled(prepareDefaultRequestBuilder()
                .sharedSecret("new")
                .clientKey("1")
                .build());
        assertThat(lifecycleService.countClients())
                .isEqualTo(1);
        assertThat(lifecycleService.findClient("1")).isNotEmpty();
        assertThat(lifecycleService.findClient("1").get().getSharedSecret()).isEqualTo("new");
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testWholeLifecycle() throws Exception {
        assertThat(lifecycleService.countClients()).isZero();
        final String clientKey = "clientKey";
        final LifecycleRequestMock preparedRequest = prepareDefaultRequestBuilder()
                .clientKey(clientKey)
                .build();
        createInstalled(preparedRequest);
        assertThat(lifecycleService.isInstalled(clientKey)).isTrue();
        assertThat(lifecycleService.isEnabled(clientKey)).isFalse();

        createLifecycleRequest(LIFECYCLE_ENABLED_PATH, preparedRequest, "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJxc2giOiJiNGYzMjJmNGRkMjQ2OTFiYmViNjZhZjkwZmNiZWE0MjAxMDkyMDA5YmM2MzA0MDc0YTg5NGRjMTQyOWFhOTA2IiwiaXNzIjoiamlyYTo1ZDQzMWQ5Yy0zZWU4LTQyNmEtYjM2NS1mNjc2ODljNTExYTMiLCJjb250ZXh0Ijp7fSwiZXhwIjoxNDYxNzc3MTMzLCJpYXQiOjE0NjE3NzY5NTN9.NTun8Cy_ipFu7BHzuvAVa4liuq4Xk4JwLw6z0kdGAJM");
        assertThat(lifecycleService.isEnabled(clientKey)).isTrue();

        createLifecycleRequest(LIFECYCLE_DISABLED_PATH, preparedRequest, "");
        assertThat(lifecycleService.isEnabled(clientKey)).isFalse();

        createLifecycleRequest(LIFECYCLE_UNINSTALLED_PATH, preparedRequest, "");
        assertThat(lifecycleService.isInstalled(clientKey)).isFalse();
    }

    private MockMvcResponse createInstalled(LifecycleRequestMock request) {
        return createLifecycleRequest(LIFECYCLE_INSTALLED_PATH, request, "JWT eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInFzaCI6ImEzYzgyZDc2MTY2YWQ2ODM3YmE3NzYzNmFlNDZmZTkyNTQxYmVlNTg1YTNjNzAyNzhjNjgyYWM2MjQxZWNmYTIiLCJpc3MiOiJqaXJhOjVkNDMxZDljLTNlZTgtNDI2YS1iMzY1LWY2NzY4OWM1MTFhMyIsImNvbnRleHQiOnsidXNlciI6eyJ1c2VyS2V5IjoiYWRtaW4iLCJ1c2VybmFtZSI6ImFkbWluIiwiZGlzcGxheU5hbWUiOiJhZG1pbiJ9fSwiZXhwIjoxNDYxNzc3MTMyLCJpYXQiOjE0NjE3NzY5NTJ9.-aY71bfoQKokY3Ha_DB2Uq56-tAfxCLDZf-r856XcB8");
    }

    private MockMvcResponse createLifecycleRequest(String resourcePath, LifecycleRequestMock request, String jwtPayload) {
        return mockMvc.contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("user_key", "admin")
                .header("Authorization", "JWT" + jwtPayload)
                .body(request)
                .when()
                .post(resourcePath);
    }


    private LifecycleRequestMock.LifecycleRequestMockBuilder prepareDefaultRequestBuilder() {
        return LifecycleRequestMock.builder()
                .key("wyrzyk.archetypes.atlassian-connect-plugin")
                .clientKey("jira:5d431d9c-3ee8-426a-b365-f67689c511a3")
                .publicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCesbDSDJn70fRG/wH4KyWjIWWxIjqvIbLiZcgv0OdTwlGRCa/5+CIBmjpj4WELCNVp0/1vHXX3RdeQe6HZ/0kmP3ptViqcyj9YdvC+WBwpYx5Z6Nula5asdwc3jG3tD9spAmv0EfoXrSbQrn2a145cJvgo5VrE3Z4j5UWD4sqQXwIDAQAB")
                .sharedSecret("28b8a704-879a-45fe-94ad-09fb3760a5e5")
                .serverVersion("72002")
                .pluginsVersion("1.1.84")
                .baseUrl("http://localhost:2990/jira")
                .productType("jira")
                .description("Atlassian JIRA at http://localhost:2990/jira")
                .serviceEntitlementNumber("SEN-number")
                .eventType("installed");
    }
}
