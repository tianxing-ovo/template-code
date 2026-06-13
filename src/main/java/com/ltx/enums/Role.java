package com.ltx.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 角色枚举
 *
 * @author tianxing
 */
@AllArgsConstructor
@Getter
public enum Role {
    ADMIN("admin", "管理员"), USER("user", "普通用户");

    @EnumValue
    private final String value;

    @JsonValue
    private final String desc;

    /**
     * 根据值获取描述
     *
     * @param value 值
     * @return 描述
     */
    public static String getDescByValue(String value) {
        for (Role role : values()) {
            if (role.value.equals(value)) {
                return role.desc;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }

    /**
     * 根据描述获取角色枚举
     *
     * @param desc 描述
     * @return 角色枚举
     */
    public static Role getEnumByDesc(String desc) {
        for (Role role : values()) {
            if (role.desc.equals(desc)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid desc: " + desc);
    }

    @Override
    public String toString() {
        return desc;
    }
}
