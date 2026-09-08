package com.example.blog_domain.service.userservice;

import com.example.blog_common.result.Result;
import com.example.blog_domain.dto.user.SignupUserDto;

/**
 * Signup User Service
 * @author palpitate
 * @date 2023/09/04
 */

public interface SignupUserService {

    Result signupUser(SignupUserDto signupUserDto);

}
