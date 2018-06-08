package com.imooc.example.axon.account;

import com.imooc.example.axon.customer.event.AccountCreatedEvent;
import com.imooc.example.axon.customer.event.AccountMoneyDepositedEvent;
import com.imooc.example.axon.customer.event.AccountMoneyWithdrawnEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by mavlarn on 2018/5/22.
 */
@Service
public class AccountProjector {

    private final AccountRepository repository;

    @Autowired
    public AccountProjector(AccountRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void on(AccountCreatedEvent event) {
        AccountEntity account = new AccountEntity(event.getAccountId(), event.getName());
        repository.save(account);
    }

    @EventHandler
    public void on(AccountMoneyDepositedEvent event) {
        String accountId = event.getAccountId();
        AccountEntity accountView = repository.getOne(accountId);

        double newDeposit = accountView.getDeposit() + event.getAmount();
        accountView.setDeposit(newDeposit);
        repository.save(accountView);
    }

    @EventHandler
    public void on(AccountMoneyWithdrawnEvent event) {
        String accountId = event.getAccountId();
        AccountEntity accountView = repository.getOne(accountId);

        double newDeposit = accountView.getDeposit() - event.getAmount();
        accountView.setDeposit(newDeposit);
        repository.save(accountView);
    }
}
