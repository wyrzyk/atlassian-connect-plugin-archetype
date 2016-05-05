package wyrzyk.archetypes.auth;

import com.atlassian.jwt.Jwt;
import com.atlassian.jwt.core.http.auth.AuthenticationResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Service
@Slf4j
class AuthenticationResultHandlerService implements AuthenticationResultHandler<HttpServletResponse, Boolean> {
    @Override
    public Boolean createAndSendInternalError(Exception e, HttpServletResponse response, String externallyVisibleMessage) {
        log.warn("Internal Error", e);
        response.setStatus(SC_INTERNAL_SERVER_ERROR);
        return false;
    }

    @Override
    public Boolean createAndSendBadRequestError(Exception e, HttpServletResponse response, String externallyVisibleMessage) {
        log.debug("Bad request", e);
        response.setStatus(SC_BAD_REQUEST);
        return false;
    }

    @Override
    public Boolean createAndSendUnauthorisedFailure(Exception e, HttpServletResponse response, String externallyVisibleMessage) {
        log.debug("Unauthorized Failure", e);
        response.setStatus(SC_UNAUTHORIZED);
        return false;
    }

    @Override
    public Boolean createAndSendForbiddenError(Exception e, HttpServletResponse response) {
        response.setStatus(SC_FORBIDDEN);
        return false;
    }

    @Override
    public Boolean success(String message, Principal principal, Jwt authenticatedJwt) {
        log.trace("Succesfully authorized {}", principal);
        return true;
    }
}
