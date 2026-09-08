package com.example.blog_web.controller.usercontroller;


import com.example.blog_common.result.Result;
import com.example.blog_domain.dto.user.LoginUserDto;
import com.example.blog_domain.service.userservice.LoginUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户登录控制器
 * @author palpitate
 * @date 2023/09/04
 */
@RestController
@RequestMapping("/api/v1/user")
public class LoginUserCon {

    private final LoginUserService loginUserService;
    public LoginUserCon(LoginUserService loginUserService) {
        this.loginUserService = loginUserService;
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginUserDto lud){
        return loginUserService.login(lud);
    }
}
