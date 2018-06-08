package com.imooc.example.axon.customer.event;

/**
 * Created by mavlarn on 2018/5/22.
 */
public class AccountCreatedEvent {

    private String accountId;
    private String name;

    public AccountCreatedEvent(String accountId, String name) {
        this.accountId = accountId;
        this.name = name;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
