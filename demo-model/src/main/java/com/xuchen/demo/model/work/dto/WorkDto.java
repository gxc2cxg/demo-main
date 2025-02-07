package com.xuchen.demo.model.work.dto;

import com.xuchen.demo.model.common.validation.ValidationGroup;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class WorkDto {

    @NotNull(message = "任务ID不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Delete.class})
    private Long workId;

    private String exchange;

    private String bindingKey;

    private Integer priority;

    private String params;

    private String message;

    private Long createUser;

    @NotNull(message = "任务执行时间不能为空", groups = {ValidationGroup.Insert.class})
    private Date executeTime;

    @NotNull(message = "任务状态不能为空", groups = {ValidationGroup.Update.class})
    private Integer status;
}
