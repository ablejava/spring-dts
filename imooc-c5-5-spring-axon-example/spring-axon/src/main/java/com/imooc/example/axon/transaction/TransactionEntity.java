package com.imooc.example.axon.transaction;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by mavlarn on 2018/5/22.
 */
@Entity
public class TransactionEntity {

    @Id
    private String id;
    private String fromAccountId;
    private String toAccountId;

    private String amount;
    private Status status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    private enum Status {
        STARTED,
        FAILED,
        COMPLETED
    }
}
