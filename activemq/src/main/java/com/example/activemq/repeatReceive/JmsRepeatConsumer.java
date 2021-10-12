package com.example.activemq.repeatReceive;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;

import javax.jms.*;
import java.io.IOException;


/**
 * 重复消费
 *
 */
public class JmsRepeatConsumer {
    public static void main(String[] args) throws JMSException, IOException {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setMaximumRedeliveries(3); // 设置最大重复投递3次
        activeMQConnectionFactory.setRedeliveryPolicy(redeliveryPolicy);



        // 通过工厂获得连接并启动
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        // 第一次参数是事务，第二个是签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 创建topic
        Queue queue = session.createQueue("queue");
        // 使用topic模式，需要先启动消费者在生产，如果先发送topic消息，消费者后启动不会消费之前发送的topic消息
        // session.createTopic("topic");

        MessageConsumer consumer = session.createConsumer(queue);
        //1.通过阻塞队列
//        while (true) {
//
//            TextMessage receive = (TextMessage) consumer.receive();
//
//            if (receive != null) {
//
//                String text = receive.getText();
//                System.out.println(text);
//            } else {
//                break;
//            }
//        }
//
//
//        consumer.close();
//        session.close();
//        connection.close();

        //2。通过监听队列
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage) message;
                String text = null;
                try {
                    text = textMessage.getText();
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
