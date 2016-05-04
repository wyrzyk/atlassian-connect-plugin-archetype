package wyrzyk.archetypes.auth;

import com.atlassian.jwt.core.reader.JwtIssuerSharedSecretService;
import com.atlassian.jwt.exception.JwtIssuerLacksSharedSecretException;
import com.atlassian.jwt.exception.JwtUnknownIssuerException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wyrzyk.archetypes.web.lifecycle.LifecycleService;

import javax.annotation.Nonnull;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class JwtIssuerSharedSecretServiceImpl implements JwtIssuerSharedSecretService {
    private final LifecycleService lifecycleService;

    @Override
    public String getSharedSecret(@Nonnull String issuer) throws JwtIssuerLacksSharedSecretException, JwtUnknownIssuerException {
        return Optional.ofNullable(lifecycleService.findClient(issuer)
                .orElseThrow(() -> new JwtUnknownIssuerException(issuer))
                .getSharedSecret()).orElseThrow(() -> new JwtIssuerLacksSharedSecretException(issuer));
    }
}
