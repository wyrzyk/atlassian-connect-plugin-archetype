package wyrzyk.archetypes.api;

import lombok.experimental.UtilityClass;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;

@UtilityClass
public class AtlassianConnectUtils {
    public static HandlerMethodArgumentResolver createClientDtoResolver() {
        return new ClientInfoDtoResolver();
    }

    public static HandlerInterceptor createJwtAuthenticationInterceptor() {
        return new JwtAuthenticationInterceptor();
    }
}
