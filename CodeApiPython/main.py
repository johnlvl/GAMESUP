from fastapi import FastAPI, HTTPException
from typing import List
from recommendation import generate_recommendations
from models import (
    RecommendationsRequest,
    RecommendationsResponse,
    RecommendationItem,
    EventsBatchRequest,
    EventsBatchResponse,
    HealthResponse,
)

app = FastAPI()

# Endpoint de base pour tester que l'API est en ligne
@app.get("/")
async def root():
    return {"message": "API de recommandation en ligne"}

@app.get("/health", response_model=HealthResponse)
async def health():
    return HealthResponse(status="ok", model_loaded=True)


# Endpoint pour récupérer des recommandations
@app.post("/recommendations", response_model=RecommendationsResponse)
async def recommendations(req: RecommendationsRequest):
    try:
        items: List[RecommendationItem] = generate_recommendations(req)
        return RecommendationsResponse(
            user_id=req.user_id,
            recommendations=items,
            model_version="v1",
        )
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


@app.post("/events/batch", response_model=EventsBatchResponse)
async def events_batch(body: EventsBatchRequest):
    try:
        accepted = len(body.events)
        return EventsBatchResponse(accepted=accepted)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
