
package com.marketplace.api.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marketplace.api.model.dto.ProjectBaseDTO;
import com.marketplace.api.model.dto.ProjectBidDTO;
import com.marketplace.api.model.dto.ProjectDTO;
import com.marketplace.api.model.entity.Account;
import com.marketplace.api.model.entity.Bid;
import com.marketplace.api.model.entity.Project;
import com.marketplace.api.repository.AccountRepository;
import com.marketplace.api.repository.BidRepository;
import com.marketplace.api.repository.ProjectRepository;
import com.marketplace.api.services.ProjectService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Narasim Bayanaboina
 */
// tag::ProjectServiceImpl[]
@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

	private final ProjectRepository projectRepository;

	private final BidRepository bidRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	public ProjectServiceImpl(ProjectRepository projectRepository, AccountRepository accountRepository,
			BidRepository bidRepository) {
		this.projectRepository = projectRepository;
		this.bidRepository = bidRepository;

	}

	public Collection<ProjectBaseDTO> getOpenProjects(Account account) {

		Collection<Project> list = this.projectRepository.findOpenProjects();
		log.info("OpenProjects list {}", list);
		Collection<ProjectBaseDTO> listDto = convertBaseEntity(list);
		return listDto;

	}

	public Collection<Project> getProjects(Account account) {

		Collection<Project> list = this.projectRepository.findByUsername(account.getId());
		return list;

	}

	@Transactional
	public Long createProject(Account account, Project project) {
		log.info("createProject() accountId {}", account.getId());
		project.setAccount(account);
		Project result = this.projectRepository.save(project);
		return result.getId();

	}

	public ProjectBidDTO getProject(Account account, Long projectId) {

		ProjectBidDTO projectDTO = projectRepository.findByMinBid(projectId);
		log.info("getProject()  {}", projectDTO);
		return projectDTO;
	}

	/**
	 * @param account
	 * @param projectId
	 * @return Project
	 */

	public Project getBids(Account account, Long projectId) {
		log.info("getBids() projectId {}", projectId);
		// TODO:check Role and return only if Authorized Project Owner or else Error
		Project result = this.projectRepository.findProjectByOwner(projectId, account.getId());
		return Optional.ofNullable(result).orElse(new Project());
	}

	@Transactional
	public Long createBid(Bid bid, Long projectId, Account account) {

		log.info("createBid() accountId {}", account.getId());
		bid.setBuyerId(account.getId());
		bid.setProject(this.projectRepository.findOne(projectId));
		Bid result = this.bidRepository.save(bid);

		log.info("createBid() BidID {}", result);
		return result.getId();
	}

	private Collection<ProjectBaseDTO> convertBaseEntity(Collection<Project> list) {
		Collection<ProjectBaseDTO> resultList = new ArrayList<ProjectBaseDTO>();
		list.forEach(project -> {
			ProjectBaseDTO projectDTO = modelMapper.map(project, ProjectBaseDTO.class);
			resultList.add(projectDTO);
		});
		return resultList;

	}

	@SuppressWarnings("unused")
	private ProjectDTO convertEntity(Project project) {

		ProjectDTO projectDTO = modelMapper.map(project, ProjectDTO.class);
		return projectDTO;

	}
}
// end::ProjectServiceImpl[]
