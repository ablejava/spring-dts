package com.example.activemq.brokerServer;

import org.apache.activemq.broker.BrokerService;

public class EmbedBroker {

    /**
     * broker作为本机的mq服务
     * 先启动brokerservice
     * 再启动消费者和生产者
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        // activemq也支持在vm中通信基于嵌入式的broker
        BrokerService brokerService = new BrokerService();
        brokerService.setUseJmx(true);
        brokerService.addConnector("tcp://127.0.0.1:61617");
        brokerService.start();
    }
}
