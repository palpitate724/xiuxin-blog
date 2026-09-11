package com.example.blog_web.controller.usercontroller;


import com.example.blog_common.result.Result;
import com.example.blog_domain.dto.user.SignupUserDto;
import com.example.blog_domain.service.userservice.SignupUserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户注册控制器
 * @author palpitate
 * @date 2026/09/09
 */
@Transactional(rollbackFor = Exception.class)
@RestController
@RequestMapping("/api/v1/user")
public class SignupUserCon {

    private final SignupUserService signupUserService;
    public SignupUserCon(SignupUserService signupUserService) {
        this.signupUserService = signupUserService;
    }

    /**
     * 用户注册
     * @param sud
     * @return
     */
    @PostMapping("/signup")
    public Result signup(@RequestBody SignupUserDto sud){
        return signupUserService.signupUser(sud);
    }


}
