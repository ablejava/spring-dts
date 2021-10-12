package com.example.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class JmsSubConsumer {
    public static void main(String[] args) throws JMSException, IOException {

        /**
         * 1.先运行一个消费者，向mq注册，我关注了这个主题
         * 2.然后再运行生产者发送消息
         * 3。无论消费者是否在线都会收到，下次连接时会把收到的消息都接收下来
         */


        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        // 通过工厂获得连接并启动
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.setClientID("张三");
        // 第一次参数是事务，第二个是签收

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);

        // 使用topic模式，需要先启动消费者在生产，如果先发送topic消息，消费者后启动不会消费之前发送的topic消息
        Topic topic = session.createTopic("topic");
        TopicSubscriber topicSubscriber = session.createDurableSubscriber(topic, "remark.....");

        connection.start();

        Message message = topicSubscriber.receive();
        while (null != message) {
            TextMessage textMessage = (TextMessage)message;
            String text = textMessage.getText();
            System.out.println(text);
        }
        //如果使用开启事务，使用后关闭事务
        // session.commit();
        session.close();
        connection.close();
    }
}
