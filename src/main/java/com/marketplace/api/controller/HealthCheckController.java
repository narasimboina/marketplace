package com.marketplace.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Narasim
 */
// tag::controller[]
@RestController
@RequestMapping("/health")
@Slf4j
public class HealthCheckController {
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<String>  health() {
		log.info("health " );
		return new ResponseEntity<String>("Welcome. " , HttpStatus.OK);

	}

}
