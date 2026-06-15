package com.ltx.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ltx.enums.Sex;
import com.ltx.common.valid.InsertGroup;
import com.ltx.common.valid.ListValue;
import com.ltx.common.valid.UpdateGroup;
import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

/**
 * 查询用户请求体
 *
 * @author tianxing
 */
@Data
@Accessors(chain = true)
public class UserRequestBody {

    @Min(1)
    @Null(message = "新增不能指定id", groups = InsertGroup.class)
    @NotNull(message = "更新必须指定id", groups = UpdateGroup.class)
    private Integer id;

    @NotNull(groups = InsertGroup.class)
    private String name;

    private String username;

    @Max(20)
    private Integer age;

    @ListValue(values = {1, 2}, groups = {InsertGroup.class})
    private Integer status;

    private Sex sex;

    private String password;

    private List<String> hobbies;

    @NotEmpty
    private String province;

    @NotBlank(message = "地址必须提交", groups = {InsertGroup.class, UpdateGroup.class})
    private String address;

    @Size(min = 1, max = 10)
    private String city;

    private String description;

    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;
    private Boolean enabled;

    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonProperty("end_date")
    private LocalDate endDate;
}
