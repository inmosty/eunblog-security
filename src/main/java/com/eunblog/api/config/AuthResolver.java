package com.eunblog.api.config;

import com.eunblog.api.config.data.UserSession;
import com.eunblog.api.exception.Unauthorized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
public class AuthResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String accessToken = webRequest.getParameter("accessToken");
        if (accessToken == null || !accessToken.equals("")) {
            throw new Unauthorized();
        }
        UserSession userSession = new UserSession();
        userSession.name = "inmosty";
        return userSession;
    }
}
