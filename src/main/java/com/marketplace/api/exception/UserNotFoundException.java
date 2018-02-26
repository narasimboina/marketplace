
package com.marketplace.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Narasim
 */
@ResponseStatus(HttpStatus.NOT_FOUND)

public class UserNotFoundException extends RuntimeException {

	/**
	 * @method:UserNotFoundException
	 */
	private static final long serialVersionUID = 1L;
	// tag::UserNotFoundException[]
	public UserNotFoundException(String userId) {
		super("user doesnt exists '" + userId + "'.");
	}
	// tag::UserNotFoundException[]
}

