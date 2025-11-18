package com.gamesUP.gamesUP.model;

import jakarta.persistence.*;

@Entity
@Table(name = "avis")
public class Avis {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String commentaire;

	private int note;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "game_id")
	private Game game;

	public Avis() {}

	public Long getId() { return id; }
	public String getCommentaire() { return commentaire; }
	public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
	public int getNote() { return note; }
	public void setNote(int note) { this.note = note; }
	public User getUser() { return user; }
	public void setUser(User user) { this.user = user; }
	public Game getGame() { return game; }
	public void setGame(Game game) { this.game = game; }
}
