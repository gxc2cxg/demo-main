package com.xuchen.demo.api.work;

import com.xuchen.demo.model.common.validation.ValidationGroup;
import com.xuchen.demo.model.common.vo.ResponseResult;
import com.xuchen.demo.model.work.dto.WorkDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "demo-work")
public interface WorkClient {

    @PostMapping("/work/insert")
    ResponseResult<Long> insert(@RequestBody @Validated({ValidationGroup.Insert.class}) WorkDto workDto);

    @DeleteMapping("/work/delete")
    ResponseResult<Object> delete(@RequestBody @Validated({ValidationGroup.Delete.class}) WorkDto workDto);

    @PostMapping("/work/update")
    ResponseResult<Object> update(@RequestBody @Validated({ValidationGroup.Delete.class}) WorkDto workDto);
}
