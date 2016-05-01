package wyrzyk.archetypes.auth;

import com.atlassian.jwt.Jwt;
import com.atlassian.jwt.core.http.auth.AuthenticationResultHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Service
class AuthenticationResultHandlerService implements AuthenticationResultHandler<HttpServletResponse, Boolean> {
    @Override
    public Boolean createAndSendInternalError(Exception e, HttpServletResponse response, String externallyVisibleMessage) {
        return false;
    }

    @Override
    public Boolean createAndSendBadRequestError(Exception e, HttpServletResponse response, String externallyVisibleMessage) {
        return false;
    }

    @Override
    public Boolean createAndSendUnauthorisedFailure(Exception e, HttpServletResponse response, String externallyVisibleMessage) {
        return false;
    }

    @Override
    public Boolean createAndSendForbiddenError(Exception e, HttpServletResponse response) {
        return false;
    }

    @Override
    public Boolean success(String message, Principal principal, Jwt authenticatedJwt) {
        return true;
    }
}
