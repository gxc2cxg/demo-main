package com.xuchen.demo.work.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuchen.demo.common.util.UserContext;
import com.xuchen.demo.model.common.enums.ResponseStatus;
import com.xuchen.demo.model.common.exception.ClientException;
import com.xuchen.demo.model.common.exception.ServerException;
import com.xuchen.demo.model.common.vo.ResponseResult;
import com.xuchen.demo.model.work.constant.WorkConstant;
import com.xuchen.demo.model.work.dto.WorkDto;
import com.xuchen.demo.model.work.enums.WorkStatus;
import com.xuchen.demo.model.work.pojo.Work;
import com.xuchen.demo.model.work.pojo.WorkLog;
import com.xuchen.demo.work.mapper.WorkLogMapper;
import com.xuchen.demo.work.mapper.WorkMapper;
import com.xuchen.demo.work.service.WorkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class WorkServiceImpl extends ServiceImpl<WorkMapper, Work> implements WorkService {

    private final WorkMapper workMapper;

    private final WorkLogMapper workLogMapper;

    private final RabbitTemplate rabbitTemplate;

    private final RedissonClient redissonClient;

    private boolean addWorkToDatabase(Work work) {
        log.info("addWorkToDatabase: {}", work);

        try {
            WorkLog workLog = new WorkLog();
            BeanUtils.copyProperties(work, workLog);

            if (work.getWorkId() == null) {
                workMapper.insert(work);
                workLog.setWorkId(work.getWorkId());
                workLogMapper.insert(workLog);
            } else {
                if (Objects.equals(work.getStatus(), WorkStatus.SUCCESS.getStatus()) || Objects.equals(work.getStatus(), WorkStatus.FAILURE.getStatus())) {
                    workMapper.deleteById(work.getWorkId());
                } else {
                    workMapper.updateById(work);
                }
                workLogMapper.insert(workLog);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean addWorkToRabbitMQ(Work work) {
        log.info("addWorkToRabbitMQ: {}", work);

        try {
            rabbitTemplate.convertAndSend(work.getExchange(), work.getBindingKey(), JSON.toJSONString(work), message -> {
                WorkDto workDto = new WorkDto();
                BeanUtils.copyProperties(work, workDto);
                workDto.setStatus(WorkStatus.ENQUEUE.getStatus());
                update(workDto);

                message.getMessageProperties().setDelay((int) Math.max(0, work.getExecuteTime().getTime() - System.currentTimeMillis()));
                return message;
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public ResponseResult<Long> insert(WorkDto workDto) {
        log.info("insert work: {}", workDto);

        Work work = new Work();
        BeanUtils.copyProperties(workDto, work);
        work.setStatus(WorkStatus.NORMAL.getStatus());
        if (work.getExchange() == null) {
            work.setExchange(WorkConstant.DEFAULT_EXCHANGE);
            work.setBindingKey(WorkConstant.DEFAULT_KEY);
        }
        if (work.getPriority() == null) {
            work.setPriority(0);
        }

        if (!addWorkToDatabase(work)) {
            throw new ServerException(ResponseStatus.DATABASE_OPERATION_EXCEPTION);
        }
        if (work.getExecuteTime().getTime() <= System.currentTimeMillis() + WorkConstant.PREPARE_TIME) {
            if (!addWorkToRabbitMQ(work)) {
                throw new ServerException(ResponseStatus.RABBIT_OPERATION_EXCEPTION);
            }
        }
        return ResponseResult.success(work.getWorkId());
    }

    @Override
    public ResponseResult<Object> delete(WorkDto workDto) {
        log.info("delete work: {}", workDto);

        Work work = workMapper.selectById(workDto.getWorkId());
        if (work == null) {
            return ResponseResult.success();
        }
        if (!work.getCreateUser().equals(UserContext.getUserId())) {
            throw new ClientException(ResponseStatus.PERMISSION_DENIED_EXCEPTION);
        }

        workDto = new WorkDto();
        BeanUtils.copyProperties(work, workDto);
        workDto.setStatus(WorkStatus.CANCEL.getStatus());
        update(workDto);
        return ResponseResult.success();
    }

    @Override
    public ResponseResult<Object> update(WorkDto workDto) {
        log.info("update work: {}", workDto);

        Work work = workMapper.selectById(workDto.getWorkId());
        if (!Objects.equals(work.getCreateUser(), UserContext.getUserId())) {
            throw new ClientException(ResponseStatus.PERMISSION_DENIED_EXCEPTION);
        }

        work.setStatus(workDto.getStatus());
        work.setMessage(workDto.getMessage());
        if (!addWorkToDatabase(work)) {
            throw new ServerException(ResponseStatus.DATABASE_OPERATION_EXCEPTION);
        }
        return ResponseResult.success();
    }

    @Scheduled(cron = "0 */5 * * * ?")
    public void refreshWork() {
        RLock lock = redissonClient.getLock(WorkConstant.REFRESH_LOCK);
        if (lock.tryLock()) {
            try {
                List<Work> works = workMapper.selectList(
                        Wrappers.<Work>lambdaQuery()
                                .eq(Work::getStatus, 1)
                                .lt(Work::getExecuteTime, new Date(System.currentTimeMillis() + WorkConstant.PREPARE_TIME))
                                .orderByAsc(Work::getExecuteTime)
                                .orderByDesc(Work::getPriority));
                log.info("refresh work: {}", works.size());
                for (Work work : works) {
                    if (!addWorkToRabbitMQ(work)) {
                        log.info("refresh work failed: {}", work);
                    }
                }
            } finally {
                lock.unlock();
            }
        }
    }
}
