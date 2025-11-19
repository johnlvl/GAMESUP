# CodeApiPython

FastAPI microservice providing recommendation capabilities consumed by the Java Spring application.

## KNN Recommendation Architecture

The service includes a KNN-based recommender (`knn_pipeline.py`) that builds an implicit user–game interaction matrix from ingested events:

| Event Type | Weight |
|------------|--------|
| PURCHASE   | 5.0    |
| RATING     | 3.0    |
| VIEW       | 1.0    |

Training converts accumulated events into a sparse matrix and fits `sklearn.neighbors.NearestNeighbors` on game vectors. After training, recommendations for a user are scored by aggregating similarity contributions from games the user has interacted with.

Fallback: If the model is not trained or insufficient data exists, a mock descending-score recommendation path is used so endpoints remain functional.

## Endpoints

- `GET /health` – Returns service status (`model_loaded` flag indicates KNN readiness).
- `POST /events/batch` – Ingest a list of events (`user_id`, `game_id`, `event_type`). Stored in-memory.
- `POST /train` – Triggers KNN training over current in-memory events. Returns counts and trained status.
- `POST /recommendations` – Provides recommendations. Uses KNN if trained; otherwise mock fallback.

## Data & Limitations

- Events are stored in-memory (`EVENT_STORE`); restarting the service clears history.
- Requires at least one user with multiple distinct game interactions to produce meaningful KNN output.
- No persistence or scheduling yet; future work should introduce durable storage + periodic retraining.

## Running

Install dependencies and start the server:

```bash
pip install -r requirements.txt
uvicorn main:app --reload --port 8001
```

### Sample Interaction Flow

```bash
# 1. Ingest events
curl -X POST http://localhost:8001/events/batch -H "Content-Type: application/json" -d '[{"user_id":1,"game_id":42,"event_type":"VIEW"},{"user_id":1,"game_id":43,"event_type":"PURCHASE"},{"user_id":2,"game_id":42,"event_type":"VIEW"}]'

# 2. Train model
curl -X POST http://localhost:8001/train

# 3. Get recommendations
curl -X POST http://localhost:8001/recommendations -H "Content-Type: application/json" -d '{"user_id":1, "candidates":[42,43,44,45], "k":3}'
```

## requirements.txt

Contents:

```
fastapi==0.115.0
uvicorn==0.30.0
pydantic==2.9.2
numpy==1.26.4
pandas==2.2.3
scikit-learn==1.5.2
```

## Next Steps

- Add persistence layer (SQLite or PostgreSQL).
- Implement incremental / scheduled training.
- Incorporate more event types and decay weighting over time.
- Add evaluation metrics endpoint (coverage, diversity, personalization).
