
package com.marketplace.api.controller;

import java.net.URI;
import java.security.Principal;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.marketplace.api.exception.UserNotFoundException;
import com.marketplace.api.model.dto.ProjectBaseDTO;
import com.marketplace.api.model.dto.ProjectBidDTO;
import com.marketplace.api.model.entity.Account;
import com.marketplace.api.model.entity.Bid;
import com.marketplace.api.model.entity.Project;
import com.marketplace.api.repository.AccountRepository;
import com.marketplace.api.services.ProjectService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Narasim
 */
// tag::controller[]
@RestController
@RequestMapping("/projects")
@Slf4j
public class ProjectRestController {

	private final AccountRepository accountRepository;

	private final ProjectService projectService;

	@Autowired
	ProjectRestController(AccountRepository accountRepository, ProjectService projectService) {

		this.accountRepository = accountRepository;

		this.projectService = projectService;

	}

	/**
	 * 
	 * @param principal
	 * @return
	 */

	@RequestMapping(method = RequestMethod.GET)
	Collection<ProjectBaseDTO> getOpenProjects(Principal principal) {
		Account account = this.validateUser(principal);
		Collection<ProjectBaseDTO> list = this.projectService.getOpenProjects(account);
		log.info("getOpenProjects {}", list);
		return list;

	}

	/**
	 * 
	 * @param principal
	 * @param project
	 * @return
	 */

	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<?> createProject(Principal principal, @RequestBody Project project) {
		Account account = this.validateUser(principal);
		log.info(" project {}", project);
		return this.accountRepository.findByUsername(principal.getName()).map(user -> {
			Long id = projectService.createProject(account, project);

			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
			log.info("location: {}", location);
			return ResponseEntity.created(location).build();
		}).orElse(ResponseEntity.noContent().build());
	}

	/**
	 * 
	 * @param principal
	 * @param projectId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{projectId}")
	ProjectBidDTO getProject(Principal principal, @PathVariable Long projectId) {
		log.info(" projectId {}", projectId);
		// TODO: Check User Roles
		Account account = this.validateUser(principal);
		return this.projectService.getProject(account, projectId);

	}

	/**
	 * 
	 * @param principal
	 * @param projectId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{projectId}/bids")
	Project getBids(Principal principal, @PathVariable Long projectId) {
		Account account = this.validateUser(principal);
		return this.projectService.getBids(account, projectId);

	}

	/**
	 * 
	 * @param principal
	 * @param BidDTO
	 * @param projectId
	 * @return
	 */

	@RequestMapping(method = RequestMethod.POST, value = "/{projectId}/bids")
	ResponseEntity<?> createBid(Principal principal, @RequestBody Bid BidDto, @PathVariable Long projectId) {
		this.validateUser(principal);
		log.info(" projectId {}", projectId);
		return this.accountRepository.findByUsername(principal.getName()).map(user -> {
			Long id = projectService.createBid(BidDto, projectId, user);

			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
			log.info("location: {}", location);
			return ResponseEntity.created(location).build();
		}).orElse(ResponseEntity.noContent().build());
	}

	/**
	 * 
	 * @param principal
	 * @return
	 */

	private Account validateUser(Principal principal) {

		String userId = principal.getName();
		log.info("Logged in User: {}", userId);
		Account account = this.accountRepository.findByUsername(userId)
				.orElseThrow(() -> new UserNotFoundException(userId));
		return account;
	}

}
// end::controller[]
