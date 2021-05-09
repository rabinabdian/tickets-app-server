package com.TicketsAppServer.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TicketsAppServer.ExceptionGenerator;
import com.TicketsAppServer.JwtServices;
import com.TicketsAppServer.beans.Ticket;
import com.TicketsAppServer.services.TicketService;

@RestController
@RequestMapping(path = "ticket")
public class TicketController {
	@Autowired
	TicketService ticketService;

	@Autowired
	JwtServices jwtServices;

	/**
	 * create new ticket by given ticket's data
	 * 
	 * @param authHeader - auth token
	 * @param ticket     - data
	 * @return
	 */
	@PostMapping(path = "create")
	public ResponseEntity<?> create(
			@RequestHeader(value = "Authorization", required = false) String authHeader,
			@RequestBody Ticket ticket) {

		try {
			Integer userId = jwtServices.jwtValidation(authHeader);

			return new ResponseEntity<Ticket>(
					ticketService.create(userId, ticket), HttpStatus.OK);
		} catch (Exception e) {
			Map<String, String> error = ExceptionGenerator
					.exceptionGenerate(e.getMessage());

			return new ResponseEntity<Map<String, String>>(error,
					HttpStatus.FORBIDDEN);
		}

	}

	/**
	 * update the given ticket, verify the user
	 * 
	 * @param authHeader - auth token
	 * @param ticket     - the new ticket
	 * @return ticket status
	 */
	@PutMapping(path = "update/{id}")
	public ResponseEntity<?> update(
			@RequestHeader(value = "Authorization", required = false) String authHeader,
			@PathVariable("id") int ticketId, @RequestBody Ticket ticket) {
		try {
			jwtServices.jwtValidation(authHeader);

			return new ResponseEntity<Ticket>(
					ticketService.update(ticket, ticketId), HttpStatus.OK);
		} catch (Exception e) {
			Map<String, String> error = ExceptionGenerator
					.exceptionGenerate(e.getMessage());

			return new ResponseEntity<Map<String, String>>(error,
					HttpStatus.FORBIDDEN);
		}
	}

	/**
	 * delete the chosen ticket using ticket id, and verify the owner by token
	 * 
	 * @param authHeader - auth token
	 * @param ticketId   - the given ticket's id
	 * @return ticket status
	 */
	@DeleteMapping(path = "delete/{id}")
	public ResponseEntity<?> delete(
			@RequestHeader(value = "Authorization", required = false) String authHeader,
			@PathVariable("id") int ticketId) {
		try {
			int userId = jwtServices.jwtValidation(authHeader);

			return new ResponseEntity<Ticket>(
					ticketService.delete(userId, ticketId), HttpStatus.OK);
		} catch (Exception e) {
			Map<String, String> error = ExceptionGenerator
					.exceptionGenerate(e.getMessage());

			return new ResponseEntity<Map<String, String>>(error,
					HttpStatus.FORBIDDEN);
		}

	}

	/**
	 * get all the tickets from the requested user, verify by token
	 * 
	 * @param authHeader - auth token
	 * @return tickets list
	 */
	@GetMapping(path = "all")
	public ResponseEntity<?> getTickets(
			@RequestHeader(value = "Authorization", required = false) String authHeader) {
		try {
			Integer userId = jwtServices.jwtValidation(authHeader);

			List<Ticket> tickets = ticketService.getTickets(userId);

			return new ResponseEntity<List<Ticket>>(tickets, HttpStatus.OK);

		} catch (Exception e) {
			Map<String, String> error = ExceptionGenerator
					.exceptionGenerate(e.getMessage());

			return new ResponseEntity<Map<String, String>>(error,
					HttpStatus.FORBIDDEN);
		}
	}

	@GetMapping(path = "{id}")
	public ResponseEntity<?> getTicket(
			@RequestHeader(value = "Authorization", required = false) String authHeader,
			@PathVariable("id") int ticketId) {
		try {
			Integer userId = jwtServices.jwtValidation(authHeader);
			Ticket ticket = ticketService.getTicket(userId, ticketId);
			return new ResponseEntity<Ticket>(ticket, HttpStatus.OK);

		} catch (Exception e) {
			Map<String, String> error = ExceptionGenerator
					.exceptionGenerate(e.getMessage());

			return new ResponseEntity<Map<String, String>>(error,
					HttpStatus.FORBIDDEN);
		}
	}

}
