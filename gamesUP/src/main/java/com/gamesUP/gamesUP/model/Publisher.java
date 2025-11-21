package com.gamesUP.gamesUP.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "publishers")
public class Publisher {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@OneToMany(mappedBy = "publisher")
	private List<Game> games;

	public Publisher() {}

	public Long getId() { return id; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
}
