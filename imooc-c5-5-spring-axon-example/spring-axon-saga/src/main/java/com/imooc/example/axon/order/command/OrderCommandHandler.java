package com.imooc.example.axon.order.command;

import com.imooc.example.axon.customer.Customer;
import com.imooc.example.axon.order.Order;
import com.imooc.example.axon.order.event.OrderPaidEvent;
import com.imooc.example.axon.order.event.OrderPayFailedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.Aggregate;
import org.axonframework.commandhandling.model.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

/**
 * Created by mavlarn on 2018/5/28.
 */
public class OrderCommandHandler {

    private static final Logger LOG = LoggerFactory.getLogger(OrderCommandHandler.class);

    private Repository<Order> orderRepository;
    private Repository<Customer> customerRepository;

    public OrderCommandHandler(Repository<Order> orderRepository, Repository<Customer> customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    @CommandHandler
    public void handlePay(OrderPayCommand command) {
//        Aggregate<Order> aggregateRoot = orderAggregateRepository.load(command.getOrderId());
//        aggregateRoot.execute(order -> {
//            if (!order.getStatus().equals("NEW")) {
//                LOG.error("Invalid command for order:{}", this);
//                return;
//            }
//            apply(new OrderPaidEvent());
//        });

        Aggregate<Customer> customerAggregate = customerRepository.load(command.getCustomerId());
        customerAggregate.execute(customer -> {
            if (customer.getDeposit() < command.getAmount()) {
                LOG.error("Not enough deposit");
                apply(new OrderPayFailedEvent(command.getOrderId()));
            } else {
                apply(new OrderPaidEvent(command.getOrderId(), command.getCustomerId(), command.getAmount()));
            }
        });
    }
}
