package com.xuchen.demo.work.listener;

import com.alibaba.fastjson.JSON;
import com.xuchen.demo.model.work.constant.WorkConstant;
import com.xuchen.demo.model.work.pojo.Work;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ErrorListener {

    @RabbitListener(queues = {WorkConstant.ERROR_QUEUE})
    public void errorListener(String message) {
        Work work = JSON.parseObject(message, Work.class);
        log.info("work executing failed: {}", work);
    }
}
