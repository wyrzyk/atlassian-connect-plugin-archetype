package wyrzyk.archetypes.web.lifecycle;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;
import static wyrzyk.archetypes.auth.Constants.CLIENT_REQUEST_ATTRIBUTE;

public class ClientInfoDtoResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(ClientInfoDto.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        return nativeWebRequest.getAttribute(CLIENT_REQUEST_ATTRIBUTE, SCOPE_REQUEST);
    }
}
