package com.eunblog.api.config;

import com.eunblog.api.config.data.UserSession;
import com.eunblog.api.domain.Session;
import com.eunblog.api.exception.Unauthorized;
import com.eunblog.api.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {
    private final SessionRepository sessionRepository;
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        if (servletRequest == null) throw new Unauthorized();
        Cookie[] cookies = servletRequest.getCookies();
        log.info("cookies= {}", cookies);
        if (cookies.length == 0) throw new Unauthorized();

        String accessToken = cookies[0].getValue();
        log.info("accessToken = {}", accessToken);

        Session sessionOption = sessionRepository.findByAccessToken(accessToken).orElseThrow(Unauthorized::new);

        return new UserSession(sessionOption.getUser().getId());
    }
/* old
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        //String accessToken = webRequest.getParameter("accessToken");
        String accessToken = webRequest.getHeader("Authorization");
        log.info("accessToken= {}", accessToken);
        if (accessToken == null || accessToken.equals("")) {
            throw new Unauthorized();
        }

        Session sessionOption = sessionRepository.findByAccessToken(accessToken).orElseThrow(Unauthorized::new);

        return new UserSession(sessionOption.getUser().getId());
    }
*/
}
