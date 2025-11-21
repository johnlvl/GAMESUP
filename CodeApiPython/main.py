from fastapi import FastAPI, HTTPException
from typing import List
from recommendation import generate_recommendations
from knn_pipeline import (
    knn_recommender,
    store_events,
    train_current_events,
    recommend_with_knn,
)
from models import (
    RecommendationsRequest,
    RecommendationsResponse,
    RecommendationItem,
    EventsBatchRequest,
    EventsBatchResponse,
    HealthResponse,
    TrainResponse,
)

app = FastAPI()

# Endpoint de base pour tester que l'API est en ligne
@app.get("/")
async def root():
    return {"message": "API de recommandation en ligne"}

@app.get("/health", response_model=HealthResponse)
async def health():
    return HealthResponse(status="ok", model_loaded=knn_recommender.trained)


# Endpoint pour récupérer des recommandations
@app.post("/recommendations", response_model=RecommendationsResponse)
async def recommendations(req: RecommendationsRequest):
    try:
        # If model trained, attempt KNN; else fallback mock
        items: List[RecommendationItem]
        if knn_recommender.trained:
            candidates = req.candidates or []
            k = req.k or min(5, len(candidates) if candidates else 5)
            if not candidates:
                # Fallback if no explicit candidates provided
                items = generate_recommendations(req)
            else:
                knn_results = recommend_with_knn(req.user_id, candidates, k)
                if not knn_results:
                    items = generate_recommendations(req)
                else:
                    items = [RecommendationItem(game_id=g, score=score) for g, score in knn_results]
        else:
            items = generate_recommendations(req)
        return RecommendationsResponse(
            user_id=req.user_id,
            recommendations=items,
            model_version="v1-knn" if knn_recommender.trained else "v1-mock",
        )
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


@app.post("/events/batch", response_model=EventsBatchResponse)
async def events_batch(body: EventsBatchRequest):
    try:
        accepted = store_events([e.dict() for e in body.events])
        return EventsBatchResponse(accepted=accepted)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


@app.post("/train", response_model=TrainResponse)
async def train():
    try:
        summary = train_current_events()
        status = "ok" if knn_recommender.trained else "skipped"
        return TrainResponse(status=status, users=summary.get("users", 0), games=summary.get("games", 0), trained=knn_recommender.trained)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
