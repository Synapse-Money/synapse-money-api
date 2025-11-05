package com.synapse.money.domain.entity;

import com.synapse.money.domain.enums.ETransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Category Domain Entity Tests")
public class CategoryTest {

    @Test
    @DisplayName("Should create category successfully")
    void shouldCreateCategorySuccessfully() {
        User user = User.builder()
                .id(1L)
                .email("john.doe@example.com")
                .password("hashedPassword123")
                .firstName("John")
                .lastName("Doe")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Category category = Category.builder()
                .id(1L)
                .user(user)
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
        assertThat(category.getUser()).isEqualTo(user);
        assertThat(category.getColor()).isEqualTo("#333333");
        assertThat(category.getIcon()).isEqualTo("Test Icon");
        assertThat(category.getTransactionType()).isEqualTo(ETransactionType.INCOME);
        assertThat(category.isDefault()).isEqualTo(true);
        assertThat(category.getCreatedAt()).isNotNull();
        assertThat(category.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should validate color format")
    void shouldValidateColorFormat() {
        assertThat(Category.isValidColor("#333333")).isTrue();
        assertThat(Category.isValidColor("#ff0000")).isTrue();
        assertThat(Category.isValidColor("#00ff00")).isTrue();

        assertThat(Category.isValidColor("red")).isFalse();
        assertThat(Category.isValidColor("green")).isFalse();
        assertThat(Category.isValidColor("blue")).isFalse();

        assertThat(Category.isValidColor(null)).isFalse();
        assertThat(Category.isValidColor("")).isFalse();
        assertThat(Category.isValidColor("#")).isFalse();
    }
}