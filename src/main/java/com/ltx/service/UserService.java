package com.ltx.service;

import com.ltx.entity.po.User;
import com.ltx.entity.request.UserRequestBody;
import java.util.List;

/**
 * 用户服务接口
 *
 * @author tianxing
 */
public interface UserService {

    /**
     * 查询用户列表
     *
     * @param requestBody 请求体
     * @return 用户列表
     */
    List<User> queryUserList(UserRequestBody requestBody);

    /**
     * 新增用户
     *
     * @param user 用户
     * @return 用户
     */
    User addUser(User user);

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户
     */
    User getUserById(Integer id);

    /**
     * 根据ID删除用户
     *
     * @param id 用户ID
     */
    void deleteUserById(Integer id);

    /**
     * 更新用户信息
     *
     * @param id   用户ID
     * @param user 用户
     */
    void updateUser(Integer id, User user);
}
