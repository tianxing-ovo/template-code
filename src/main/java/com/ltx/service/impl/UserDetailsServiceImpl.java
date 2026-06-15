package com.ltx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ltx.common.constant.Constant;
import com.ltx.entity.dto.SecurityUser;
import com.ltx.entity.po.User;
import com.ltx.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 用户详情服务
 *
 * @author tianxing
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    /**
     * 登录时校验并加载用户信息
     *
     * @param username 用户名
     * @return 用户详情
     * @throws UsernameNotFoundException 用户名不存在异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据username从数据库查询用户信息
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        // 获取用户角色
        String role = user.getRole() != null ? user.getRole().getValue() : "user";
        // 构建用户权限列表
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(Constant.ROLE_PREFIX + role)
        );
        return new SecurityUser(user, authorities);
    }
}
