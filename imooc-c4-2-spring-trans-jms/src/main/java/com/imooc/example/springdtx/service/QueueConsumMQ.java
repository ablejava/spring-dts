package com.imooc.example.springdtx.service;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.TextMessage;

@Component
public class QueueConsumMQ {


    @JmsListener(destination = "${myQueue}")
    public void receiver(TextMessage textMessage) {
        System.out.println(textMessage +"*********");
    }
}
