package com.forum.video.service.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author genghaoran
 */
@Component
@Slf4j
public class RabbitMqSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send(String context){
        log.info("send context:{}", context);
        this.amqpTemplate.convertAndSend("upload.video", context);
    }


}
