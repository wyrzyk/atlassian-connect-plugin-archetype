package wyrzyk.archetypes.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import wyrzyk.archetypes.auth.JwtService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static java.util.Collections.list;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.apache.commons.lang3.StringUtils.removeStart;
import static org.apache.commons.lang3.StringUtils.startsWith;

public class JwtAuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        final Optional<String> jwtTokenOptional = extractJwtToken(request);
        if (!jwtTokenOptional.isPresent()) {
            response.setStatus(SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    private Optional<String> extractJwtToken(HttpServletRequest request) {
        final String jwtHeaderPrefix = "JWT ";
        return ofNullable(
                of(request)
                        .map(req -> req.getParameter("jwt"))
                        .orElseGet(() -> list(request.getHeaders("Authorization"))
                                .stream()
                                .filter(header -> startsWith(header, jwtHeaderPrefix))
                                .map(header -> removeStart(header, jwtHeaderPrefix))
                                .findFirst()
                                .orElse(null)));
    }
}
