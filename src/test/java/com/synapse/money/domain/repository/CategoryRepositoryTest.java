package com.synapse.money.domain.repository;

import com.synapse.money.domain.entity.Category;
import com.synapse.money.domain.entity.User;
import com.synapse.money.domain.enums.ETransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Category Repository Tests")
public class CategoryRepositoryTest {

    @Mock
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

        when(categoryRepository.save(category)).thenReturn(category);

        Category categorySaved = categoryRepository.save(category);

        assertThat(categorySaved).isNotNull();
        assertThat(categorySaved.getId()).isNotNull();
    }
}
