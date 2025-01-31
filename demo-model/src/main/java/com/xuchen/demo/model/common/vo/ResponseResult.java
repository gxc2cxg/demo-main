package com.xuchen.demo.model.common.vo;

import com.xuchen.demo.model.common.enums.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseResult<T> {

    private ResponseStatus status;

    private T data;

    public static <T> ResponseResult<T> success() {
        return new ResponseResult<T>(ResponseStatus.SUCCESS, null);
    }

    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<T>(ResponseStatus.SUCCESS, data);
    }

    public static <T> ResponseResult<T> fail(ResponseStatus status) {
        return new ResponseResult<T>(status, null);
    }
}
