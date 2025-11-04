package com.synapse.money.domain.entity;

import com.synapse.money.domain.enums.ETransactionType;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Category Domain Entity Tests")
public class CategoryTest {

    @Test
    @DisplayName("Should create category successfully")
    public void shouldCreateCategorySuccessfully() {
        Category category = Category.builder()
                .id(1L)
                .name("Test Category")
                .color("#333333")
                .icon("Test Icon")
                .transactionType(ETransactionType.INCOME)
                .isDefault(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        assertThat(category).isNotNull();
        assertThat(category.getId()).isEqualTo(1L);
        assertThat(category.getColor()).isEqualTo("#333333");
        assertThat(category.getIcon()).isEqualTo("Test Icon");
        assertThat(category.getTransactionType()).isEqualTo(ETransactionType.INCOME);
        assertThat(category.isDefault()).isEqualTo(true);
        assertThat(category.getCreatedAt()).isNotNull();
        assertThat(category.getUpdatedAt()).isNotNull();
    }
}
