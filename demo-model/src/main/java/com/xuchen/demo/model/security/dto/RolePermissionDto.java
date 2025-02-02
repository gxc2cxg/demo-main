package com.xuchen.demo.model.security.dto;

import com.xuchen.demo.model.common.validation.ValidationGroup;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RolePermissionDto {

    @NotNull(message = "角色Id不能为空", groups = {ValidationGroup.Insert.class})
    private Long roleId;

    @NotNull(message = "权限Id不能为空", groups = {ValidationGroup.Insert.class})
    private Long permissionId;
}
