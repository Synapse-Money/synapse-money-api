package com.synapse.money.domain.enums;

public enum ETransactionType {
    EXPENSE("expense"),
    TRANSFER("transfer"),
    INCOME("income");

    private final String description;

    ETransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
