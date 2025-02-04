package com.xuchen.demo.model.work.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_work_log")
public class WorkLog {

    @TableId(value = "log_id", type = IdType.ASSIGN_ID)
    private Long logId;

    @TableField("work_id")
    private Long workId;

    @TableField("exchange")
    private String exchange;

    @TableField("binding_key")
    private String bindingKey;

    @TableField("priority")
    private Integer priority;

    @TableField("params")
    private String params;

    @TableField("execute_time")
    private Date executeTime;

    @TableField("status")
    private Integer status;

    @TableField("message")
    private String message;

    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
