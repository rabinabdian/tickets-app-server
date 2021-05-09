package com.TicketsAppServer;

import java.util.HashMap;
import java.util.Map;

public class ExceptionGenerator {
	/**
	 * error message generator
	 * 
	 * @param errorMessage if there is an exception return it otherwise return
	 *                     default exception
	 * @return exception map
	 */
	public static Map<String, String> exceptionGenerate(String errorMessage) {
		Map<String, String> error = new HashMap<>();
		error.put("error", errorMessage.length() > 0 ? errorMessage
				: "something went wrong");
		return error;
	}
}
