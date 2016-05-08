package wyrzyk.archetypes.api;

import com.atlassian.jwt.core.http.JavaxJwtRequestExtractor;
import com.atlassian.jwt.core.http.JwtRequestExtractor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import wyrzyk.archetypes.auth.JwtAuthenticatorService;
import wyrzyk.archetypes.auth.JwtService;
import wyrzyk.archetypes.lifecycle.LifecycleService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

class JwtAuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtAuthenticatorService jwtAuthenticator;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private LifecycleService lifecycleService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        final Optional<String> jwtTokenOptional = extractJwtToken(request);
        if (!jwtTokenOptional.isPresent()) {
            response.setStatus(SC_UNAUTHORIZED);
            return false;
        }
        if (isFirstInstalledRequest((HandlerMethod) o, jwtTokenOptional)) {
            return true;
        }
        return jwtAuthenticator.authenticate(request, response);
    }

    private boolean isFirstInstalledRequest(HandlerMethod o, Optional<String> jwtTokenOptional) {
        final boolean isInstalledRequest = StringUtils.equals(o.getMethod().getName(), "installed");
        final boolean isClientAlreadyInstalled = !jwtTokenOptional
                .flatMap(jwtService::extractIssuerUnverified)
                .flatMap(lifecycleService::findClient)
                .isPresent();
        return isInstalledRequest && isClientAlreadyInstalled;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    private Optional<String> extractJwtToken(HttpServletRequest request) {
        final JwtRequestExtractor javaxJwtRequestExtractor = new JavaxJwtRequestExtractor();
        return ofNullable(javaxJwtRequestExtractor.extractJwt(request));
    }
}
