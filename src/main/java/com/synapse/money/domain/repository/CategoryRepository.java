package com.synapse.money.domain.repository;

import com.synapse.money.domain.entity.Category;

public interface CategoryRepository {

    Category save(Category category);
}