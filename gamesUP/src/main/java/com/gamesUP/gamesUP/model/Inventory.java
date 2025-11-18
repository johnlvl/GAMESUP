package com.gamesUP.gamesUP.model;

import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "inventories")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "inventory_stock", joinColumns = @JoinColumn(name = "inventory_id"))
    @MapKeyJoinColumn(name = "game_id")
    @Column(name = "quantity")
    private Map<Game, Integer> stock = new HashMap<>();

    public Inventory() {}

    public Long getId() { return id; }
    public Map<Game, Integer> getStock() { return stock; }
    public void setStock(Map<Game, Integer> stock) { this.stock = stock; }
}
