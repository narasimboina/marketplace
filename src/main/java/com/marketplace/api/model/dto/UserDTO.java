package com.marketplace.api.model.dto;

import org.springframework.data.rest.core.config.Projection;

import com.marketplace.api.model.entity.Account;

@Projection(name = "userDto", types = Account.class)
public interface UserDTO {

    Long getId();

    String getName();
}