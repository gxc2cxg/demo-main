package com.xuchen.demo.work.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuchen.demo.model.work.pojo.Work;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WorkMapper extends BaseMapper<Work> {
}
