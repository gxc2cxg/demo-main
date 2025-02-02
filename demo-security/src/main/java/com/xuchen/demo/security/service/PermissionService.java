package com.xuchen.demo.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuchen.demo.model.common.vo.ResponseResult;
import com.xuchen.demo.model.security.dto.PermissionDto;
import com.xuchen.demo.model.security.pojo.Permission;

import java.util.List;

public interface PermissionService extends IService<Permission> {

    ResponseResult<Long> insert(PermissionDto permissionDto);

    ResponseResult<Object> delete(List<Long> permissionIds);

    ResponseResult<Object> update(PermissionDto permissionDto);

    ResponseResult<List<Permission>> select(PermissionDto permissionDto);
}
