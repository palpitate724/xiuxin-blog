package com.example.blog_domain.service.userservice.impl;

import com.example.blog_common.enums.ResultCode;
import com.example.blog_common.result.Result;
import com.example.blog_common.utils.bcrypt.BcryptUtils;
import com.example.blog_domain.service.userservice.SignupUserService;

import com.example.blog_common.enums.UserRole;
import com.example.blog_domain.dto.user.SignupUserDto;
import com.example.blog_domain.entity.UserEntity;
import com.example.blog_domain.entity.UserRoleEntity;
import com.example.blog_domain.mapper.UserMapper;
import com.example.blog_domain.mapper.UserRoleMapper;
import com.example.blog_domain.vo.user.SignupUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 用户注册service
 * @author palpitate
 * @date 2023/09/04
 */
@Slf4j
@Service
public class SignupUserSerImpl implements SignupUserService {


    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    public SignupUserSerImpl(UserMapper userMapper, UserRoleMapper userRoleMapper) {
        this.userMapper = userMapper;
        this.userRoleMapper = userRoleMapper;
    }

    /**
     * 用户注册
     * @param
     * @return
     */
    @Override
    public Result signupUser(SignupUserDto sud) {
        Result result = Result.getInstance();
        try{
            UserEntity ue = new UserEntity();
            BeanUtils.copyProperties(sud, ue);
            ue.setPassword(BcryptUtils.jiami(sud.getPassword()));
            userMapper.insert(ue);

            UserRoleEntity ure = new UserRoleEntity();
            ure.setUserid(ue.getId());
            ure.setRoleid(UserRole.USER.getCode());
            userRoleMapper.insert(ure);

            SignupUserVo suv = new SignupUserVo();
            BeanUtils.copyProperties(ue, suv);
            result.setCode(ResultCode.SUCCESS.getCode());
            result.setMessage(ResultCode.SUCCESS.getMessage());
            result.setData(suv);
            return result;

        }catch (Exception e){
            result.setCode(ResultCode.FAIL.getCode());
            result.setMessage(ResultCode.FAIL.getMessage());
            result.setData(null);
            return result;
        }
    }

}
