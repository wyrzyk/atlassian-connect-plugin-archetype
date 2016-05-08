package wyrzyk.archetypes.auth;

import com.atlassian.jwt.CanonicalHttpRequest;
import com.atlassian.jwt.core.reader.NimbusJwtReaderFactory;
import com.atlassian.jwt.httpclient.CanonicalHttpUriRequest;
import org.joor.Reflect;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


public class JwtServiceTest {

    private static final String SHARED_SECRET = "28b8a704-879a-45fe-94ad-09fb3760a5e5";
    private static final String JWT_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJxc2giOiJlMDgxYmI5NGIxMTIyOWZkNDA2NTIzNGFkMzg0Zjc0Mzk5NGI2M2YwYmJiNDVkYzcyZDkwMjA1NGJmNjM5Y2M1IiwiaXNzIjoiY2xpZW50IiwiZXhwIjoxNDYyMzMzNDQ1LCJpYXQiOjE0NjIzMzMyNjV9.UOOIJPd3LQ8lSV2php5qiEvEkj2OykU-GheFZoFshY8";

    @Mock
    private NimbusJwtReaderFactory nimbusJwtReaderFactory;
    private JwtServiceProxy jwtService;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        jwtService = Reflect.on(new JwtService(nimbusJwtReaderFactory)).as(JwtServiceProxy.class);
    }

    @Test
    public void prepareJwtToken() throws Exception {
        final CanonicalHttpRequest request = new CanonicalHttpUriRequest("GET", "/installed", "/");
        final String jwtToken = jwtService.prepareJwtToken("client", SHARED_SECRET, request, 1462333265, 1462333445);
        assertThat(jwtToken).isEqualTo(
                JWT_TOKEN);
    }

    interface JwtServiceProxy {
        String prepareJwtToken(String clientKey, String sharedSecret, CanonicalHttpRequest canonicalHttpRequest);

        Optional<String> extractIssuerUnverified(String jwtToken);

        String prepareJwtToken(String clientKey, String sharedSecret, CanonicalHttpRequest canonicalHttpRequest, long issuedAt, long expiresAt);
    }
}