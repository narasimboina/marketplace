package com.marketplace.api.controller;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import com.marketplace.api.Application;
import com.marketplace.api.model.dto.BidDTO;
import com.marketplace.api.model.entity.Account;
import com.marketplace.api.model.entity.Project;
import com.marketplace.api.repository.AccountRepository;
import com.marketplace.api.repository.ProjectRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Narasim
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Application.class,
		ProjectRestControllerTest.ExtraConfigInfo.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProjectRestControllerTest {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private MockMvc mockMvc;

	private String userName = "testuser";

	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	private Account account;

	private List<Project> projectList = new ArrayList<>();

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {

		this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().orElse(null);

		assertNotNull("the JSON message converter should not be null", this.mappingJackson2HttpMessageConverter);
	}

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).addFilter(springSecurityFilterChain).build();
		// this.projectRepository.deleteAllInBatch();;
		// this.accountRepository.deleteAllInBatch();

		this.account = accountRepository.findByUsername(userName).get();

		Project project = new Project();
		project.setBudjet(8999.00);
		project.setDescription("test project");
		project.setAccount(this.account);
		project.setProjectName("Retro");
		Calendar calendar = new GregorianCalendar(2018, 6, 28);
		project.setClosingDate(calendar);

		Project project2 = new Project();
		project2.setBudjet(9992.00);
		project2.setDescription("test2 project");
		project2.setAccount(this.account);
		project2.setProjectName("Retro2");
		project2.setClosingDate(calendar);

		this.projectList.add(projectRepository.save(project));
		this.projectList.add(projectRepository.save(project2));

	}

	@Test
	public void getProjectById() throws Exception {

		BidDTO bid = new BidDTO();

		bid.setDescription("bid#1 for " + this.projectList.get(1).getId());

		bid.setQuote(3990.00);

		String projectJson = json(bid);
		ResultActions resp = this.mockMvc.perform(post("/projects/" + this.projectList.get(1).getId() + "/bids")
				.header("Authorization", "Bearer " + passwordGrant("narasim")).contentType(contentType)
				.content(projectJson)).andExpect(status().isCreated());
		log.info(" Response getProjectById {} ", resp.andReturn().getResponse());

		BidDTO bid2 = new BidDTO();

		bid2.setDescription("bid#2 for " + this.projectList.get(1).getId());

		bid2.setQuote(4960.00);
		projectJson = json(bid2);

		resp = this.mockMvc.perform(post("/projects/" + this.projectList.get(1).getId() + "/bids")
				.header("Authorization", "Bearer " + passwordGrant("eric")).contentType(contentType)
				.content(projectJson)).andExpect(status().isCreated());
		log.info(" Response  getProjectById {} ", resp.andReturn().getResponse().getStatus());

		ResultActions result = mockMvc
				.perform(get("/projects/" + this.projectList.get(1).getId()).header("Authorization",
						"Bearer " + passwordGrant(userName)))
				.andExpect(status().isOk()).andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.id", is(this.projectList.get(1).getId().intValue())));

		log.info(" Project#:{} Min Bid for {}  ", this.projectList.get(1).getId(),
				result.andReturn().getResponse().getContentAsString());

	}

	@Test
	public void getProjectList() throws Exception {
		ResultActions result = mockMvc
				.perform(get("/projects").header("Authorization", "Bearer " + passwordGrant(userName)))
				.andExpect(status().isOk()).andExpect(content().contentType(contentType));
		log.info(" Response {}", result.andReturn().getResponse().getContentAsString());
	}

	@Test
	public void createProject() throws Exception {
		Project project = new Project();
		project.setBudjet(9199.00);
		project.setDescription("Retro2project");
		project.setAccount(this.account);
		project.setProjectName("Retro2project");
		Calendar calendar = new GregorianCalendar(2018, 6, 28);
		project.setClosingDate(calendar);
		String projectJson = json(project);
		this.mockMvc.perform(post("/projects").header("Authorization", "Bearer " + passwordGrant("sam"))
				.contentType(contentType).content(projectJson)).andExpect(status().isCreated());
	}

	@Test
	public void createBid() throws Exception {

		// create project

		Account user = accountRepository.findByUsername("eric").get();

		Project project = new Project();
		project.setBudjet(9000.00);
		project.setDescription("Retro3project");
		project.setAccount(user);
		project.setProjectName("Retro3project");
		Calendar calendar = new GregorianCalendar(2018, 6, 28);
		project.setClosingDate(calendar);
		Project result = this.projectRepository.save(project);

		BidDTO bid = new BidDTO();

		bid.setDescription("bid with excellence");

		bid.setQuote(8990.00);

		String projectJson = json(bid);
		ResultActions resp = this.mockMvc.perform(post("/projects/" + result.getId() + "/bids")
				.header("Authorization", "Bearer " + passwordGrant("narasim")).contentType(contentType)
				.content(projectJson)).andExpect(status().isCreated());
		log.info(" Response {}", resp.andReturn().getResponse());
	}

	@Test
	public void getBidsForProject() throws Exception {
		Account user = accountRepository.findByUsername("eric").get();
		Project project = new Project();
		project.setBudjet(9500.00);
		project.setDescription("Retro4project");
		project.setAccount(user);
		project.setProjectName("Retro 4project");
		Calendar calendar = new GregorianCalendar(2018, 6, 28);
		project.setClosingDate(calendar);
		Project result = this.projectRepository.save(project);

		BidDTO bid = new BidDTO();

		bid.setDescription("bid with excellence");
		;
		bid.setQuote(9990.00);

		String projectJson = json(bid);
		ResultActions resp = this.mockMvc.perform(post("/projects/" + result.getId() + "/bids")
				.header("Authorization", "Bearer " + passwordGrant("narasim")).contentType(contentType)
				.content(projectJson)).andExpect(status().isCreated());
		log.info(" Response {}", resp.andReturn().getResponse());
		ResultActions bidResp = mockMvc
				.perform(get("/projects/" + project.getId() + "/bids").header("Authorization",
						"Bearer " + passwordGrant("eric")))
				.andExpect(status().isOk()).andExpect(content().contentType(contentType));
		log.info(" Bid Response {}", bidResp.andReturn().getResponse().getContentAsString());
	}

	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		log.info(" PayLoad {}", mockHttpOutputMessage.getBodyAsString());
		return mockHttpOutputMessage.getBodyAsString();
	}

	@Autowired
	TestRestTemplate testRestTemplate;

	public String passwordGrant(String username) {
		MultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
		request.set("username", username);
		request.set("password", "password");
		request.set("grant_type", "password");
		@SuppressWarnings("unchecked")
		Map<String, Object> token = testRestTemplate.postForObject("/oauth/token", request, Map.class);
		Object tokenval = Optional.ofNullable(token.get("access_token")).orElse("");
		log.info(" =====TOKEN==== {}", tokenval.toString());
		assertNotNull("response may be incorrect: " + token, tokenval);
		return tokenval.toString();
	}

	@Test
	public void passwordAuthGrant() {
		MultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
		request.set("username", "testuser");
		request.set("password", "password");
		request.set("grant_type", "password");
		@SuppressWarnings("unchecked")
		Map<String, Object> token = testRestTemplate.postForObject("/oauth/token", request, Map.class);
		assertNotNull("response may be incorrect: " + token, token.get("access_token"));
	}

	@TestConfiguration
	public static class ExtraConfigInfo {

		@Bean
		RestTemplateBuilder restTemplateBuilder() {
			return new RestTemplateBuilder().basicAuthorization("prod-marketplace", "987654");
		}
	}

}
