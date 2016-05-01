package wyrzyk.archetypes.auth;

import com.atlassian.jwt.core.reader.JwtIssuerSharedSecretService;
import com.atlassian.jwt.core.reader.JwtIssuerValidator;
import com.atlassian.jwt.core.reader.NimbusJwtReaderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NimbusJwtReaderService extends NimbusJwtReaderFactory {

    @Autowired
    public NimbusJwtReaderService(JwtIssuerValidator jwtIssuerValidator,
                                  JwtIssuerSharedSecretService jwtIssuerSharedSecretService) {
        super(jwtIssuerValidator, jwtIssuerSharedSecretService);
    }
}
