package com.xuchen.demo.work.controller;

import com.xuchen.demo.api.work.WorkClient;
import com.xuchen.demo.model.common.vo.ResponseResult;
import com.xuchen.demo.model.work.dto.WorkDto;
import com.xuchen.demo.work.service.WorkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/work")
public class WorkController implements WorkClient {

    private final WorkService workService;

    @Override
    @PostMapping("/insert")
    public ResponseResult<Long> insert(WorkDto workDto) {
        return workService.insert(workDto);
    }

    @Override
    @DeleteMapping("/delete")
    public ResponseResult<Object> delete(WorkDto workDto) {
        return workService.delete(workDto);
    }

    @Override
    @PostMapping("/update")
    public ResponseResult<Object> update(WorkDto workDto) {
        return workService.update(workDto);
    }
}
