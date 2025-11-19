from pydantic import BaseModel
from typing import List, Optional

class UserPurchase(BaseModel):
    game_id: int
    rating: float

class UserData(BaseModel):
    user_id: int
    purchases: List[UserPurchase]

class RecommendationsRequest(BaseModel):
    user_id: int
    k: Optional[int] = None
    candidates: Optional[List[int]] = None


class RecommendationItem(BaseModel):
    game_id: int
    score: float


class RecommendationsResponse(BaseModel):
    user_id: int
    recommendations: List[RecommendationItem]
    model_version: str


class EventItem(BaseModel):
    type: str  # 'PURCHASE' | 'RATING' | 'VIEW'
    user_id: int
    game_id: int
    value: Optional[float] = None
    ts: str  # ISO8601 timestamp


class EventsBatchRequest(BaseModel):
    events: List[EventItem]


class EventsBatchResponse(BaseModel):
    accepted: int


class HealthResponse(BaseModel):
    status: str
    model_loaded: bool


class TrainResponse(BaseModel):
    status: str  # 'ok' or 'skipped'
    users: int
    games: int
    trained: bool
