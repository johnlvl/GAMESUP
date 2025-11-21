package com.gamesUP.gamesUP.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String type;

	@OneToMany(mappedBy = "category")
	private List<Game> games;

	public Category() {}

	public Long getId() { return id; }
	public String getType() { return type; }
	public void setType(String type) { this.type = type; }
}
