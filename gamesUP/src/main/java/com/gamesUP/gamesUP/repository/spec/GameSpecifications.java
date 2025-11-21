package com.gamesUP.gamesUP.repository.spec;

import com.gamesUP.gamesUP.model.Game;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public final class GameSpecifications {

    private GameSpecifications() {}

    public static Specification<Game> nomContains(String q) {
        return (root, query, cb) -> q == null || q.isBlank() ? cb.conjunction() : cb.like(cb.upper(root.get("nom")), "%" + q.toUpperCase() + "%");
    }

    public static Specification<Game> categoryIdEquals(Long categoryId) {
        return (root, query, cb) -> categoryId == null ? cb.conjunction() : cb.equal(root.join("category", JoinType.LEFT).get("id"), categoryId);
    }

    public static Specification<Game> publisherIdEquals(Long publisherId) {
        return (root, query, cb) -> publisherId == null ? cb.conjunction() : cb.equal(root.join("publisher", JoinType.LEFT).get("id"), publisherId);
    }

    public static Specification<Game> authorIdEquals(Long authorId) {
        return (root, query, cb) -> authorId == null ? cb.conjunction() : cb.equal(root.join("author", JoinType.LEFT).get("id"), authorId);
    }

    public static Specification<Game> priceGreaterOrEqual(BigDecimal min) {
        return (root, query, cb) -> min == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("price"), min);
    }

    public static Specification<Game> priceLessOrEqual(BigDecimal max) {
        return (root, query, cb) -> max == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("price"), max);
    }

    // inStock: quantity > 0 in inventory_stock join table. We assume join table name mapped via Inventory entity.
    // Simplest approach: exists inventory_stock row with quantity > 0. Without direct entity mapping, fallback to price or skip.
    // Here we implement a LEFT join on inventoryStock collection if mapped later; placeholder returning conjunction.
    public static Specification<Game> inStock(Boolean inStock) {
        if (inStock == null) return (root, query, cb) -> cb.conjunction();
        // Placeholder: if field not yet mapped, always true unless false requested.
        if (!inStock) return (root, query, cb) -> cb.conjunction();
        // TODO: Implement actual stock join when Inventory mapping exposes quantities.
        return (root, query, cb) -> cb.conjunction();
    }
}