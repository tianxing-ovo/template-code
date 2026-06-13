package com.ltx.entity.po;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ltx.annotation.SensitiveInfo;
import com.ltx.easyexcel.converter.ListConverter;
import com.ltx.easyexcel.converter.RoleConverter;
import com.ltx.easyexcel.converter.SexConverter;
import com.ltx.enums.Role;
import com.ltx.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author tianxing
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SensitiveInfo({"province", "address", "password", "city"})
@TableName(value = "user", autoResultMap = true)
public class User {
    private Integer id;
    @ExcelProperty(value = "姓名")
    private String name;
    @ExcelProperty(value = "年龄")
    private Integer age;
    @ExcelProperty(value = "性别", converter = SexConverter.class)
    private Sex sex;
    @ExcelProperty(value = "密码", converter = ListConverter.class)
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> password;
    @ExcelProperty(value = "省份")
    private String province;
    @ExcelProperty(value = "地址")
    private String address;
    @ExcelProperty(value = "城市")
    private String city;
    @ExcelProperty(value = "描述")
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("my_datetime")
    @ExcelProperty(value = "日期时间")
    private LocalDateTime datetime;
    @TableField("my_date")
    @ExcelProperty(value = "日期")
    private LocalDate date;
    @ExcelProperty(value = "角色", converter = RoleConverter.class)
    private Role role;
}
