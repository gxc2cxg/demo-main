package com.xuchen.demo.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuchen.demo.model.common.enums.ResponseStatus;
import com.xuchen.demo.model.common.exception.ClientException;
import com.xuchen.demo.model.common.exception.ServerException;
import com.xuchen.demo.model.common.vo.ResponseResult;
import com.xuchen.demo.model.security.dto.RoleDto;
import com.xuchen.demo.model.security.dto.RolePermissionDto;
import com.xuchen.demo.model.security.enums.RoleStatus;
import com.xuchen.demo.model.security.pojo.Permission;
import com.xuchen.demo.model.security.pojo.Role;
import com.xuchen.demo.model.security.pojo.RolePermission;
import com.xuchen.demo.security.mapper.PermissionMapper;
import com.xuchen.demo.security.mapper.RoleMapper;
import com.xuchen.demo.security.mapper.RolePermissionMapper;
import com.xuchen.demo.security.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private final RoleMapper roleMapper;

    private final PermissionMapper permissionMapper;
    private final RolePermissionMapper rolePermissionMapper;

    @Override
    public ResponseResult<Long> insert(RoleDto roleDto) {
        log.info("insert role: {}", roleDto);

        Role role = roleMapper.selectOne(Wrappers.<Role>lambdaQuery().eq(Role::getRoleId, roleDto.getRoleId()));
        if (role != null) {
            throw new ClientException(ResponseStatus.ROLE_EXISTED_EXCEPTION);
        }

        role = new Role();
        BeanUtils.copyProperties(roleDto, role);
        role.setStatus(RoleStatus.NORMAL.getStatus());
        if (roleMapper.insert(role) == 0) {
            throw new ServerException(ResponseStatus.DATABASE_OPERATION_EXCEPTION);
        }
        return ResponseResult.success(role.getRoleId());
    }

    @Override
    public ResponseResult<Object> delete(List<Long> roleIds) {
        log.info("delete role: {}", roleIds);

        List<Role> roles = roleMapper.selectList(Wrappers.<Role>lambdaQuery().in(Role::getRoleId, roleIds));
        if (!roles.isEmpty() && roleMapper.deleteByIds(roleIds) != roles.size()) {
            throw new ServerException(ResponseStatus.DATABASE_OPERATION_EXCEPTION);
        }
        return ResponseResult.success();
    }

    @Override
    public ResponseResult<Object> update(RoleDto roleDto) {
        log.info("update role: {}", roleDto);

        Role role = roleMapper.selectById(roleDto.getRoleId());
        if (role == null) {
            throw new ClientException(ResponseStatus.ROLE_NOT_EXISTED_EXCEPTION);
        }
        if (roleDto.getRoleName() != null) {
            role.setRoleName(roleDto.getRoleName());
        }
        if (roleDto.getStatus() != null) {
            role.setStatus(roleDto.getStatus());
        }
        if (roleMapper.updateById(role) == 0) {
            throw new ServerException(ResponseStatus.DATABASE_OPERATION_EXCEPTION);
        }
        return ResponseResult.success();
    }

    @Override
    public ResponseResult<List<Role>> select(RoleDto roleDto) {
        log.info("select role: {}", roleDto);

        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        if (roleDto.getRoleName() != null) {
            wrapper.in(Role::getRoleName, roleDto.getRoleName());
        }
        if (roleDto.getStatus() != null) {
            wrapper.eq(Role::getStatus, roleDto.getStatus());
        }
        wrapper.orderByDesc(Role::getUpdateTime);
        return ResponseResult.success(roleMapper.selectList(wrapper));
    }

    @Override
    public ResponseResult<Object> bindingPermission(RolePermissionDto rolePermissionDto) {
        log.info("binding permission: {}", rolePermissionDto);

        Role role = roleMapper.selectOne(Wrappers.<Role>lambdaQuery().eq(Role::getRoleId, rolePermissionDto.getRoleId()));
        if (role == null) {
            throw new ClientException(ResponseStatus.ROLE_NOT_EXISTED_EXCEPTION);
        }
        Permission permission = permissionMapper.selectOne(Wrappers.<Permission>lambdaQuery().eq(Permission::getPermissionId, rolePermissionDto.getPermissionId()));
        if (permission == null) {
            throw new ClientException(ResponseStatus.PERMISSION_NOT_EXISTED_EXCEPTION);
        }
        RolePermission rolePermission = rolePermissionMapper.selectOne(Wrappers.<RolePermission>lambdaQuery().eq(RolePermission::getRoleId, rolePermissionDto.getRoleId()).eq(RolePermission::getPermissionId, rolePermissionDto.getPermissionId()));
        if (rolePermission != null) {
            throw new ClientException(ResponseStatus.ROLE_PERMISSION_EXISTED_EXCEPTION);
        }

        rolePermission = new RolePermission();
        BeanUtils.copyProperties(rolePermissionDto, rolePermission);
        if (rolePermissionMapper.insert(rolePermission) == 0) {
            throw new ServerException(ResponseStatus.DATABASE_OPERATION_EXCEPTION);
        }
        return ResponseResult.success(rolePermission.getRolePermissionId());
    }
}
