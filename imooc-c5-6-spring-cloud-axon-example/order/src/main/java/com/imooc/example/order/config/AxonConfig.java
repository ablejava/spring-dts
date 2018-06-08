package com.imooc.example.order.config;

import com.imooc.example.order.OrderManagementSaga;
import com.rabbitmq.client.Channel;
import org.axonframework.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.axonframework.config.SagaConfiguration;
import org.axonframework.serialization.Serializer;
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


/**
 * Created by mavlarn on 2018/6/4.
 */
@Configuration
public class AxonConfig {

    private static final Logger LOG = LoggerFactory.getLogger(AxonConfig.class);

    @Value("${axon.amqp.exchange}")
    private String exchangeName;

    @Bean
    public Queue orderQueue(){
        return new Queue("order",true);
    }

    @Bean
    public Exchange exchange(){
        return ExchangeBuilder.topicExchange(exchangeName).durable(true).build();
    }

    @Bean
    public Binding orderQueueBinding() {
        return BindingBuilder.bind(orderQueue()).to(exchange()).with("com.imooc.example.order.event.#").noargs();
    }

    /**
     * Order Saga需要监听事件来处理订单流程，它需要监听ticket、user队列来监听saga流程里面相应的事件。
     * 他反而不需要监听order队列，因为order的事件都是本地处理的。所以我们不需要设置OrderEventProcessor
     */
    @Bean
    public SpringAMQPMessageSource orderMessageSource(Serializer serializer) {
        return new SpringAMQPMessageSource(serializer){
            @RabbitListener(queues = {"order", "ticket", "user"})
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                LOG.debug("Message received: {}", message);
                super.onMessage(message, channel);
            }
        };
    }

//    @Autowired
//    public void configure(EventHandlingConfiguration ehConfig, SpringAMQPMessageSource orderMessageSource) {
//        ehConfig.registerSubscribingEventProcessor("OrderEventProcessor", c -> orderMessageSource);
//    }

    @Bean
    public SagaConfiguration<OrderManagementSaga> orderManagementSagaConfiguration(SpringAMQPMessageSource orderMessageSource,
                                                                                   PlatformTransactionManager txManager) {
        return SagaConfiguration.subscribingSagaManager(OrderManagementSaga.class, c -> orderMessageSource)
                                .configureTransactionManager(c -> new SpringTransactionManager(txManager));
    }

}
