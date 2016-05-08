package wyrzyk.archetypes.auth;

import com.atlassian.jwt.core.reader.JwtIssuerSharedSecretService;
import com.atlassian.jwt.exception.JwtIssuerLacksSharedSecretException;
import com.atlassian.jwt.exception.JwtUnknownIssuerException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import wyrzyk.archetypes.api.ClientInfoDto;
import wyrzyk.archetypes.lifecycle.LifecycleService;

import java.util.Optional;

import static org.mockito.Mockito.when;


public class JwtIssuerSharedSecretServiceImplTest {
    private static final String CLIENT_KEY = "clientKey";
    private  static final String SECRET = "secret";

    @Mock
    private LifecycleService lifecycleService;
    @Mock
    private ClientInfoDto clientInfoDto;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = JwtUnknownIssuerException.class)
    public void testGetSharedSecretShoutThrowJwtUnknownIssuerExceptionIfClientNotFound() throws Exception {
        when(lifecycleService.findClient(CLIENT_KEY)).thenReturn(Optional.empty());

        final JwtIssuerSharedSecretService jwtIssuerSharedSecretService = new JwtIssuerSharedSecretServiceImpl(lifecycleService);
        jwtIssuerSharedSecretService.getSharedSecret(CLIENT_KEY);
    }

    @Test(expected = JwtIssuerLacksSharedSecretException.class)
    public void testGetSharedSecretShoutThrowJwtIssuerLacksSharedSecretExceptionIfClientHasNoSecretKey() throws Exception {
        when(lifecycleService.findClient(CLIENT_KEY)).thenReturn(Optional.of(clientInfoDto));

        final JwtIssuerSharedSecretService jwtIssuerSharedSecretService = new JwtIssuerSharedSecretServiceImpl(lifecycleService);
        jwtIssuerSharedSecretService.getSharedSecret(CLIENT_KEY);
    }

    @Test()
    public void testGetSharedSecret() throws Exception {
        when(clientInfoDto.getSharedSecret()).thenReturn(SECRET);
        when(lifecycleService.findClient(CLIENT_KEY))
                .thenReturn(Optional.of(clientInfoDto));

        final JwtIssuerSharedSecretService jwtIssuerSharedSecretService = new JwtIssuerSharedSecretServiceImpl(lifecycleService);
        final String sharedSecret = jwtIssuerSharedSecretService.getSharedSecret(CLIENT_KEY);
        Assertions.assertThat(sharedSecret).isEqualTo(SECRET);
    }

}