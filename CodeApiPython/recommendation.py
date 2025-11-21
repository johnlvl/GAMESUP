"""Recommendation logic with mock fallback.

If the KNN model is not trained or cannot produce results, we use a simple
descending-score mock. This keeps endpoints functional even without data.
"""

from typing import List
from knn_pipeline import recommend_with_knn, knn_recommender
from models import RecommendationsRequest, RecommendationItem


def generate_recommendations(req: RecommendationsRequest) -> List[RecommendationItem]:
    """Return recommendations using KNN if trained, else mock fallback.

    Mock strategy:
    - Use provided candidates or default list.
    - Assign descending scores.
    """
    base_candidates = req.candidates or [101, 102, 103, 104, 105]
    k = req.k if req.k is not None else min(5, len(base_candidates))

    if knn_recommender.trained and req.candidates:
        # Attempt KNN direct recommendation
        knn_results = recommend_with_knn(req.user_id, req.candidates, k)
        if knn_results:
            return [RecommendationItem(game_id=g, score=round(score, 4)) for g, score in knn_results]

    # Fallback mock scoring
    selected = base_candidates[:k]
    start_score = 0.95
    step = 0.01
    return [
        RecommendationItem(game_id=gid, score=max(0.0, round(start_score - step * idx, 2)))
        for idx, gid in enumerate(selected)
    ]
