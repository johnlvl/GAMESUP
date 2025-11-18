package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
