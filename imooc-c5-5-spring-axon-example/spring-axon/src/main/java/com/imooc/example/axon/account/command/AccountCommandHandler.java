package com.imooc.example.axon.account.command;

import com.imooc.example.axon.account.Account;
import com.imooc.example.axon.customer.event.AccountMoneyWithdrawnEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.Aggregate;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.eventhandling.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static org.axonframework.eventhandling.GenericEventMessage.asEventMessage;

/**
 * Created by mavlarn on 2018/5/28.
 */
@Component
public class AccountCommandHandler {

    private static final Logger LOG = LoggerFactory.getLogger(AccountCommandHandler.class);

    private Repository<Account> accountRepository;
    private EventBus eventBus;

    public AccountCommandHandler(Repository<Account> accountRepository, EventBus eventBus) {
        this.accountRepository = accountRepository;
        this.eventBus = eventBus;
    }

    @CommandHandler
    public void handlePay(AccountWithdrawCommand command) {
        Aggregate<Account> accountAggregate = accountRepository.load(command.getAccountId());
        accountAggregate.execute(account -> {
            if (account.getBalance() >= command.getAmount()) {
                eventBus.publish(asEventMessage(new AccountMoneyWithdrawnEvent(command.getAccountId(), command.getAmount())));
            } else {
                throw new IllegalArgumentException("余额不足");
            }
        });
    }
}
