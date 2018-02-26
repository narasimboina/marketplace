package com.marketplace.api.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    @JsonIgnore
    private String password;
    
    private String userType;

    @OneToMany(mappedBy = "account")
    private Set<Project> projects = new HashSet<>();

    private Account() { } // JPA only

    public Account(final String username, final String password, final String userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Set<Project> getProjects() {
        return projects;
    }

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
    
}
