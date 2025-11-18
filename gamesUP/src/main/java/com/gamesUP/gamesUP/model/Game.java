package com.gamesUP.gamesUP.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id; // Keep Integer to avoid breaking existing controller code

    public String nom;
    public String auteur; // legacy textual author
    public String genre;

    @ManyToOne
    @JoinColumn(name = "category_id")
    public Category category;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    public Publisher publisher;

    @ManyToOne
    @JoinColumn(name = "author_id")
    public Author author; // relational author

    public int numEdition;
    public BigDecimal price;

    public Game() {}
}
