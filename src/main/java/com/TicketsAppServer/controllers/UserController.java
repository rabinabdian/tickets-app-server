package com.TicketsAppServer.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TicketsAppServer.ExceptionGenerator;
import com.TicketsAppServer.JwtServices;
import com.TicketsAppServer.beans.User;
import com.TicketsAppServer.services.UserService;

@RestController
@RequestMapping(path = "user")
public class UserController {
	@Autowired
	UserService userService;

	@Autowired
	JwtServices jwtServices;

	/**
	 * check if the user not exist then insert him to the db, and generate token
	 * to the user
	 * 
	 * @param user
	 * @return auth token
	 */
	@PostMapping(path = "register")
	public ResponseEntity<Map<String, String>> register(
			@RequestBody User user) {
		Map<String, String> response = userService.register(user);

		return new ResponseEntity<Map<String, String>>(response,
				response.containsKey("token") ? HttpStatus.OK
						: HttpStatus.CONFLICT);
	}

	/**
	 * verify the user's credentials and return generated token for the user
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping(path = "login")
	public ResponseEntity<Map<String, String>> login(
			@RequestBody Map<String, String> user) {
		Map<String, String> response = userService.login(user);

		return new ResponseEntity<Map<String, String>>(response,
				response.containsKey("token") ? HttpStatus.OK
						: HttpStatus.FORBIDDEN);
	}

	@GetMapping(path = "")
	public ResponseEntity<?> getUser(
			@RequestHeader(value = "Authorization", required = false) String authHeader) {
		try {
			Integer userId = jwtServices.jwtValidation(authHeader);

			return new ResponseEntity<Map<String, String>>(
					userService.getUser(userId), HttpStatus.OK);

		} catch (Exception e) {
			Map<String, String> error = ExceptionGenerator
					.exceptionGenerate(e.getMessage());

			return new ResponseEntity<Map<String, String>>(error,
					HttpStatus.FORBIDDEN);
		}
	}
}
