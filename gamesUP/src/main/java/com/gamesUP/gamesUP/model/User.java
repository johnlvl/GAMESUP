package com.gamesUP.gamesUP.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nom;

	@OneToMany(mappedBy = "user")
	private List<Purchase> purchases;

	public User() {}

	public Long getId() { return id; }
	public String getNom() { return nom; }
	public void setNom(String nom) { this.nom = nom; }
}
