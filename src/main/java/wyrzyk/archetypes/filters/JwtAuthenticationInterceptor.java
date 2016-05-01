package wyrzyk.archetypes.filters;

import com.atlassian.jwt.core.http.JavaxJwtRequestExtractor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import wyrzyk.archetypes.auth.JwtAuthenticatorService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

public class JwtAuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtAuthenticatorService jwtAuthenticator;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        final Optional<String> jwtTokenOptional = extractJwtToken(request);
        if (!jwtTokenOptional.isPresent()) {
            response.setStatus(SC_UNAUTHORIZED);
            return false;
        }
        if (isFirstInstalledRequest((HandlerMethod) o)) {
            return true;
        }
        return jwtAuthenticator.authenticate(request, response);
    }

    private boolean isFirstInstalledRequest(HandlerMethod o) {
        return StringUtils.equals(o.getMethod().getName(), "installed");
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    private Optional<String> extractJwtToken(HttpServletRequest request) {
        final JavaxJwtRequestExtractor javaxJwtRequestExtractor = new JavaxJwtRequestExtractor();
        return ofNullable(javaxJwtRequestExtractor.extractJwt(request));
    }
}
