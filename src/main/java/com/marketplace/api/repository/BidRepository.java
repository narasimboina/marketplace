package com.marketplace.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketplace.api.model.entity.Bid;

public interface BidRepository extends JpaRepository<Bid, Long> {

}