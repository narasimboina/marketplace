
package com.marketplace.api.services;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.marketplace.api.model.dto.ProjectBaseDTO;
import com.marketplace.api.model.dto.ProjectBidDTO;
import com.marketplace.api.model.entity.Account;
import com.marketplace.api.model.entity.Bid;
import com.marketplace.api.model.entity.Project;

/**
 * @author Narasim Bayanaboina
 */
// tag::ProjectService[]
@Service
public interface ProjectService {

	public Collection<ProjectBaseDTO> getOpenProjects(Account account);

	public Collection<Project> getProjects(Account account);

	public Long createProject(Account account, Project project);

	public ProjectBidDTO getProject(Account account, Long projectId);

	public Project getBids(Account account, Long projectId);

	public Long createBid(Bid bid, Long projectId, Account account);

}
// end::ProjectService[]
