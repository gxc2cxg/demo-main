package com.xuchen.demo.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuchen.demo.model.common.enums.ResponseStatus;
import com.xuchen.demo.model.common.exception.ClientException;
import com.xuchen.demo.model.common.exception.ServerException;
import com.xuchen.demo.model.common.vo.ResponseResult;
import com.xuchen.demo.model.security.dto.PermissionDto;
import com.xuchen.demo.model.security.enums.PermissionStatus;
import com.xuchen.demo.model.security.pojo.Permission;
import com.xuchen.demo.security.mapper.PermissionMapper;
import com.xuchen.demo.security.service.PermissionService;
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
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    private final PermissionMapper permissionMapper;

    @Override
    public ResponseResult<Long> insert(PermissionDto permissionDto) {
        log.info("insert permission: {}", permissionDto);

        Permission permission = permissionMapper.selectOne(Wrappers.<Permission>lambdaQuery().eq(Permission::getPermissionName, permissionDto.getPermissionName()));
        if (permission != null) {
            throw new ClientException(ResponseStatus.PERMISSION_EXISTED_EXCEPTION);
        }

        permission = new Permission();
        BeanUtils.copyProperties(permissionDto, permission);
        permission.setStatus(PermissionStatus.NORMAL.getStatus());
        if (permissionMapper.insert(permission) == 0) {
            throw new ServerException(ResponseStatus.DATABASE_OPERATION_EXCEPTION);
        }
        return ResponseResult.success(permission.getPermissionId());
    }

    @Override
    public ResponseResult<Object> delete(List<Long> permissionIds) {
        log.info("delete permission: {}", permissionIds);

        List<Permission> permissions = permissionMapper.selectList(Wrappers.<Permission>lambdaQuery().in(Permission::getPermissionId, permissionIds));
        if (!permissions.isEmpty() && permissionMapper.deleteByIds(permissionIds) != permissions.size()) {
            throw new ServerException(ResponseStatus.DATABASE_OPERATION_EXCEPTION);
        }
        return ResponseResult.success();
    }

    @Override
    public ResponseResult<Object> update(PermissionDto permissionDto) {
        log.info("update permission: {}", permissionDto);

        Permission permission = permissionMapper.selectById(permissionDto.getPermissionId());
        if (permission == null) {
            throw new ClientException(ResponseStatus.PERMISSION_NOT_EXISTED_EXCEPTION);
        }
        if (permissionDto.getPermissionName() != null) {
            permission.setPermissionName(permissionDto.getPermissionName());
        }
        if (permissionDto.getStatus() != null) {
            permission.setStatus(permissionDto.getStatus());
        }
        if (permissionMapper.updateById(permission) == 0) {
            throw new ServerException(ResponseStatus.DATABASE_OPERATION_EXCEPTION);
        }
        return ResponseResult.success();
    }

    @Override
    public ResponseResult<List<Permission>> select(PermissionDto permissionDto) {
        log.info("select permission: {}", permissionDto);

        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        if (permissionDto.getPermissionName() != null) {
            wrapper.like(Permission::getPermissionName, permissionDto.getPermissionName());
        }
        if (permissionDto.getStatus() != null) {
            wrapper.eq(Permission::getStatus, permissionDto.getStatus());
        }
        wrapper.orderByDesc(Permission::getUpdateTime);
        return ResponseResult.success(permissionMapper.selectList(wrapper));
    }
}
