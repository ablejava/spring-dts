package com.example.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.HashMap;

public class JmsSubTransactionalProduct {

    public static void main(String[] args) throws Exception{
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
        // 通过工厂获得连接并启动
        Connection connection = activeMQConnectionFactory.createConnection();


        // 第一次参数是事务，第二个是签收
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        // 创建topic
        Topic topic = session.createTopic("topic");
        // 创建product
        MessageProducer producer = session.createProducer(topic);
        // 设置持久化
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);

        connection.start();
        // 创建文本消息
        TextMessage textMessage = session.createTextMessage("message");
        // 创建mapMessage
        MapMessage mapMessage = session.createMapMessage();
        mapMessage.setString("key", "value");
        //创建已实现序列化的ObjectMessage
        session.createObjectMessage(new HashMap<String, String>());
        // 创建流消息
        session.createStreamMessage();
        // 创建字节数组消息
        session.createBytesMessage();
        // 发送消息
        producer.send(textMessage);

        // 关闭连接
        producer.close();

        // 提交事务
        session.commit();



        session.close();
        connection.close();

    }
}
