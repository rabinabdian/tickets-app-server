package com.TicketsAppServer.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.TicketsAppServer.JwtServices;
import com.TicketsAppServer.beans.Ticket;
import com.TicketsAppServer.beans.User;
import com.TicketsAppServer.repositories.TicketRepository;
import com.TicketsAppServer.repositories.UserRepository;

import lombok.Synchronized;

@Service
public class TicketService {
	@Autowired
	TicketRepository ticketRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	JwtServices JwtServices;

	/**
	 * create ticket by given ticket's data
	 * 
	 * @param ticket - data that coming from client
	 * @return ticket's data
	 */
	@Synchronized
	public Ticket create(int userId, Ticket ticket) {
		User user = userRepository.findById(userId).get();
		user.getTickets().add(ticket);
		User savedUser = userRepository.save(user);

		List<Ticket> tickets = savedUser.getTickets();

		return tickets.get(tickets.size() - 1);
	}

	/**
	 * update the ticket with the given data
	 * 
	 * @param ticket - the new ticket to update
	 * @return ticket status
	 * @throws Exception if the ticket not found
	 */
	public Ticket update(Ticket ticket, int ticketId) throws Exception {
		Optional<Ticket> opTicket = ticketRepository.findById(ticketId);
		if (opTicket.isEmpty())
			throw new Exception("ticket not found");

		Ticket originTicket = opTicket.get();
		originTicket.setTitle(ticket.getTitle());
		originTicket.setBody(ticket.getBody());
		originTicket.setPriority(ticket.getPriority());
		originTicket.setRead(ticket.isRead());
		originTicket.setColor(ticket.getColor());
		originTicket.setIcon(ticket.getIcon());
		ticketRepository.save(originTicket);
		return originTicket;
	}

	/**
	 * delete the given ticket using user's id and ticket's id
	 * 
	 * @param userId
	 * @param ticketId
	 * @return ticket status
	 * @throws Exception if the ticket not found
	 */
	public Ticket delete(int userId, int ticketId) throws Exception {
		User user = userRepository.findById(userId).get();
		List<Ticket> tickets = user.getTickets();

		for (Ticket ticket : tickets) {
			if (ticket.getId() == ticketId) {
				tickets.remove(ticket);
				ticketRepository.deleteById(ticketId);
				userRepository.save(user);
				Map<String, String> result = new HashMap<>();
				result.put("ticketId", String.valueOf(ticketId));
				result.put("status", "deleted");
				return ticket;
			}
		}
		throw new Exception("ticket not found");

	}

	/**
	 * get all the user's tickets
	 * 
	 * @param userId
	 * @return list of all the user's tickets
	 */
	public List<Ticket> getTickets(int userId) {
		User user = userRepository.findById(userId).get();
		return user.getTickets();
	}

	/**
	 * get all the user's tickets
	 * 
	 * @param userId
	 * @return list of all the user's tickets
	 * @throws Exception
	 */
	public Ticket getTicket(int userId, int ticketId) throws Exception {
		User user = userRepository.findById(userId).get();
		List<Ticket> tickets = user.getTickets();
		for (Ticket t : tickets) {
			if (t.getId() == ticketId)
				return t;
		}

		throw new Exception("ticket not found");
	}

}
