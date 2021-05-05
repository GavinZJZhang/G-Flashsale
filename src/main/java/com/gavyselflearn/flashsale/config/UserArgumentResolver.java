package com.gavyselflearn.flashsale.config;

import com.gavyselflearn.flashsale.domain.FlashsaleUser;
import com.gavyselflearn.flashsale.service.FlashsaleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    FlashsaleUserService userService;

    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();
        return clazz == FlashsaleUser.class;
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
         NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);

        String paramToken = request.getParameter(FlashsaleUserService.COOK1_NAME_TOKEN);
        String cookieToken = getCookieValue(request, FlashsaleUserService.COOK1_NAME_TOKEN);
        if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return null;
        }
        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;

        return token;

    }

    private String getCookieValue(HttpServletRequest request, String cookieToken) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null || cookies.length <= 0) {
            return null;
        }
        for(Cookie cookie:cookies) {
            if(cookie.getName().equals(cookieToken)) {
                return cookie.getValue();
            }
        }
        return null;
    }

}
