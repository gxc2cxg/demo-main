package com.xuchen.demo.security.controller;

import com.xuchen.demo.api.security.UserClient;
import com.xuchen.demo.model.common.validation.ValidationGroup;
import com.xuchen.demo.model.common.vo.ResponseResult;
import com.xuchen.demo.model.security.dto.UserDto;
import com.xuchen.demo.model.security.dto.UserRoleDto;
import com.xuchen.demo.model.security.dto.UserVerifyDto;
import com.xuchen.demo.model.security.pojo.User;
import com.xuchen.demo.model.security.vo.UserLoginVo;
import com.xuchen.demo.model.security.vo.UserVerifyVo;
import com.xuchen.demo.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/security/user")
public class UserController implements UserClient {

    private final UserService userService;

    @PostMapping("/insert")
    public ResponseResult<Long> insert(@RequestBody @Validated({ValidationGroup.Insert.class}) UserDto userDto) {
        return userService.insert(userDto);
    }

    @DeleteMapping("/delete")
    public ResponseResult<Object> delete(@RequestParam("userIds") List<Long> userIds) {
        return userService.delete(userIds);
    }

    @PutMapping("/update")
    public ResponseResult<Object> update(@RequestBody @Validated({ValidationGroup.Update.class}) UserDto userDto) {
        return userService.update(userDto);
    }

    @GetMapping("/select")
    public ResponseResult<List<User>> select(@RequestBody UserDto userDto) {
        return userService.select(userDto);
    }

    @PostMapping("/bindingRole")
    public ResponseResult<Object> bindingRole(@RequestBody UserRoleDto userRoleDto) {
        return userService.bindingRole(userRoleDto);
    }

    @PostMapping("/login")
    public ResponseResult<UserLoginVo> login (@RequestBody UserDto userDto) {
        return userService.login(userDto);
    }

    @Override
    @PostMapping("/verify")
    public ResponseResult<UserVerifyVo> verify(UserVerifyDto userVerifyDto) {
        return userService.verify(userVerifyDto);
    }

    @GetMapping("/logout")
    public ResponseResult<Object> logout() {
        return userService.logout();
    }
}
