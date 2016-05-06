package it.wyrzyk.archetypes.web.lifecycle;

import com.atlassian.jwt.CanonicalHttpRequest;
import com.atlassian.jwt.httpclient.CanonicalHttpUriRequest;
import com.jayway.restassured.module.mockmvc.response.MockMvcResponse;
import com.jayway.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.hamcrest.Matchers;
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
import wyrzyk.archetypes.auth.JwtService;
import wyrzyk.archetypes.config.WebConfiguration;
import wyrzyk.archetypes.web.lifecycle.LifecycleService;

import java.util.Optional;

import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static java.util.Optional.empty;
import static javax.servlet.http.HttpServletResponse.SC_NO_CONTENT;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
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
    private static final String CLIENT_KEY = "jira:5d431d9c-3ee8-426a-b365-f67689c511a3";
    private static final String SHARED_SECRET = "28b8a704-879a-45fe-94ad-09fb3760a5e5";
    private static final String BASE_URL = "http://localhost:2990/jira";

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private LifecycleService lifecycleService;
    @Autowired
    private JwtService jwtService;

    public MockMvcRequestSpecification mockMvc() {
        return given().webAppContextSetup(webApplicationContext);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testIfInstalledMethodReturns200or204() throws Exception {
        createInstalled(prepareDefaultRequestBuilder().build())
                .then()
                .statusCode(Matchers.isOneOf(SC_OK, SC_NO_CONTENT));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void instalationShouldFailWithoutAuthenticationHeader() throws Exception {
        createLifecycleRequest(LIFECYCLE_INSTALLED_PATH, prepareDefaultRequestBuilder().build(),
                empty())
                .then()
                .statusCode(SC_UNAUTHORIZED);
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
    public void testIfPluginIsReinstalledAndSecondRequestHasProperJwtToken() throws Exception {
        assertThat(lifecycleService.countClients()).isZero();
        createInstalled(prepareDefaultRequestBuilder()
                .sharedSecret(SHARED_SECRET)
                .build());
        createInstalled(prepareDefaultRequestBuilder()
                .sharedSecret("new")
                .build());
        assertThat(lifecycleService.countClients())
                .isEqualTo(1);
        assertThat(lifecycleService.findClient(CLIENT_KEY)).isNotEmpty();
        assertThat(lifecycleService.findClient(CLIENT_KEY).get().getSharedSecret()).isEqualTo("new");
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testIfPluginIsReinstalledAndSecondRequestHasFakeJwtToken() throws Exception {
        assertThat(lifecycleService.countClients()).isZero();
        createInstalled(prepareDefaultRequestBuilder()
                .sharedSecret(SHARED_SECRET)
                .build());
        final String newSharedSecret = "A_NEW_SHARED_SECRET";
        createLifecycleRequest(LIFECYCLE_ENABLED_PATH, prepareDefaultRequestBuilder()
                .sharedSecret(newSharedSecret)
                .build(), Optional.of(newSharedSecret))
                .then()
                .statusCode(SC_UNAUTHORIZED);
        assertThat(lifecycleService.countClients())
                .isEqualTo(1);
        assertThat(lifecycleService.findClient(CLIENT_KEY).get().getSharedSecret()).isEqualTo(SHARED_SECRET);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testWholeLifecycle() throws Exception {
        assertThat(lifecycleService.countClients()).isZero();
        final LifecycleRequestMock preparedRequest = prepareDefaultRequestBuilder()
                .build();
        createInstalled(preparedRequest);
        assertThat(lifecycleService.isInstalled(CLIENT_KEY)).isTrue();
        assertThat(lifecycleService.isEnabled(CLIENT_KEY)).isFalse();

        createLifecycleRequest(LIFECYCLE_ENABLED_PATH, preparedRequest);
        assertThat(lifecycleService.isEnabled(CLIENT_KEY)).isTrue();

        createLifecycleRequest(LIFECYCLE_DISABLED_PATH, preparedRequest);
        assertThat(lifecycleService.isEnabled(CLIENT_KEY)).isFalse();

        createLifecycleRequest(LIFECYCLE_UNINSTALLED_PATH, preparedRequest);
        assertThat(lifecycleService.isInstalled(CLIENT_KEY)).isFalse();
    }

    private MockMvcResponse createInstalled(LifecycleRequestMock request) {
        return createLifecycleRequest(LIFECYCLE_INSTALLED_PATH, request);
    }

    private MockMvcResponse createLifecycleRequest(String resourcePath, LifecycleRequestMock request) {
        return createLifecycleRequest(resourcePath, request, Optional.of(SHARED_SECRET));
    }

    private MockMvcResponse createLifecycleRequest(String resourcePath, LifecycleRequestMock request, Optional<String> sharedSecret) {
        final CanonicalHttpRequest canonicalHttpRequest = new CanonicalHttpUriRequest("POST", resourcePath, BASE_URL);
        return (sharedSecret.isPresent() ?
                mockMvc().header("Authorization", String.format("JWT %s", jwtService.prepareJwtToken(CLIENT_KEY, sharedSecret.get(), canonicalHttpRequest)))
                : mockMvc())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(resourcePath);
    }


    private LifecycleRequestMock.LifecycleRequestMockBuilder prepareDefaultRequestBuilder() {
        return LifecycleRequestMock.builder()
                .key("wyrzyk.archetypes.atlassian-connect-plugin")
                .clientKey(CLIENT_KEY)
                .publicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCesbDSDJn70fRG/wH4KyWjIWWxIjqvIbLiZcgv0OdTwlGRCa/5+CIBmjpj4WELCNVp0/1vHXX3RdeQe6HZ/0kmP3ptViqcyj9YdvC+WBwpYx5Z6Nula5asdwc3jG3tD9spAmv0EfoXrSbQrn2a145cJvgo5VrE3Z4j5UWD4sqQXwIDAQAB")
                .sharedSecret(SHARED_SECRET)
                .serverVersion("72002")
                .pluginsVersion("1.1.84")
                .baseUrl(BASE_URL)
                .productType("jira")
                .description("Atlassian JIRA at http://localhost:2990/jira")
                .serviceEntitlementNumber("SEN-number")
                .eventType("installed");
    }
}
