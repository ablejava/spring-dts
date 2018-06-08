package com.imooc.example.order.command;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

/**
 * Created by mavlarn on 2018/5/24.
 */
public class OrderPayCommand {

    @TargetAggregateIdentifier
    private String orderId;
    private String customerId;
    private Double amount;

    public OrderPayCommand(String orderId, String customerId, Double amount) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public Double getAmount() {
        return amount;
    }
}