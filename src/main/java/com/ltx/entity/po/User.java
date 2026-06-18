package com.ltx.entity.po;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ltx.common.annotation.SensitiveInfo;
import com.ltx.common.easyexcel.converter.ListConverter;
import com.ltx.common.easyexcel.converter.RoleConverter;
import com.ltx.common.easyexcel.converter.SexConverter;
import com.ltx.enums.Role;
import com.ltx.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户实体类
 * 
 * @author tianxing
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SensitiveInfo({"province", "address", "password", "city"})
@TableName(value = "user", autoResultMap = true)
public class User {
    @ColumnWidth(8)
    private Integer id;
    @ExcelProperty(value = "姓名")
    @ColumnWidth(10)
    private String name;
    @ExcelProperty(value = "用户名")
    @ColumnWidth(12)
    private String username;
    @ExcelProperty(value = "年龄")
    @ColumnWidth(8)
    private Integer age;
    @ExcelProperty(value = "性别", converter = SexConverter.class)
    @ColumnWidth(8)
    private Sex sex;
    @ExcelProperty(value = "密码")
    @ColumnWidth(12)
    private String password;
    @ExcelProperty(value = "兴趣爱好", converter = ListConverter.class)
    @TableField(typeHandler = JacksonTypeHandler.class)
    @ColumnWidth(15)
    private List<String> hobbies = new ArrayList<>();
    @ExcelProperty(value = "省份")
    @ColumnWidth(12)
    private String province;
    @ExcelProperty(value = "地址")
    @ColumnWidth(30)
    private String address;
    @ExcelProperty(value = "城市")
    @ColumnWidth(10)
    private String city;
    @ExcelProperty(value = "描述")
    @ColumnWidth(15)
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ExcelProperty(value = "创建时间")
    @ColumnWidth(20)
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ExcelProperty(value = "更新时间")
    @ColumnWidth(20)
    private LocalDateTime updateTime;
    @ExcelProperty(value = "角色", converter = RoleConverter.class)
    @ColumnWidth(10)
    private Role role;
    @TableField(value = "account_non_expired")
    private Boolean accountNonExpired = true;
    @TableField(value = "account_non_locked")
    private Boolean accountNonLocked = true;
    @TableField(value = "credentials_non_expired")
    private Boolean credentialsNonExpired = true;
    @TableField(value = "enabled")
    private Boolean enabled = true;
}
