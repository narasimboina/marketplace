package com.marketplace.api.model.dto;

import com.google.common.base.MoreObjects;
import com.marketplace.api.model.entity.Bid;

public class BidDTO extends Bid{

	private Long id;

	private String description;

	private Double quote;

	private Long buyerId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", id).add("buyerId", buyerId)
				.add("description", description).add("quote", quote).toString();
	}

	public Double getQuote() {
		return quote;
	}

	public void setQuote(Double quote) {
		this.quote = quote;
	}


	public Long getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}

}