package wyrzyk.archetypes.auth;

import com.atlassian.jwt.core.reader.JwtIssuerSharedSecretService;
import com.atlassian.jwt.exception.JwtUnknownIssuerException;
import org.junit.Test;
import wyrzyk.archetypes.web.lifecycle.LifecycleService;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class JwtIssuerSharedSecretServiceImplTest {
    public static final String CLIENT_KEY = "clientKey";

    @Test(expected = JwtUnknownIssuerException.class)
    public void testGetSharedSecretShoutThrowJwtUnknownIssuerExceptionIfClientNotFound() throws Exception {
        final LifecycleService lifecycleService = mock(LifecycleService.class);
        when(lifecycleService.findClient(CLIENT_KEY)).thenReturn(Optional.empty());

        final JwtIssuerSharedSecretService jwtIssuerSharedSecretService = new JwtIssuerSharedSecretServiceImpl(lifecycleService);
        jwtIssuerSharedSecretService.getSharedSecret(CLIENT_KEY);
    }

}