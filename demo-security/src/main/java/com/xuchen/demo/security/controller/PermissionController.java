package com.xuchen.demo.security.controller;

import com.xuchen.demo.model.common.validation.ValidationGroup;
import com.xuchen.demo.model.common.vo.ResponseResult;
import com.xuchen.demo.model.security.dto.PermissionDto;
import com.xuchen.demo.model.security.pojo.Permission;
import com.xuchen.demo.security.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/security/permission")
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping("/insert")
    public ResponseResult<Long> insert(@RequestBody @Validated({ValidationGroup.Insert.class}) PermissionDto permissionDto) {
        return permissionService.insert(permissionDto);
    }

    @DeleteMapping("/delete")
    public ResponseResult<Object> delete(@RequestParam("permissionIds") List<Long> permissionIds) {
        return permissionService.delete(permissionIds);
    }

    @PutMapping("/update")
    public ResponseResult<Object> update(@RequestBody @Validated({ValidationGroup.Update.class}) PermissionDto permissionDto) {
        return permissionService.update(permissionDto);
    }

    @GetMapping("/select")
    public ResponseResult<List<Permission>> select(@RequestBody PermissionDto permissionDto) {
        return permissionService.select(permissionDto);
    }
}
