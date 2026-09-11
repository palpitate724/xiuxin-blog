package com.example.blog_web.intercrptor;

import com.example.blog_common.enums.ResultCode;
import com.example.blog_common.utils.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


/**
 * Token拦截器
 * @author palpitate
 * @date 2026/09/10
 */
@Slf4j
@Component
public class TokenIntercrptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;
    public TokenIntercrptor(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {

        String token = req.getHeader("token");
        if (token == null || token.isEmpty()) {
            res.setStatus(ResultCode.UNAUTHORIZED.getCode());
            return false;
        }
        if (!jwtUtils.isTokenValid(token)) {
            res.setStatus(ResultCode.UNAUTHORIZED.getCode());
            return false;
        }
        return true;

    }
}
