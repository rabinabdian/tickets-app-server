package com.TicketsAppServer.repositories;

import org.springframework.data.repository.CrudRepository;

import com.TicketsAppServer.beans.Ticket;

public interface TicketRepository extends CrudRepository<Ticket, Integer> {

}
