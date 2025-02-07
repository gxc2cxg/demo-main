package com.xuchen.demo.model.security.dto;

import com.xuchen.demo.model.common.validation.ValidationGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RoleDto {

    @NotNull(message = "角色Id不能为空", groups = {ValidationGroup.Update.class})
    private Long roleId;

    @NotBlank(message = "角色名不能为空", groups = {ValidationGroup.Insert.class})
    private String roleName;

    private Integer status;
}
