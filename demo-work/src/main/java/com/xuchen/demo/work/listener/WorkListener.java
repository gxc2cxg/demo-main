package com.xuchen.demo.work.listener;

import com.alibaba.fastjson.JSON;
import com.xuchen.demo.model.work.constant.WorkConstant;
import com.xuchen.demo.model.work.dto.WorkDto;
import com.xuchen.demo.model.work.enums.WorkStatus;
import com.xuchen.demo.model.work.pojo.Work;
import com.xuchen.demo.work.controller.WorkController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@SuppressWarnings("ALL")
@Slf4j
@Component
@RequiredArgsConstructor
public class WorkListener {

    private final WorkController workController;

    @RabbitListener(queues = {WorkConstant.DEFAULT_QUEUE})
    public void sampleListener(String message) throws InterruptedException {
        Work work = JSON.parseObject(message, Work.class);
        log.info("consumer receive: {}", work);

        WorkDto workDto = new WorkDto();
        BeanUtils.copyProperties(work, workDto);
        workDto.setStatus(WorkStatus.EXECUTING.getStatus());
        workController.update(workDto);

        Thread.sleep(60 * 1000);

        workDto.setStatus(WorkStatus.SUCCESS.getStatus());
        workController.update(workDto);
    }
}
