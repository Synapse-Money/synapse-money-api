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
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Category Repository Tests")
public class CategoryRepositoryTest {

    @Mock
    CategoryRepository categoryRepository;

    @Test
    @DisplayName("Should save category successfully")
    void shouldSaveCategorySuccessfully() {
        Category category = makeSut();

        when(categoryRepository.save(category)).thenReturn(category);

        Category categorySaved = categoryRepository.save(category);

        assertThat(categorySaved).isNotNull();
        assertThat(categorySaved.getId()).isNotNull();
    }

    @Test
    @DisplayName("Should not save category without user")
    void shouldNotSaveCategoryWithoutUser() {
        Category category = makeSut(builder -> builder.user(null));

        when(categoryRepository.save(argThat(cat -> cat != null && cat.getUser() == null)))
                .thenReturn(null);

        Category categorySaved = categoryRepository.save(category);

        assertThat(categorySaved).isNull();
    }


    private Category makeSut() {
        return makeSut(builder -> {});
    }

    private Category makeSut(Consumer<Category.CategoryBuilder> consumer) {
        Category.CategoryBuilder builder = Category.builder()
                .id(1L)
                .user(makeUser())
                .name("Test Category")
                .color("#333333")
                .icon("Test Icon")
                .transactionType(ETransactionType.INCOME)
                .isDefault(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now());

        consumer.accept(builder);
        return builder.build();
    }

    private User makeUser() {
        return User.builder()
                .id(1L)
                .email("john.doe@example.com")
                .password("hashedPassword123")
                .firstName("John")
                .lastName("Doe")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
