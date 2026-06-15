package com.ltx.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ltx.entity.po.User;
import com.ltx.entity.request.UserRequestBody;
import com.ltx.mapper.UserMapper;
import com.ltx.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户服务实现类
 *
 * @author tianxing
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Cacheable(value = "userCache", key = "T(com.ltx.common.constant.RedisConstant).CACHE_USER_KEY + (#requestBody.id == null && #requestBody.age == null && #requestBody.name == null ? 'allUsers' : #requestBody.id + ':' + #requestBody.age + ':' + #requestBody.name)", unless = "#result == null || #result.size() == 0")
    public List<User> queryUserList(UserRequestBody requestBody) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (requestBody != null) {
            queryWrapper.eq(requestBody.getId() != null, User::getId, requestBody.getId());
            queryWrapper.le(requestBody.getAge() != null, User::getAge, requestBody.getAge());
            queryWrapper.like(StrUtil.isNotBlank(requestBody.getName()), 
                              User::getName, 
                              StrUtil.trim(requestBody.getName()));
        }
        return userMapper.selectList(queryWrapper);
    }

    @Override
    @CacheEvict(value = "userCache", allEntries = true)
    public User addUser(User user) {
        String password = user.getPassword();
        // 对密码进行加密
        if (StrUtil.isNotBlank(password)) {
            user.setPassword(passwordEncoder.encode(password));
        }
        userMapper.insert(user);
        return user;
    }

    @Override
    @Cacheable(value = "userCache", key = "T(com.ltx.common.constant.RedisConstant).CACHE_USER_KEY + #id", unless = "#result == null")
    public User getUserById(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    @CacheEvict(value = "userCache", allEntries = true)
    public void deleteUserById(Integer id) {
        userMapper.deleteById(id);
    }

    @Override
    @CacheEvict(value = "userCache", allEntries = true)
    public void updateUser(Integer id, User user) {
        user.setId(id);
        String password = user.getPassword();
        // 对密码进行加密
        if (StrUtil.isNotBlank(password)) {
            user.setPassword(passwordEncoder.encode(password));
        }
        userMapper.updateById(user);
    }
}
