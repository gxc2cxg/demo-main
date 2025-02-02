package com.xuchen.demo.api.security;

import com.xuchen.demo.model.common.vo.ResponseResult;
import com.xuchen.demo.model.security.dto.UserVerifyDto;
import com.xuchen.demo.model.security.vo.UserVerifyVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "demo-security")
public interface UserClient {

    @PostMapping("/security/user/verify")
    ResponseResult<UserVerifyVo> verify(@RequestBody UserVerifyDto userVerifyDto);
}
