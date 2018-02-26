package com.marketplace.api.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.api.model.entity.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);
}
