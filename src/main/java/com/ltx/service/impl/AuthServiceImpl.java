package com.ltx.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ltx.common.constant.Constant;
import com.ltx.common.exception.BusinessException;
import com.ltx.common.util.JwtUtil;
import com.ltx.common.util.RedisUtil;
import com.ltx.entity.po.User;
import com.ltx.enums.ErrorCode;
import com.ltx.enums.Role;
import com.ltx.mapper.UserMapper;
import com.ltx.service.AuthService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现类
 *
 * @author tianxing
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    @Override
    public String login(String username, String password) {
        // 根据用户名查询用户并校验密码(统一提示防用户名枚举攻击)
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user == null || !BCrypt.checkpw(password, user.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }
        // 校验账号状态
        if (Boolean.FALSE.equals(user.getEnabled())) {
            throw new BusinessException(ErrorCode.ACCOUNT_DISABLED);
        }
        if (Boolean.FALSE.equals(user.getAccountNonLocked())) {
            throw new BusinessException(ErrorCode.ACCOUNT_LOCKED);
        }
        // 生成token
        String token = jwtUtil.createJws(user, 2, TimeUnit.HOURS);
        // {key = login:user:用户ID, value = token}
        String key = Constant.LOGIN_USER_KEY + user.getId();
        redisUtil.set(key, token, 2, TimeUnit.HOURS);
        return token;
    }

    @Override
    public void logout(String token) {
        if (StrUtil.isBlank(token)) {
            throw new BusinessException(ErrorCode.TOKEN_IS_NULL);
        }
        User user;
        try {
            // 校验token
            Claims claims = jwtUtil.getPayLoad(token);
            // 从token中获取用户
            user = Convert.convert(User.class, claims.get(Constant.USER));
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.TOKEN_INVALID);
        }
        // 删除Redis中的token
        redisUtil.delete(Constant.LOGIN_USER_KEY + user.getId());
    }
}
