package com.xuchen.demo.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuchen.demo.model.security.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    List<String> selectUserAuthorities(Long userId);
}
