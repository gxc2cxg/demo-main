package com.xuchen.demo.work.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuchen.demo.model.work.pojo.WorkLog;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface WorkLogMapper extends BaseMapper<WorkLog> {
}
