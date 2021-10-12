package com.example.activemq.asyncMQ;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageProducer;
import org.apache.activemq.AsyncCallback;

import javax.jms.*;
import java.util.HashMap;
import java.util.UUID;

public class JmsAsyncProduct {

    public static void main(String[] args) throws Exception{
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        /**
         * 开启异步投递
         */
        activeMQConnectionFactory.setUseAsyncSend(true);

        // 通过工厂获得连接并启动
        Connection connection = activeMQConnectionFactory.createConnection();


        // 第一次参数是事务，第二个是签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 创建topic
        Queue queue = session.createQueue("queue");
        // 创建product
        ActiveMQMessageProducer producer = (ActiveMQMessageProducer) session.createProducer(queue);
        // 设置持久化
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);


        connection.start();
        // 创建文本消息
        TextMessage textMessage = session.createTextMessage("message");

        textMessage.setJMSMessageID(UUID.randomUUID().toString());

        String jmsMessageID = textMessage.getJMSMessageID();


        // 发送消息
        producer.send(textMessage, new AsyncCallback() {
            @Override
            public void onSuccess() {
                System.out.println(jmsMessageID+" success ");
            }

            @Override
            public void onException(JMSException e) {

                System.out.println(jmsMessageID +" fail ");
            }
        });

        // 关闭连接
        producer.close();
        session.close();
        connection.close();

    }
}
