package com.synapse.money.domain.repository;

import com.synapse.money.domain.entity.Category;
import com.synapse.money.domain.entity.User;
import com.synapse.money.domain.enums.ETransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Category Repository Tests")
public class CategoryRepositoryTest {

    CategoryRepository categoryRepository;

    @Test
    void shouldSaveCategorySuccessfully() {
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

        Category categorySaved = categoryRepository.save(category);

        assertThat(categorySaved).isNotNull();
        assertThat(categorySaved.getId()).isNotNull();
    }
}
