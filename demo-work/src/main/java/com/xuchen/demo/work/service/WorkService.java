package com.xuchen.demo.work.service;

import com.xuchen.demo.model.common.vo.ResponseResult;
import com.xuchen.demo.model.work.dto.WorkDto;

public interface WorkService {
    ResponseResult<Long> insert(WorkDto workDto);

    ResponseResult<Object> delete(WorkDto workDto);

    ResponseResult<Object> update(WorkDto workDto);
}
