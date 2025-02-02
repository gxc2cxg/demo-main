package com.xuchen.demo.security.controller;

import com.xuchen.demo.model.common.validation.ValidationGroup;
import com.xuchen.demo.model.common.vo.ResponseResult;
import com.xuchen.demo.model.security.dto.RoleDto;
import com.xuchen.demo.model.security.dto.RolePermissionDto;
import com.xuchen.demo.model.security.dto.UserRoleDto;
import com.xuchen.demo.model.security.pojo.Role;
import com.xuchen.demo.security.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/security/role")
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/insert")
    public ResponseResult<Long> insert(@RequestBody @Validated({ValidationGroup.Insert.class}) RoleDto roleDto) {
        return roleService.insert(roleDto);
    }

    @DeleteMapping("/delete")
    public ResponseResult<Object> delete(@RequestParam("roleIds") List<Long> roleIds) {
        return roleService.delete(roleIds);
    }

    @PutMapping("/update")
    public ResponseResult<Object> update(@RequestBody @Validated({ValidationGroup.Update.class}) RoleDto roleDto) {
        return roleService.update(roleDto);
    }

    @GetMapping("/select")
    public ResponseResult<List<Role>> select(@RequestBody RoleDto roleDto) {
        return roleService.select(roleDto);
    }

    @PostMapping("/bindingPermission")
    public ResponseResult<Object> bindingPermission(@RequestBody RolePermissionDto rolePermissionDto) {
        return roleService.bindingPermission(rolePermissionDto);
    }
}
