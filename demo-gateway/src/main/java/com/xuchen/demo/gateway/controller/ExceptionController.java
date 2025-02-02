package com.xuchen.demo.gateway.controller;

import com.xuchen.demo.model.common.enums.ResponseStatus;
import com.xuchen.demo.model.common.exception.ClientException;
import com.xuchen.demo.model.common.exception.ServerException;
import com.xuchen.demo.model.gateway.constant.GatewayConstant;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class ExceptionController {

    @RequestMapping(GatewayConstant.EXCEPTION_PATH)
    public void handlerException(@RequestParam(GatewayConstant.EXCEPTION_TYPE) String type, @RequestParam(GatewayConstant.EXCEPTION_STATUS) ResponseStatus status) throws Exception {
        if (Objects.equals(type, ClientException.class.getName())) {
            throw new ClientException(status);
        } else if (Objects.equals(type, ServerException.class.getName())) {
            throw new ServerException(status);
        } else {
            throw new ServerException(ResponseStatus.DEFAULT_SERVER_EXCEPTION);
        }
    }
}
