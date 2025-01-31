package com.xuchen.demo.model.common.exception;

import com.xuchen.demo.model.common.enums.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClientException extends RuntimeException {
    private final ResponseStatus status;
}
