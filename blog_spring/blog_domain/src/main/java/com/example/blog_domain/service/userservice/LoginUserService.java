package com.example.blog_domain.service.userservice;

import com.example.blog_common.result.Result;
import com.example.blog_domain.dto.user.LoginUserDto;

/**
 * Login User Service
 * @author palpitate
 * @date 2023/09/04
 */
public interface LoginUserService {
    /**
     * 用户登录
     * @param lud
     * @return login result
     */
    Result login(LoginUserDto lud);
}
