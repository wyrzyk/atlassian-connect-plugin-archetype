package wyrzyk.archetypes.api;

import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;

public interface AtlassianConnectUtils {
    static HandlerMethodArgumentResolver createClientDtoResolver() {
        return new ClientInfoDtoResolver();
    }

    static HandlerInterceptor createJwtAuthenticationInterceptor(){
        return new JwtAuthenticationInterceptor();
    }
}
