package com.example.activemq.nio;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.HashMap;

public class JmsProduct {

    /**
     * 使用前需要修改activemq.xml文件
     * <transportConnectors>
     *     <transportConnector name="nio" url="nio://0.0.0.0:61616"/>
     *     <transportConnectors/>
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("nio://localhost:61616");
        // 通过工厂获得连接并启动
        Connection connection = activeMQConnectionFactory.createConnection();


        // 第一次参数是事务，第二个是签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 创建topic
        Queue queue = session.createQueue("queue");
        // 创建product
        MessageProducer producer = session.createProducer(queue);
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
        session.close();
        connection.close();

    }
}
