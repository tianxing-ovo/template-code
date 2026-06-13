package com.ltx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ltx.entity.SexCount;
import com.ltx.entity.po.User;
import com.ltx.entity.request.UserRequestBody;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tianxing
 */
public interface UserMapper extends BaseMapper<User> {

    List<User> query(@Param("requestBody") UserRequestBody requestBody);

    List<User> queryUserList(@Param("requestBody") UserRequestBody requestBody);

    void add(@Param("user") User user);

    List<SexCount> queryGroupBy(@Param("groupField") String groupField);

}