package com.example.blog_domain.service.userservice.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.blog_common.enums.ResultCode;
import com.example.blog_common.utils.bcrypt.BcryptUtils;
import com.example.blog_common.utils.jwt.JwtUtils;
import com.example.blog_common.utils.minio.MinioUtils;
import com.example.blog_domain.dto.user.LoginUserDto;
import com.example.blog_domain.entity.UserEntity;
import com.example.blog_domain.mapper.UserMapper;
import com.example.blog_common.result.Result;
import com.example.blog_domain.service.userservice.LoginUserService;
import com.example.blog_domain.vo.user.LoginUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户登录service
 * @author palpitate
 * @date 2026/09/09
 */
@Transactional(rollbackFor = Exception.class)
@Slf4j
@Service
public class LoginUserSerImpl implements LoginUserService {
    /**
     * Bean 构造器注入
     */
    private final UserMapper userMapper;
    private final MinioUtils minioUtils;
    private final JwtUtils jwtUtils;
    public LoginUserSerImpl(UserMapper userMapper, MinioUtils minioUtils, JwtUtils jwtUtils) {
        this.userMapper = userMapper;
        this.jwtUtils = jwtUtils;
        this.minioUtils = minioUtils;
    }
    private static final Result result = Result.getInstance();

    /**
     * 用户登录
     * @param lud
     * @return login result
     */
    @Override
    public Result login(LoginUserDto lud) {

        Result result = Result.getInstance();

        UserEntity ue = userMapper.selectOne(
                new LambdaQueryWrapper<UserEntity>()
                        .eq(UserEntity::getUsername, lud.getUsername())
                        .eq(UserEntity::getDeleted, 0)
        );
        // 用户不存在
        if (ue == null){
            result.setCode(ResultCode.USER_NOT_EXIST.getCode());
            result.setMessage(ResultCode.USER_NOT_EXIST.getMessage());
            result.setData(null);
        }
        // 密码错误
        else if (!BcryptUtils.jiemi(lud.getPassword(),ue.getPassword())){
            result.setCode(ResultCode.PASSWORD_ERROR.getCode());
            result.setMessage(ResultCode.PASSWORD_ERROR.getMessage());
            result.setData(null);
        }
        // 登录成功
        else {
            LoginUserVo luv = new LoginUserVo();
            BeanUtils.copyProperties(ue,luv);
            luv.setTouxiangurl(minioUtils.getObjectUrl(ue.getTouxiangurl()));
            luv.setToken(jwtUtils.getToken(ue.getId(),ue.getUsername()));

            result.setCode(ResultCode.SUCCESS.getCode());
            result.setMessage(ResultCode.SUCCESS.getMessage());
            result.setData(luv);
        }
        return result;
    }
}
