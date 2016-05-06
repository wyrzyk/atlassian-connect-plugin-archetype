package wyrzyk.archetypes.auth;

import com.atlassian.jwt.CanonicalHttpRequest;
import com.atlassian.jwt.Jwt;
import com.atlassian.jwt.SigningAlgorithm;
import com.atlassian.jwt.core.reader.NimbusJwtReaderFactory;
import com.atlassian.jwt.core.writer.JsonSmartJwtJsonBuilder;
import com.atlassian.jwt.core.writer.JwtClaimsBuilder;
import com.atlassian.jwt.core.writer.NimbusJwtWriterFactory;
import com.atlassian.jwt.exception.JwtIssuerLacksSharedSecretException;
import com.atlassian.jwt.exception.JwtParseException;
import com.atlassian.jwt.exception.JwtUnknownIssuerException;
import com.atlassian.jwt.exception.JwtVerificationException;
import com.atlassian.jwt.writer.JwtJsonBuilder;
import com.atlassian.jwt.writer.JwtWriterFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
public class JwtService {
    private final NimbusJwtReaderFactory nimbusJwtReaderFactory;

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

    public Optional<String> extractIssuerUnverified(String jwtToken) {
        return extractJwtUnverified(jwtToken)
                .map(Jwt::getIssuer);
    }

    private Optional<Jwt> extractJwtUnverified(String jwtToken) {
        try {
            return Optional.of(nimbusJwtReaderFactory.getReader(jwtToken).readUnverified(jwtToken));
        } catch (JwtParseException | JwtVerificationException | JwtIssuerLacksSharedSecretException e) {
            throw new RuntimeException(e);
        } catch (JwtUnknownIssuerException e) {
            log.warn("Unknown issuer in jwt token {}", jwtToken);
            return Optional.empty();
        }
    }
}
