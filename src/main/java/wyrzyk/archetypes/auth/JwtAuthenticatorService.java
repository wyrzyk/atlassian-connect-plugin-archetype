package wyrzyk.archetypes.auth;

import com.atlassian.jwt.Jwt;
import com.atlassian.jwt.core.http.JavaxJwtRequestExtractor;
import com.atlassian.jwt.core.http.auth.AbstractJwtAuthenticator;
import com.atlassian.jwt.core.http.auth.AuthenticationResultHandler;
import com.atlassian.jwt.core.reader.NimbusJwtReaderFactory;
import com.atlassian.jwt.exception.JwtIssuerLacksSharedSecretException;
import com.atlassian.jwt.exception.JwtParseException;
import com.atlassian.jwt.exception.JwtUnknownIssuerException;
import com.atlassian.jwt.exception.JwtUserRejectedException;
import com.atlassian.jwt.exception.JwtVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wyrzyk.archetypes.api.ClientInfoDto;
import wyrzyk.archetypes.lifecycle.LifecycleService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;
import static wyrzyk.archetypes.auth.Constants.CLIENT_REQUEST_ATTRIBUTE;

@Service
public class JwtAuthenticatorService extends AbstractJwtAuthenticator<HttpServletRequest, HttpServletResponse, Boolean> {
    private final NimbusJwtReaderFactory nimbusJwtReaderFactory;
    private final LifecycleService lifecycleService;

    @Autowired
    public JwtAuthenticatorService(AuthenticationResultHandler authenticationResultHandler,
                                   NimbusJwtReaderFactory nimbusJwtReaderFactory, LifecycleService lifecycleService) {
        super(new JavaxJwtRequestExtractor(), authenticationResultHandler);
        this.nimbusJwtReaderFactory = nimbusJwtReaderFactory;
        this.lifecycleService = lifecycleService;
    }

    @Override
    protected Jwt verifyJwt(String jwt, Map claimVerifiers) throws JwtParseException, JwtVerificationException, JwtIssuerLacksSharedSecretException, JwtUnknownIssuerException, IOException, NoSuchAlgorithmException {
        return nimbusJwtReaderFactory.getReader(jwt).readAndVerify(jwt, claimVerifiers);
    }

    @Override
    protected void tagRequest(HttpServletRequest request, Jwt jwt) throws JwtUserRejectedException {
        final Optional<ClientInfoDto> client = lifecycleService.findClient(jwt.getIssuer());
        request.setAttribute(CLIENT_REQUEST_ATTRIBUTE,
                client.orElseThrow(() -> new JwtUserRejectedException(format("Client %s not found.", jwt.getIssuer()))));
    }
}
