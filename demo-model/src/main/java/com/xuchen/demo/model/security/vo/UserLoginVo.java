package com.xuchen.demo.model.security.vo;

import lombok.Data;

@Data
public class UserLoginVo {

    private Long userId;

    private String token;
}
