package com.TicketsAppServer.repositories;

import org.springframework.data.repository.CrudRepository;

import com.TicketsAppServer.beans.User;

public interface UserRepository extends CrudRepository<User, Integer> {
	User findByEmail(String email);
}
