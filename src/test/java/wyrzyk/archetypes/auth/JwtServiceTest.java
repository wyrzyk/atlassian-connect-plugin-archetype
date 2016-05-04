package wyrzyk.archetypes.auth;

import com.atlassian.jwt.CanonicalHttpRequest;
import com.atlassian.jwt.httpclient.CanonicalHttpUriRequest;
import org.assertj.core.api.Assertions;
import org.junit.Test;


public class JwtServiceTest {
    private static final String SHARED_SECRET = "28b8a704-879a-45fe-94ad-09fb3760a5e5";
    private final JwtService jwtService = new JwtService();

    @Test
    public void prepareJwtToken() throws Exception {
        final CanonicalHttpRequest request = new CanonicalHttpUriRequest("GET", "/installed", "/");
        final String jwtToken = jwtService.prepareJwtToken("client", SHARED_SECRET, request, 1462333265, 1462333445);
        Assertions.assertThat(jwtToken).isEqualTo("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJxc2giOiJlMDgxYmI5NGIxMTIyOWZkNDA2NTIzNGFkMzg0Zjc0Mzk5NGI2M2YwYmJiNDVkYzcyZDkwMjA1NGJmNjM5Y2M1IiwiaXNzIjoiY2xpZW50IiwiZXhwIjoxNDYyMzMzNDQ1LCJpYXQiOjE0NjIzMzMyNjV9.UOOIJPd3LQ8lSV2php5qiEvEkj2OykU-GheFZoFshY8");
    }
}