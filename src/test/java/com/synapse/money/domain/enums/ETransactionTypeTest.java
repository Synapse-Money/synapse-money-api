package com.synapse.money.domain.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ETransactionType Enum Tests")
class ETransactionTypeTest {

    @Test
    @DisplayName("Should have EXPENSE, TRANSFER and values")
    void shouldHaveCorrectValues() {
        assertThat(ETransactionType.values()).contains(
                ETransactionType.EXPENSE,
                ETransactionType.TRANSFER,
                ETransactionType.INCOME
        );
    }

    @Test
    @DisplayName("Should have exactly 3 values")
    void shouldHaveExactlyThreeValues() {
        assertThat(ETransactionType.values()).hasSize(3);
    }

    @Test
    @DisplayName("Should return correct description for EXPENSE")
    void shouldReturnCorrectDescriptionForExpense() {
        assertThat(ETransactionType.EXPENSE.getDescription()).isEqualTo("expense");
    }

    @Test
    @DisplayName("Should return correct description for TRANSFER")
    void shouldReturnCorrectDescriptionForTransfer() {
        assertThat(ETransactionType.TRANSFER.getDescription()).isEqualTo("transfer");
    }

    @Test
    @DisplayName("Should return correct description for INCOME")
    void shouldReturnCorrectDescriptionForIncome() {
        assertThat(ETransactionType.INCOME.getDescription()).isEqualTo("income");
    }

    @Test
    @DisplayName("Should return description for all enum values")
    void shouldReturnDescriptionForAllValues() {
        for (ETransactionType type : ETransactionType.values()) {
            assertThat(type.getDescription()).isNotNull().isNotEmpty();
        }
    }
}