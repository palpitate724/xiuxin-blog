package com.example.blog_web.intercrptor;

import com.example.blog_common.enums.ResultCode;
import com.example.blog_common.utils.jwt.JwtUtils;
import com.example.blog_web.config.IntercptoAnnotation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.method.HandlerMethod;


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
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {


        if (! (handler instanceof HandlerMethod)){
            log.info("非方法调用，不进行拦截");
            return true;
        }
        HandlerMethod method = (HandlerMethod) handler;
        IntercptoAnnotation annotation = method.getMethodAnnotation(IntercptoAnnotation.class);

        // 如果方法上没有注解，则不进行拦截
        if (annotation == null){
            log.info("方法上没有注解，不进行拦截");
            return true;
        }

        // 执行拦截逻辑
        // 获取token
        String token = req.getHeader("token");
        if (token == null || token.isEmpty()) {
            log.info("token为空");
            res.setStatus(ResultCode.UNAUTHORIZED.getCode());
            return false;
        }
        if (!jwtUtils.isTokenValid(token)) {
            log.info("token无效");
            res.setStatus(ResultCode.UNAUTHORIZED.getCode());
            return false;
        }
        log.info("token有效，放行请求");
        return true;

    }
}
