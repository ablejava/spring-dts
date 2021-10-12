package com.example.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class JmsAckConsumer {
    public static void main(String[] args) throws JMSException, IOException {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp:localhost:61616");
        // 通过工厂获得连接并启动
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        // 第一次参数是事务，第二个是签收
        Session session = connection.createSession(false, Session.DUPS_OK_ACKNOWLEDGE);
        // 创建topic
        Queue queue = session.createQueue("queue");
        // 使用topic模式，需要先启动消费者在生产，如果先发送topic消息，消费者后启动不会消费之前发送的topic消息
        // session.createTopic("topic");

        MessageConsumer consumer = session.createConsumer(queue);

        //2。通过监听队列
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage) message;
                String text = null;
                try {
                    text = textMessage.getText();

                    // 确认消息，
                    textMessage.acknowledge();
                } catch (JMSException e) {

                }
                System.out.println(text);
            }
        });
        System.in.read();
        consumer.close();
        session.close();
        connection.close();
    }
}
