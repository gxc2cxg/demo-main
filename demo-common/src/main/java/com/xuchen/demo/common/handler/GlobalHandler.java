package com.xuchen.demo.common.handler;

import com.xuchen.demo.model.common.exception.ClientException;
import com.xuchen.demo.model.common.exception.ServerException;
import com.xuchen.demo.model.common.vo.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@Slf4j
@ControllerAdvice
public class GlobalHandler {

    @ResponseBody
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ExceptionHandler(ClientException.class)
    public ResponseResult<Object> clientException(ClientException e) {
        log.error("客户端异常：{}", e.getMessage(), e);
        return ResponseResult.fail(e.getStatus());
    }

    @ResponseBody
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ExceptionHandler(ServerException.class)
    public ResponseResult<Object> serverException(ServerException e) {
        log.error("服务端异常：{}", e.getMessage(), e);
        return ResponseResult.fail(e.getStatus());
    }
}
