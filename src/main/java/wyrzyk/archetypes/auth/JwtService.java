package wyrzyk.archetypes.auth;

import com.atlassian.jwt.CanonicalHttpRequest;
import com.atlassian.jwt.SigningAlgorithm;
import com.atlassian.jwt.core.writer.JsonSmartJwtJsonBuilder;
import com.atlassian.jwt.core.writer.JwtClaimsBuilder;
import com.atlassian.jwt.core.writer.NimbusJwtWriterFactory;
import com.atlassian.jwt.writer.JwtJsonBuilder;
import com.atlassian.jwt.writer.JwtWriterFactory;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    public String prepareJwtToken(String clientKey, String sharedSecret, CanonicalHttpRequest canonicalHttpRequest) {
        final long issuedAt = System.currentTimeMillis() / 1000L;
        final long expiresAt = issuedAt + 180L;
        return prepareJwtToken(clientKey, sharedSecret, canonicalHttpRequest, issuedAt, expiresAt);
    }

    public String prepareJwtToken(String clientKey, String sharedSecret, CanonicalHttpRequest canonicalHttpRequest, long issuedAt, long expiresAt) {

        final JwtJsonBuilder jwtBuilder = new JsonSmartJwtJsonBuilder()
                .issuedAt(issuedAt)
                .expirationTime(expiresAt)
                .issuer(clientKey);

        try {
            JwtClaimsBuilder.appendHttpRequestClaims(jwtBuilder, canonicalHttpRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        final JwtWriterFactory jwtWriterFactory = new NimbusJwtWriterFactory();
        final String jwtbuilt = jwtBuilder.build();
        return jwtWriterFactory.macSigningWriter(SigningAlgorithm.HS256,
                sharedSecret).jsonToJwt(jwtbuilt);
    }
}
