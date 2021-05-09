package com.TicketsAppServer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.springframework.stereotype.Component;

import com.TicketsAppServer.beans.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtServices {
	// secret key
	private static String key = "-tickets-app-api-super-secret-key-";
	private static String secretKey = DatatypeConverter
			.printBase64Binary(key.getBytes());

//	private static final Key API_SECRET_KEY = Keys
//			.secretKeyFor(SignatureAlgorithm.HS256);

//	private static final long TOKEN_VALIDITY = 1000 * 60 * 60 * 2; // 2 hours

	/**
	 * generates jwt token using secrect-key, and user's data
	 * 
	 * @param user - user's data that coming from db or from register
	 * @return token
	 */
	@SuppressWarnings("deprecation")
	public Map<String, String> generateJWTToken(User user) {
		long timestamp = System.currentTimeMillis();
		String token = Jwts.builder()
				.signWith(SignatureAlgorithm.HS256, secretKey)
				.setIssuedAt(new Date(timestamp))
//				.setExpiration(new Date(timestamp + TOKEN_VALIDITY))
				.claim("userId", user.getId())
				.claim("firstName", user.getFirstName())
				.claim("lastName", user.getLastName())
				.claim("email", user.getEmail()).compact();
		Map<String, String> map = new HashMap<>();
		map.put("token", token);

		return map;
	}

	/**
	 * validation of the given jwt and return the owners id
	 * 
	 * @param authHeader - auth token
	 * @return owner's id
	 * @throws Exception if the token not acceptable
	 */
	public Integer jwtValidation(String authHeader) throws Exception {
		if (authHeader == null)
			throw new Exception("no auth header");
		String[] authHeaderArr = authHeader.split("Bearer ");
		if (authHeaderArr.length <= 1 || authHeaderArr[1] == null)
			throw new Exception("auth header invalid");
		String token = authHeaderArr[1];

		Claims claims = Jwts.parser().setSigningKey(secretKey)
				.parseClaimsJws(token).getBody();

		Object userIdClaim = claims.get("userId");
		if (userIdClaim == null)
			throw new Exception("Invalid or expired token");

		Integer userId = Integer.parseInt(userIdClaim.toString());
		return userId;
	}
}
