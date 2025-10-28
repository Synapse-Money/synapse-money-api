package com.synapse.money.domain.service;

import com.synapse.money.domain.entity.User;

public interface TokenGenerator {

    String generate(User user);
}