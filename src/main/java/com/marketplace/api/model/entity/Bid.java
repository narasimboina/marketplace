package com.marketplace.api.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * 
 * @author Narasim
 *
 */
@Entity
public class Bid {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	private String description;

	private Double quote;

	private Long buyerId;

	@JsonIgnore
	@ManyToOne
	private Project project;

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
		return MoreObjects.toStringHelper(this).add("id", id)
				// .add("projectId", projectId)
				.add("buyerId", buyerId).add("description", description).add("quote", quote).toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Bid))
			return false;
		Bid that = (Bid) o;
		return Objects.equal(id, that.id) && Objects.equal(buyerId, that.buyerId) && Objects.equal(quote, that.quote);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id, project.getId(), quote);
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

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
