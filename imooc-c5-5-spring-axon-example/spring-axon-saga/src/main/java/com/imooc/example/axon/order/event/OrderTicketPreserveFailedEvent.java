package com.imooc.example.axon.order.event;

/**
 * Created by mavlarn on 2018/5/26.
 */
public class OrderTicketPreserveFailedEvent {

    private String orderId;

    public OrderTicketPreserveFailedEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}
