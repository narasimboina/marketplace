
package com.marketplace.api;

import static com.google.common.base.Predicates.or;
import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.builders.PathSelectors.ant;
import static springfox.documentation.builders.PathSelectors.regex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

import com.google.common.base.Predicate;
import com.marketplace.api.model.entity.Account;
import com.marketplace.api.repository.AccountRepository;

import springfox.documentation.builders.ImplicitGrantBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.AuthorizationCodeGrant;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.LoginEndpoint;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.TokenEndpoint;
import springfox.documentation.service.TokenRequestEndpoint;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Narasim

 */
//tag::Application[]

@SpringBootApplication
@EnableSwagger2
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner init(AccountRepository accountRepository) {
		Arrays.asList("ram,john,dave,narasim".split(",")).forEach(a -> {
			Account account = accountRepository.save(new Account(a, "password", "Buyer"));

		});

		return (evt) -> Arrays.asList("sam,eric,testuser".split(",")).forEach(a -> {
			Account account = accountRepository.save(new Account(a, "password", "Seller"));

		});
	}

	@Bean
	FilterRegistrationBean corsFilter(@Value("${tagit.origin:http://localhost:9000}") String origin) {
		return new FilterRegistrationBean(new Filter() {
			public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
					throws IOException, ServletException {
				HttpServletRequest request = (HttpServletRequest) req;
				HttpServletResponse response = (HttpServletResponse) res;
				String method = request.getMethod();
				// this origin value can be configured by env
				response.setHeader("Access-Control-Allow-Origin", origin);
				response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
				response.setHeader("Access-Control-Max-Age", Long.toString(40 * 40));
				response.setHeader("Access-Control-Allow-Credentials", "true");
				response.setHeader("Access-Control-Allow-Headers",
						"Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization");
				if ("OPTIONS".equals(method)) {
					response.setStatus(HttpStatus.OK.value());
				} else {
					chain.doFilter(req, res);
				}
			}

			public void init(FilterConfig filterConfig) {
			}

			public void destroy() {
			}
		});
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				// .paths(PathSelectors.regex("/projects.*"))
				.paths(paths())
				// .paths(PathSelectors.ant("/projects/*"))
				// .paths(PathSelectors.regex("(?/oauth).+"))
				.build().securitySchemes(newArrayList(oauth())).securityContexts(newArrayList(securityContext()));
	}

	private Predicate<String> paths() {
		return or(regex("/projects.*"), regex("/oauth/token")

				);
	}

	@Bean
	SecurityContext securityContext() {
		AuthorizationScope readScope = new AuthorizationScope("write", "desc");
		AuthorizationScope[] scopes = new AuthorizationScope[1];
		scopes[0] = readScope;
		SecurityReference securityReference = SecurityReference.builder().reference("marketplace").scopes(scopes)
				.build();

		return SecurityContext.builder().securityReferences(newArrayList(securityReference))
				.forPaths(ant("/projects.*")).build();
	}

	@Bean
	SecurityScheme oauth() {
		return new OAuthBuilder().name("marketplace").grantTypes(grantTypes()).scopes(scopes()).build();
	}

	List<AuthorizationScope> scopes() {
		return newArrayList(new AuthorizationScope("write", ""));
	}

	 /*	List<GrantType> grantTypes() {
		GrantType grantType = new ImplicitGrantBuilder().loginEndpoint(new LoginEndpoint("/oauth/authorize")).tokenName("access_token").build();
		return newArrayList(grantType);
	}*/
	
   List<GrantType> grantTypes() {
        List<GrantType> grantTypes = new ArrayList<>();
        TokenRequestEndpoint tokenRequestEndpoint = new TokenRequestEndpoint( "/oauth/authorize", "prod-marketplace", "987654");
        TokenEndpoint tokenEndpoint = new TokenEndpoint("/oauth/token", "access_token");
        grantTypes.add(new AuthorizationCodeGrant(tokenRequestEndpoint, tokenEndpoint));
        return grantTypes;
    }
    
	@Bean
	public SecurityConfiguration securityInfo() {
		return new SecurityConfiguration("prod-marketplace", "987654", "marketplace", "prod-marketplace", "apiKey",
				ApiKeyVehicle.HEADER, "Authorization", " ");
	}
}
// end::Application[]
