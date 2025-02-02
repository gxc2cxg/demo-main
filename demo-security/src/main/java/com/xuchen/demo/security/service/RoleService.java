package com.xuchen.demo.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuchen.demo.model.common.vo.ResponseResult;
import com.xuchen.demo.model.security.dto.RoleDto;
import com.xuchen.demo.model.security.dto.RolePermissionDto;
import com.xuchen.demo.model.security.pojo.Role;

import java.util.List;

public interface RoleService extends IService<Role> {

    ResponseResult<Long> insert(RoleDto roleDto);

    ResponseResult<Object> delete(List<Long> roleIds);

    ResponseResult<Object> update(RoleDto roleDto);

    ResponseResult<List<Role>> select(RoleDto roleDto);

    ResponseResult<Object> bindingPermission(RolePermissionDto rolePermissionDto);
}
