package com.xuchen.demo.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuchen.demo.model.common.vo.ResponseResult;
import com.xuchen.demo.model.security.dto.UserDto;
import com.xuchen.demo.model.security.dto.UserRoleDto;
import com.xuchen.demo.model.security.dto.UserVerifyDto;
import com.xuchen.demo.model.security.pojo.User;
import com.xuchen.demo.model.security.vo.UserLoginVo;
import com.xuchen.demo.model.security.vo.UserVerifyVo;

import java.util.List;

public interface UserService extends IService<User> {

    ResponseResult<Long> insert(UserDto userDto);

    ResponseResult<Object> delete(List<Long> userIds);

    ResponseResult<Object> update(UserDto userDto);

    ResponseResult<List<User>> select(UserDto userDto);

    ResponseResult<Object> bindingRole(UserRoleDto userRoleDto);

    ResponseResult<UserLoginVo> login(UserDto userDto);

    ResponseResult<UserVerifyVo> verify(UserVerifyDto userVerifyDto);

    ResponseResult<Object> logout();
}
