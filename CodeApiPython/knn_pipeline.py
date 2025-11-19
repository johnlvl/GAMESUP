"""KNN recommendation pipeline scaffolding.

This module prepares a KNN-based recommender architecture that can be trained later
when real interaction data (events) is available. It supports:
- Model preparation (instantiate KNNRecommender)
- Training on an in-memory event store (placeholder logic)
- Recommendation using trained model or fallback to mock scoring

The actual training logic will be refined once sufficient data exists.
"""
from __future__ import annotations
from typing import List, Dict, Optional, Tuple
from dataclasses import dataclass
import numpy as np
try:
    from sklearn.neighbors import NearestNeighbors
except ImportError:  
    NearestNeighbors = None  # type: ignore

# Event weights allow basic implicit feedback aggregation
EVENT_WEIGHTS = {
    "PURCHASE": 3.0,
    "RATING": 2.0,  # rating value will be multiplied on top
    "VIEW": 1.0,
}

@dataclass
class StoredEvent:
    type: str
    user_id: int
    game_id: int
    value: Optional[float]

class KNNRecommender:
    """Simple user-game implicit matrix + NearestNeighbors scaffold.

    Builds a user x game sparse matrix of aggregated interaction scores.
    Uses NearestNeighbors to find similar users; recommends candidate games
    based on average score among nearest neighbors.
    """
    def __init__(self, n_neighbors: int = 5):
        self.n_neighbors = n_neighbors
        self._model: Optional[NearestNeighbors] = None
        self._user_index: Dict[int, int] = {}
        self._game_index: Dict[int, int] = {}
        self._matrix: Optional[np.ndarray] = None
        self.trained: bool = False

    def _build_indices(self, events: List[StoredEvent]) -> None:
        users = sorted({e.user_id for e in events})
        games = sorted({e.game_id for e in events})
        self._user_index = {u: i for i, u in enumerate(users)}
        self._game_index = {g: i for i, g in enumerate(games)}

    def _build_matrix(self, events: List[StoredEvent]) -> np.ndarray:
        if not events:
            return np.zeros((0, 0))
        self._build_indices(events)
        mat = np.zeros((len(self._user_index), len(self._game_index)), dtype=np.float32)
        for e in events:
            ui = self._user_index[e.user_id]
            gi = self._game_index[e.game_id]
            base = EVENT_WEIGHTS.get(e.type, 0.5)
            val = base
            if e.type == "RATING" and e.value is not None:
                # Scale rating (e.g., 0..5) to implicit weight
                val = base * float(e.value)
            mat[ui, gi] += val
        return mat

    def train(self, events: List[StoredEvent]) -> Dict[str, int]:
        """Train the nearest neighbors model from events.

        Returns a summary dict with counts.
        """
        if NearestNeighbors is None:
            # sklearn absent; cannot train
            self.trained = False
            return {"users": 0, "games": 0}
        matrix = self._build_matrix(events)
        if matrix.size == 0 or matrix.shape[0] < 2:
            # Not enough data to build neighbors meaningfully
            self.trained = False
            self._matrix = matrix
            return {"users": matrix.shape[0], "games": matrix.shape[1] if matrix.size else 0}
        self._model = NearestNeighbors(n_neighbors=min(self.n_neighbors, matrix.shape[0]))
        self._model.fit(matrix)
        self._matrix = matrix
        self.trained = True
        return {"users": matrix.shape[0], "games": matrix.shape[1]}

    def recommend(self, user_id: int, candidates: List[int], k: int) -> List[Tuple[int, float]]:
        """Recommend up to k candidate game IDs with scores.

        If not trained or user absent → empty list (caller will fallback).
        Scoring strategy: average implicit score among nearest users having the game.
        """
        if not self.trained or self._model is None or self._matrix is None:
            return []
        if user_id not in self._user_index:
            return []
        user_vector = self._matrix[self._user_index[user_id]].reshape(1, -1)
        distances, indices = self._model.kneighbors(user_vector, return_distance=True)
        neighbor_idxs = indices[0]
        # Aggregate scores among neighbors for candidate games
        results: List[Tuple[int, float]] = []
        for gid in candidates:
            if gid not in self._game_index:
                # Game unseen in training data -> minimal score
                results.append((gid, 0.0))
                continue
            gi = self._game_index[gid]
            scores = [self._matrix[n_idx, gi] for n_idx in neighbor_idxs]
            avg_score = float(np.mean(scores)) if scores else 0.0
            results.append((gid, round(avg_score, 4)))
        # Sort descending by score
        results.sort(key=lambda x: x[1], reverse=True)
        return results[:k]

# Global in-memory event store (placeholder; replaced by DB later)
EVENT_STORE: List[StoredEvent] = []

# Single recommender instance used by the FastAPI app
knn_recommender = KNNRecommender(n_neighbors=5)


def store_events(batch: List[Dict]) -> int:
    """Persist events in memory (placeholder). Returns count accepted."""
    for e in batch:
        EVENT_STORE.append(
            StoredEvent(
            type=e.get("type", "VIEW"),
            user_id=int(e["user_id"]),
            game_id=int(e["game_id"]),
            value=e.get("value"),
        )
        )
    return len(batch)


def train_current_events() -> Dict[str, int]:
    """Train recommender on accumulated events."""
    return knn_recommender.train(EVENT_STORE)


def recommend_with_knn(user_id: int, candidates: List[int], k: int) -> List[Tuple[int, float]]:
    return knn_recommender.recommend(user_id, candidates, k)
