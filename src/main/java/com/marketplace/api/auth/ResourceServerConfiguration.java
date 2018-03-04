package com.marketplace.api.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId("marketplace").stateless(false);
	}
	
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
	       http
           .authorizeRequests()
               .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security",
                       "/swagger-ui.html", "/webjars/**", "/swagger-resources/configuration/ui", "/swagger-ui.html",
                       "/swagger-resources/configuration/security")
               .permitAll()
               .and().authorizeRequests().antMatchers("/projects").hasRole("USER")
               .and().authorizeRequests().antMatchers("/oauth/authorize").hasRole("USER")
               .and().authorizeRequests().antMatchers("/oauth/token").hasRole("USER")
               .antMatchers("/projects").access("#oauth2.hasScope('write')").anyRequest().authenticated()
               .and().formLogin().and()
       		.httpBasic();
	}

}
