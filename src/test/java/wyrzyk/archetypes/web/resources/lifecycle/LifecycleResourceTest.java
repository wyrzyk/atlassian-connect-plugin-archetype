package wyrzyk.archetypes.web.resources.lifecycle;

import com.jayway.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;
import wyrzyk.archetypes.web.WebConfiguration;

import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebConfiguration.class})
public class LifecycleResourceTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    protected MockMvcRequestSpecification mockMvc;

    @Before
    public void setupMockMvc() {
        mockMvc = given().webAppContextSetup(webApplicationContext);
    }

    @Test
    public void testIfInstalledMethodReturns200or204() throws Exception {
        mockMvc.contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(LifecycleRequestMock.builder().key("key").build())
                .when()
                .post("/lifecycle/installed")
                .then()
                .statusCode(Matchers.isOneOf(200, 204));
    }
}
