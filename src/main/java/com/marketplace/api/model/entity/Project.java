package com.marketplace.api.model.entity;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * 
 * @author Narasim
 *
 */
@Entity
public class Project {

	private String projectName;

	private String description;

	private Double budjet;
	@Basic
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Los_Angeles")
	private Calendar closingDate;

	@JsonIgnore
	@ManyToOne
	private Account account;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(mappedBy = "project")
	private Set<Bid> bid = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getBudjet() {
		return budjet;
	}

	public void setBudjet(Double budjet) {
		this.budjet = budjet;
	}

	public Calendar getClosingDate() {
		return closingDate;
	}

	public void setClosingDate(Calendar closingDate) {
		this.closingDate = closingDate;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", id).add("projectName", projectName)
				.add("description", description).add("budjet", budjet).add("closingDate", closingDate).toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Project))
			return false;
		Project that = (Project) o;
		return Objects.equal(id, that.id) && Objects.equal(projectName, that.projectName)
				&& Objects.equal(closingDate, that.closingDate) && Objects.equal(budjet, that.budjet);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id, projectName, closingDate, budjet);
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Set<Bid> getBid() {
		return bid;
	}

	public void setBid(Bid bid) {
		this.bid.add(bid);
	}

}
