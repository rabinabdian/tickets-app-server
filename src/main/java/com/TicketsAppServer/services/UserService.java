package com.TicketsAppServer.services;

import java.util.Map;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.TicketsAppServer.ExceptionGenerator;
import com.TicketsAppServer.JwtServices;
import com.TicketsAppServer.beans.User;
import com.TicketsAppServer.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;

	@Autowired
	JwtServices jwtServices;

	/**
	 * check if the email exist if not generate new user and insert his data
	 * into db then generates token
	 * 
	 * @param user - user's data that coming from client
	 * @return token
	 */
	public Map<String, String> register(User user) {
		try {
			if (user.getEmail() == null || user.getFirstName() == null
					|| user.getFirstName() == null
					|| user.getPassword() == null)
				throw new Exception("bad request");

			User userResponse = userRepository.findByEmail(user.getEmail());
			if (userResponse != null)
				throw new Exception("email already exist");

			String password = user.getPassword();
			String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
			user.setPassword(hashedPassword);
			userResponse = userRepository.save(user);

			Map<String, String> response = jwtServices.generateJWTToken(user);
			response.put("id", String.valueOf(userResponse.getId()));
			response.put("email", userResponse.getEmail());
			response.put("firstName", userResponse.getFirstName());
			response.put("lastName", userResponse.getLastName());

			return response;
		} catch (Exception e) {
			return ExceptionGenerator.exceptionGenerate(e.getMessage());
		}
	}

	/**
	 * login the given user, search for the user's email and then validate the
	 * password, if passed then generates token
	 * 
	 * @param user - user's email and password that coming from client
	 * @return token
	 */
	public Map<String, String> login(Map<String, String> user) {
		try {
			User userResponse = userRepository.findByEmail(user.get("email"));
			if (userResponse == null)
				throw new Exception("invalid email or password");

			String password = user.get("password");
			String hashedPassword = userResponse.getPassword();
			if (!BCrypt.checkpw(password, hashedPassword))
				throw new Exception("invalid email or password");

			Map<String, String> response = jwtServices
					.generateJWTToken(userResponse);
			response.put("id", String.valueOf(userResponse.getId()));
			response.put("email", userResponse.getEmail());
			response.put("firstName", userResponse.getFirstName());
			response.put("lastName", userResponse.getLastName());

			return response;
		} catch (Exception e) {
			return ExceptionGenerator.exceptionGenerate(e.getMessage());
		}
	}

	public Map<String, String> getUser(int userId) {
		try {
			ObjectMapper oMapper = new ObjectMapper();

			Optional<User> userResponse = userRepository.findById(userId);
			if (userResponse.isEmpty())
				throw new Exception("unauthorized");
			User user = userResponse.get();

			@SuppressWarnings("unchecked")
			Map<String, String> userMap = oMapper.convertValue(user, Map.class);
			userMap.remove("password");
			userMap.remove("tickets");

			return userMap;
		} catch (Exception e) {
			return ExceptionGenerator.exceptionGenerate(e.getMessage());
		}
	}

}
