package com.xuchen.demo.model.security.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {

    NORMAL(0),
    FROZEN(1);

    private final Integer status;
}
