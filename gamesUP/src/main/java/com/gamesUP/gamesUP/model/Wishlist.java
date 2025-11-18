package com.gamesUP.gamesUP.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "wishlists")
public class Wishlist {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToMany
	@JoinTable(name = "wishlist_games",
			joinColumns = @JoinColumn(name = "wishlist_id"),
			inverseJoinColumns = @JoinColumn(name = "game_id"))
	private List<Game> games;

	public Wishlist() {}

	public Long getId() { return id; }
	public User getUser() { return user; }
	public void setUser(User user) { this.user = user; }
	public List<Game> getGames() { return games; }
	public void setGames(List<Game> games) { this.games = games; }
}
