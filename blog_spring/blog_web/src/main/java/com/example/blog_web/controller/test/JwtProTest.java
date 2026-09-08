package com.example.blog_web.controller.test;


import com.example.blog_common.result.Result;
import com.example.blog_domain.dto.user.SignupUserDto;
import com.example.blog_domain.service.userservice.impl.SignupUserSerImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * JwtProTest
 * @author palpitate
 * @date 2023/09/04
 */

@RestController
public class JwtProTest {

    private final SignupUserSerImpl signupUserSerImpl;
    public JwtProTest(SignupUserSerImpl signupUserSerImpl) {
        this.signupUserSerImpl = signupUserSerImpl;
    }



    @PostMapping("/test")
    public Result test(@RequestBody SignupUserDto sud) {
        return signupUserSerImpl.signupUser(sud);
    }


}
