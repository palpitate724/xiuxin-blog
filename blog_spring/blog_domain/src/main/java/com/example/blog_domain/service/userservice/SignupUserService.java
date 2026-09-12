package com.example.blog_domain.service.userservice;

import com.example.blog_common.result.Result;
import com.example.blog_domain.dto.user.SignupUserDto;

/**
 * Signup User Service
 * @author palpitate
 * @date 2026/09/09
 */

public interface SignupUserService {

    Result signupUser(SignupUserDto signupUserDto);

}
