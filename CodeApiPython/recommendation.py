"""Simple recommendation placeholder.

This module provides a minimal implementation compatible with the Spring client
contracts. It returns a ranked list of candidate game IDs with dummy scores.
"""

from typing import List
from models import RecommendationsRequest, RecommendationItem


def generate_recommendations(req: RecommendationsRequest) -> List[RecommendationItem]:
    """Return up to k recommendations from provided candidates with dummy scores.

    - If `candidates` is provided, use them; otherwise fall back to a default list.
    - Assign descending scores starting at 0.95.
    """
    base_candidates = req.candidates or [101, 102, 103, 104, 105]
    k = req.k if req.k is not None else min(5, len(base_candidates))
    selected = base_candidates[:k]
    start_score = 0.95
    step = 0.01

    items: List[RecommendationItem] = []
    for idx, game_id in enumerate(selected):
        score = max(0.0, round(start_score - step * idx, 2))
        items.append(RecommendationItem(game_id=game_id, score=score))
    return items
