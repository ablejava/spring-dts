package com.imooc.example.springdtx.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Queue;
import java.util.UUID;

@RestController
@RequestMapping("/api/queue")
public class QueueProductMQ {


    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private Queue queue;


    public void productQueue() {
        jmsMessagingTemplate.convertAndSend(queue, "********"+ UUID.randomUUID());
    }
}
