package com.marketplace.api.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.marketplace.api.model.dto.ProjectBidDTO;
import com.marketplace.api.model.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

	@Query("SELECT t FROM Project t where t.account.id = :accountId")
	Collection<Project> findByUsername(@Param("accountId") Long accountId);

	@Query("SELECT t FROM Project t where t.closingDate > CURRENT_DATE")
	Collection<Project> findOpenProjects();

	@Query("SELECT t FROM Project t where t.account.id = :accountId and t.id =:id")
	Project findProjectByOwner(@Param("id") Long projectId, @Param("accountId") Long accountId);

	@Query("select min(b.quote) as bid , p.id as id,p.description as description,p.budjet as budjet,p.projectName as projectName from Project p, Bid b where p.id=b.project.id and p.id=:id group by p.id")
	ProjectBidDTO findByMinBid(@Param("id") Long id);

}