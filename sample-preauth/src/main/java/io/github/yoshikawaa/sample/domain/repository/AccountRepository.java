package io.github.yoshikawaa.sample.domain.repository;

import io.github.yoshikawaa.sample.domain.model.Account;

public interface AccountRepository {
    Account findByName(String name);
}
