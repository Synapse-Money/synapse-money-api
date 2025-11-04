package com.synapse.money.domain.entity;

import com.synapse.money.domain.enums.ETransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private Long id;
    private String name;
    private String color;
    private String icon;
    private ETransactionType transactionType;
    private boolean isDefault;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static boolean isValidColor(String color) {
        return color.length() == 7;
    }
}
