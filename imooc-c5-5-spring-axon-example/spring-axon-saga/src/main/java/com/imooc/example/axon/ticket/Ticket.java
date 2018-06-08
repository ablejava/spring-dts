package com.imooc.example.axon.ticket;

import com.imooc.example.axon.order.event.OrderTicketMovedEvent;
import com.imooc.example.axon.order.event.OrderTicketPreserveFailedEvent;
import com.imooc.example.axon.order.event.OrderTicketPreservedEvent;
import com.imooc.example.axon.order.event.OrderTicketUnlockedEvent;
import com.imooc.example.axon.ticket.command.OrderTicketMoveCommand;
import com.imooc.example.axon.ticket.command.OrderTicketPreserveCommand;
import com.imooc.example.axon.ticket.command.OrderTicketUnlockCommand;
import com.imooc.example.axon.ticket.command.TicketCreateCommand;
import com.imooc.example.axon.ticket.event.TicketCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.Id;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

/**
 * 使用JPA的entity作为aggregate，文档中没有说明要加@Aggregate，实际上不加的话会出错。
 */
@Aggregate
@Entity(name = "tb_ticket")
public class Ticket {

    private static final Logger LOG = LoggerFactory.getLogger(Ticket.class);

    @Id
    private String id;

    private String name;

    private String lockUser;

    private String owner;

    public Ticket() { // JPA需要用不带参数的构造函数
    }

    @CommandHandler
    public Ticket(TicketCreateCommand command) {
        apply(new TicketCreatedEvent(command.getTicketId(), command.getName()));
    }

    @CommandHandler
    public void handle(OrderTicketPreserveCommand command) {
        if (this.lockUser == null) {
            apply(new OrderTicketPreservedEvent(command.getOrderId(), command.getCustomerId(), command.getTicketId()));
        } else if (this.lockUser.equals(command.getCustomerId())) {
            LOG.info("duplicated command");
        } else {
            apply(new OrderTicketPreserveFailedEvent(command.getOrderId()));
        }
    }

    @CommandHandler
    public void handle(OrderTicketUnlockCommand command) {
        if (this.lockUser == null) {
            LOG.error("Invalid command, ticket not locked");
        } else if (!this.lockUser.equals(command.getCustomerId())) {
            LOG.error("Invalid command, ticket not locked by:{}", command.getCustomerId());
        } else {
            apply(new OrderTicketUnlockedEvent());
        }
    }

    @CommandHandler
    public void handle(OrderTicketMoveCommand command) {
        if (this.lockUser == null) {
            LOG.error("Invalid command, ticket not locked");
        } else if (!this.lockUser.equals(command.getCustomerId())) {
            LOG.error("Invalid command, ticket not locked by:{}", command.getCustomerId());
        } else {
            apply(new OrderTicketMovedEvent(command.getOrderId(), command.getTicketId(), command.getCustomerId()));
        }
    }

    @EventSourcingHandler
    public void onCreate(TicketCreatedEvent event) {
        this.id = event.getTicketId();
        this.name = event.getName();
        LOG.info("Executed event:{}", event);
    }

    @EventSourcingHandler
    public void onPreserve(OrderTicketPreservedEvent event) {
        this.lockUser = event.getCustomerId();
        LOG.info("Executed event:{}", event);
    }

    @EventSourcingHandler
    public void onUnlock(OrderTicketUnlockedEvent event) {
        this.lockUser = null;
        LOG.info("Executed event:{}", event);
    }

    @EventSourcingHandler
    public void onMove(OrderTicketMovedEvent event) {
        this.lockUser = null;
        this.owner = event.getCustomerId();
        LOG.info("Executed event:{}", event);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLockUser() {
        return lockUser;
    }

    public String getOwner() {
        return owner;
    }
}
