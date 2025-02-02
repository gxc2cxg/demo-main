package com.xuchen.demo.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuchen.demo.model.security.pojo.UserRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
}
