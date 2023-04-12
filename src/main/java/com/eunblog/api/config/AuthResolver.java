package com.eunblog.api.config;

import com.eunblog.api.config.data.UserSession;
import com.eunblog.api.exception.Unauthorized;
import com.eunblog.api.repository.SessionRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {
    private static final String KEY = "bjMPtA04oRHGotaP/5rQmejCSDVPvZYg0KUiFppZxlA=";
    private final SessionRepository sessionRepository;
    private final AppConfig appConfig;
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info(">>{}", appConfig.toString());

        String jws = webRequest.getHeader("Authorization");
        log.info("jws = {}", jws);
        if (jws == null || jws.equals("")) {
            throw new Unauthorized();
        }

        byte[] decodedKey = Base64.decodeBase64(KEY);

        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(decodedKey)
                    .build()
                    .parseClaimsJws(jws);
            log.info("claims = {}", claims);
            String userId = claims.getBody().getSubject();
            log.info("userId = {}", userId);
            return new UserSession(Long.parseLong(userId));
        } catch (JwtException e) {
            throw new Unauthorized();
        }
    }
/*
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
*/
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
