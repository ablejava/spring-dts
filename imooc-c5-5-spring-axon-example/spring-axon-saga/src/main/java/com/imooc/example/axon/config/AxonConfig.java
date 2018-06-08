package com.imooc.example.axon.config;

import com.imooc.example.axon.customer.Customer;
import com.imooc.example.axon.order.Order;
import com.imooc.example.axon.order.command.OrderCommandHandler;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.spring.config.AxonConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by mavlarn on 2018/6/1.
 */
@Configuration
public class AxonConfig {

    @Autowired
    AxonConfiguration axonConfiguration;

    @Bean
    public OrderCommandHandler orderCommandHandler() {
        Repository<Order> orderRepository = axonConfiguration.repository(Order.class);
        Repository<Customer> customerRepository = axonConfiguration.repository(Customer.class);
        return new OrderCommandHandler(orderRepository, customerRepository);
    }
}
