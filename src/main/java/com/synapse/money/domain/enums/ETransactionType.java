package com.synapse.money.domain.enums;

public enum ETransactionType {
    EXPENSE("Expense"),
    INCOME("Income");

    private String description;

    ETransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
