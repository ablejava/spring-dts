/*
 * Copyright (c) 2016. Axon Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.imooc.example.order;

import com.imooc.example.order.command.OrderFailCommand;
import com.imooc.example.order.command.OrderFinishCommand;
import com.imooc.example.order.command.OrderPayCommand;
import com.imooc.example.order.event.OrderCreatedEvent;
import com.imooc.example.ticket.command.OrderTicketMoveCommand;
import com.imooc.example.ticket.command.OrderTicketPreserveCommand;
import com.imooc.example.ticket.command.OrderTicketUnlockCommand;
import com.imooc.example.ticket.event.OrderTicketMovedEvent;
import com.imooc.example.ticket.event.OrderTicketPreserveFailedEvent;
import com.imooc.example.ticket.event.OrderTicketPreservedEvent;
import com.imooc.example.user.event.OrderPaidEvent;
import com.imooc.example.user.event.OrderPayFailedEvent;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.callbacks.LoggingCallback;
import org.axonframework.eventhandling.saga.EndSaga;
import org.axonframework.eventhandling.saga.SagaEventHandler;
import org.axonframework.eventhandling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

@Saga
public class OrderManagementSaga {

    @Autowired
    private transient CommandBus commandBus;

    private String orderId;
    private String customerId;
    private String ticketId;
    private Double amount;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void on(OrderCreatedEvent event) {
        this.orderId = event.getOrderId();
        this.customerId = event.getCustomerId();
        this.ticketId = event.getTicketId();
        this.amount = event.getAmount();

        OrderTicketPreserveCommand command = new OrderTicketPreserveCommand(orderId, ticketId, customerId);
        commandBus.dispatch(asCommandMessage(command), LoggingCallback.INSTANCE);
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void on(OrderTicketPreservedEvent event) {
        OrderPayCommand command = new OrderPayCommand(orderId, customerId, amount);
        commandBus.dispatch(asCommandMessage(command), LoggingCallback.INSTANCE);
    }

    @SagaEventHandler(associationProperty = "orderId")
    @EndSaga
    public void on(OrderTicketPreserveFailedEvent event) {
        OrderFailCommand command = new OrderFailCommand(event.getOrderId(), "锁票失败");
        commandBus.dispatch(asCommandMessage(command), LoggingCallback.INSTANCE);
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void on(OrderPaidEvent event) {
        OrderTicketMoveCommand command = new OrderTicketMoveCommand(this.ticketId, this.orderId, this.customerId);
        commandBus.dispatch(asCommandMessage(command), LoggingCallback.INSTANCE);
    }

    @SagaEventHandler(associationProperty = "orderId")
    @EndSaga
    public void on(OrderPayFailedEvent event) {
        OrderTicketUnlockCommand command = new OrderTicketUnlockCommand(ticketId, customerId);
        commandBus.dispatch(asCommandMessage(command), LoggingCallback.INSTANCE);

        OrderFailCommand failCommand = new OrderFailCommand(event.getOrderId(), "扣费失败");
        commandBus.dispatch(asCommandMessage(failCommand), LoggingCallback.INSTANCE);
    }

    @SagaEventHandler(associationProperty = "orderId")
    @EndSaga
    public void on(OrderTicketMovedEvent event) {
        OrderFinishCommand command = new OrderFinishCommand(orderId);
        commandBus.dispatch(asCommandMessage(command), LoggingCallback.INSTANCE);
    }

}
