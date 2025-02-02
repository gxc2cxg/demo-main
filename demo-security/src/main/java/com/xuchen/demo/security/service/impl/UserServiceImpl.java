package com.xuchen.demo.security.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuchen.demo.common.util.UserContext;
import com.xuchen.demo.model.common.enums.ResponseStatus;
import com.xuchen.demo.model.common.exception.ClientException;
import com.xuchen.demo.model.common.exception.ServerException;
import com.xuchen.demo.model.common.vo.ResponseResult;
import com.xuchen.demo.model.security.constant.SecurityConstant;
import com.xuchen.demo.model.security.dto.UserDto;
import com.xuchen.demo.model.security.dto.UserRoleDto;
import com.xuchen.demo.model.security.dto.UserVerifyDto;
import com.xuchen.demo.model.security.enums.UserStatus;
import com.xuchen.demo.model.security.pojo.RedisUser;
import com.xuchen.demo.model.security.pojo.Role;
import com.xuchen.demo.model.security.pojo.User;
import com.xuchen.demo.model.security.pojo.UserRole;
import com.xuchen.demo.model.security.vo.UserLoginVo;
import com.xuchen.demo.model.security.vo.UserVerifyVo;
import com.xuchen.demo.security.config.JwtProperties;
import com.xuchen.demo.security.config.PermissionsProperties;
import com.xuchen.demo.security.mapper.RoleMapper;
import com.xuchen.demo.security.mapper.UserMapper;
import com.xuchen.demo.security.mapper.UserRoleMapper;
import com.xuchen.demo.security.service.UserService;
import com.xuchen.demo.security.util.JwtTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AntPathMatcher;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;

    private final RoleMapper roleMapper;

    private final UserRoleMapper userRoleMapper;

    private final RedisTemplate<String, String> redisTemplate;

    private final RedissonClient redissonClient;

    private final JwtTool jwtTool;

    private final JwtProperties jwtProperties;

    private RBloomFilter<Object> bloomFilter;

    private final PasswordEncoder passwordEncoder;

    private final PermissionsProperties permissionsProperties;

    private final HashMap<String, Set<String>> map = new HashMap<>();

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @PostConstruct
    public void init() {
        bloomFilter = redissonClient.getBloomFilter(SecurityConstant.SECURITY_FILTER_NAME);
        bloomFilter.tryInit(1000000L, 0.05);
        bloomFilter.add("admin");
    }

    @Override
    public ResponseResult<Long> insert(UserDto userDto) {
        log.info("insert user: {}", userDto);

        boolean contains = bloomFilter.contains(userDto.getUsername());
        if (contains) {
            User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, userDto.getUsername()));
            if (user != null) {
                throw new ClientException(ResponseStatus.USER_EXISTED_EXCEPTION);
            }
        }

        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setStatus(UserStatus.NORMAL.getStatus());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (userMapper.insert(user) == 0) {
            throw new ServerException(ResponseStatus.DATABASE_OPERATION_EXCEPTION);
        }

        bloomFilter.add(user.getUsername());
        return ResponseResult.success(user.getUserId());
    }

    @Override
    public ResponseResult<Object> delete(List<Long> userIds) {
        log.info("delete users: {}", userIds);

        List<User> users = userMapper.selectList(Wrappers.<User>lambdaQuery().in(User::getUserId, userIds));
        if (!users.isEmpty() && userMapper.deleteByIds(userIds) != users.size()) {
            throw new ServerException(ResponseStatus.DATABASE_OPERATION_EXCEPTION);
        }
        return ResponseResult.success();
    }

    @Override
    public ResponseResult<Object> update(UserDto userDto) {
        log.info("update user: {}", userDto);

        User user = userMapper.selectById(userDto.getUserId());
        if (user == null) {
            throw new ClientException(ResponseStatus.USER_NOT_EXISTED_EXCEPTION);
        }
        if (userDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        if (userDto.getStatus() != null) {
            user.setStatus(userDto.getStatus());
        }
        if (userMapper.updateById(user) == 0) {
            throw new ServerException(ResponseStatus.DATABASE_OPERATION_EXCEPTION);
        }
        return ResponseResult.success();
    }

    @Override
    public ResponseResult<List<User>> select(UserDto userDto) {
        log.info("select user: {}", userDto);

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (userDto.getUserId() != null) {
            wrapper.eq(User::getUserId, userDto.getUserId());
        }
        if (userDto.getUsername() != null) {
            wrapper.like(User::getUsername, userDto.getUsername());
        }
        wrapper.orderByDesc(User::getUpdateTime);

        List<User> users = userMapper.selectList(wrapper);
        for (User user : users) {
            user.setPassword(null);
        }
        return ResponseResult.success(users);
    }

    @Override
    public ResponseResult<Object> bindingRole(UserRoleDto userRoleDto) {
        log.info("binding role: {}", userRoleDto);

        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUserId, userRoleDto.getUserId()));
        if (user == null) {
            throw new ClientException(ResponseStatus.USER_NOT_EXISTED_EXCEPTION);
        }
        Role role = roleMapper.selectOne(Wrappers.<Role>lambdaQuery().eq(Role::getRoleId, userRoleDto.getRoleId()));
        if (role == null) {
            throw new ClientException(ResponseStatus.ROLE_NOT_EXISTED_EXCEPTION);
        }
        UserRole userRole = userRoleMapper.selectOne(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, userRoleDto.getUserId()).eq(UserRole::getRoleId, userRoleDto.getRoleId()));
        if (userRole != null) {
            throw new ClientException(ResponseStatus.USER_ROLE_EXISTED_EXCEPTION);
        }

        userRole = new UserRole();
        BeanUtils.copyProperties(userRoleDto, userRole);
        if (userRoleMapper.insert(userRole) == 0) {
            throw new ServerException(ResponseStatus.DATABASE_OPERATION_EXCEPTION);
        }
        return ResponseResult.success(userRole.getUserRoleId());
    }

    @Override
    public ResponseResult<UserLoginVo> login(UserDto userDto) {
        log.info("login user: {}", userDto);

        boolean contains = bloomFilter.contains(userDto.getUsername());
        if (!contains) {
            throw new ClientException(ResponseStatus.USER_NOT_EXISTED_EXCEPTION);
        }
        if (redisTemplate.opsForValue().get(SecurityConstant.SECURITY_BLACK_LIST + userDto.getUsername()) != null) {
            throw new ClientException(ResponseStatus.USER_FROZEN_EXCEPTION);
        }
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, userDto.getUsername()));
        if (user == null) {
            redisTemplate.opsForValue().set(SecurityConstant.SECURITY_BLACK_LIST + userDto.getUsername(), "", Duration.ofSeconds(30));
            throw new ClientException(ResponseStatus.USER_NOT_EXISTED_EXCEPTION);
        }
        if (passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new ClientException(ResponseStatus.WRONG_PASSWORD_EXCEPTION);
        }

        String token = jwtTool.createToken(user.getUserId());
        RedisUser redisUser = new RedisUser();
        redisUser.setUserId(user.getUserId());
        redisUser.setAuthorizes(userMapper.selectUserAuthorities(user.getUserId()));
        redisTemplate.opsForValue().set(SecurityConstant.SECURITY_TOKEN_PREFIX + user.getUserId(), JSON.toJSONString(redisUser), jwtProperties.getTtl());

        UserLoginVo userLoginVo = new UserLoginVo();
        userLoginVo.setUserId(user.getUserId());
        userLoginVo.setToken(token);
        return ResponseResult.success(userLoginVo);
    }

    private Set<String> requiredPermissions(String uri) {
        if (map.isEmpty()) {
            for (String patternAndPermission : permissionsProperties.getPatternAndPermissions()) {
                String pattern = patternAndPermission.split("=")[0];
                Set<String> permissions = new HashSet<>();
                if (!patternAndPermission.endsWith("=")) {
                    permissions = new HashSet<>(Arrays.asList(patternAndPermission.split("=")[1].split(",")));
                }
                map.put(pattern, permissions);
            }
        }

        for (String pattern : map.keySet()) {
            if (antPathMatcher.match(pattern, uri)) {
                return map.get(pattern);
            }
        }
        return new HashSet<>();
    }

    @Override
    public ResponseResult<UserVerifyVo> verify(UserVerifyDto userVerifyDto) {
        log.info("verify user: {}", userVerifyDto);

        UserVerifyVo userVerifyVo = new UserVerifyVo();
        Set<String> permissions = requiredPermissions(userVerifyDto.getUri());
        if (permissions.isEmpty()) {
            userVerifyVo.setVerify(true);
            return ResponseResult.success(userVerifyVo);
        }

        String token = userVerifyDto.getToken();
        if (token == null) {
            throw new ClientException(ResponseStatus.INVALID_TOKEN_EXCEPTION);
        }

        Long userId;
        try {
            userId = jwtTool.parseToken(token);
        } catch (Exception e) {
            throw new ClientException(ResponseStatus.INVALID_TOKEN_EXCEPTION);
        }
        RedisUser redisUser = JSON.parseObject(redisTemplate.opsForValue().get(SecurityConstant.SECURITY_TOKEN_PREFIX + userId), RedisUser.class);
        if (redisUser == null) {
            throw new ClientException(ResponseStatus.USER_NOT_EXISTED_EXCEPTION);
        }
        List<String> authorizes = redisUser.getAuthorizes();
        if (authorizes == null || authorizes.isEmpty()) {
            userVerifyVo.setVerify(false);
            return ResponseResult.success(userVerifyVo);
        }

        for (String permission : permissions) {
            if (authorizes.contains(permission)) {
                userVerifyVo.setUserId(userId);
                userVerifyVo.setVerify(true);
                return ResponseResult.success(userVerifyVo);
            }
        }
        userVerifyVo.setVerify(false);
        return ResponseResult.success(userVerifyVo);
    }

    @Override
    public ResponseResult<Object> logout() {
        log.info("logout user: {}", UserContext.getUserId());
        redisTemplate.delete(SecurityConstant.SECURITY_TOKEN_PREFIX + UserContext.getUserId());
        return ResponseResult.success();
    }
}
