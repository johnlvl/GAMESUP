package com.gamesUP.gamesUP.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "purchases")
public class Purchase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PurchaseLine> line;

	private Instant date;
	private boolean paid;
	private boolean delivered;
	private boolean archived;

	public Purchase() {}

	public Long getId() { return id; }
	public User getUser() { return user; }
	public void setUser(User user) { this.user = user; }
	public List<PurchaseLine> getLine() { return line; }
	public void setLine(List<PurchaseLine> line) { this.line = line; }
	public Instant getDate() { return date; }
	public void setDate(Instant date) { this.date = date; }
	public boolean isPaid() { return paid; }
	public void setPaid(boolean paid) { this.paid = paid; }
	public boolean isDelivered() { return delivered; }
	public void setDelivered(boolean delivered) { this.delivered = delivered; }
	public boolean isArchived() { return archived; }
	public void setArchived(boolean archived) { this.archived = archived; }
}
