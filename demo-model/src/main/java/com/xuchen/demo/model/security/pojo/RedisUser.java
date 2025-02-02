package com.xuchen.demo.model.security.pojo;

import lombok.Data;

import java.util.List;

@Data
public class RedisUser {

    private Long userId;

    private List<String> authorizes;
}
