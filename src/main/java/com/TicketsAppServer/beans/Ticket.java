package com.TicketsAppServer.beans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
	@Id
	@GeneratedValue
	private int id;
	private String title;
	private String body;
	private int priority;
	private boolean isRead;
	private String color;
	private String icon;
}
