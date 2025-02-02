package com.xuchen.demo.model.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseStatus {

    SUCCESS(2000, "执行成功"),

    DEFAULT_CLIENT_EXCEPTION(3000, "默认客户端错误"),
    INVALID_TOKEN_EXCEPTION(3001, "不合法的Token"),
    USER_EXISTED_EXCEPTION(3002, "用户已存在"),
    USER_NOT_EXISTED_EXCEPTION(3003, "用户不存在"),
    ROLE_EXISTED_EXCEPTION(3004, "角色已存在"),
    ROLE_NOT_EXISTED_EXCEPTION(3005, "角色不存在"),
    PERMISSION_EXISTED_EXCEPTION(3006, "权限已存在"),
    PERMISSION_NOT_EXISTED_EXCEPTION(3007, "权限不存在"),
    USER_ROLE_EXISTED_EXCEPTION(3008, "用户-角色已存在"),
    ROLE_PERMISSION_EXISTED_EXCEPTION(3009, "角色-权限已存在"),
    USER_FROZEN_EXCEPTION(3010, "用户名已冻结"),
    WRONG_PASSWORD_EXCEPTION(3011, "密码错误"),
    USER_NOLOGGING_EXCEPTION(3012, "用户未登录"),

    DEFAULT_SERVER_EXCEPTION(4000, "默认服务端错误"),
    DATABASE_OPERATION_EXCEPTION(4001, "数据库操作失败"),
    ;

    private final int code;

    private final String message;
}
