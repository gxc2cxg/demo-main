package com.xuchen.demo.model.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseStatus {

    SUCCESS(2000, "执行成功"),

    DEFAULT_CLIENT_EXCEPTION(3000, "默认客户端错误"),

    DEFAULT_SERVER_EXCEPTION(3000, "默认服务端错误"),
    ;

    private final int code;

    private final String message;
}
