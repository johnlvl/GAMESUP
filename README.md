# GamesUP — Backend Spring + API Recommandation FastAPI

Projet mono-repo avec:
- `gamesUP/`: API Spring Boot (Java 21) pour la gestion du catalogue (jeux, catégories, éditeurs, auteurs) + recherche avancée.
- `CodeApiPython/`: API FastAPI (Python) pour les recommandations KNN.
- `docker-compose.yml`: orchestration DB MySQL + backend + reco.

---

## Aperçu
- Port backend: `http://localhost:8080`
- Port reco (FastAPI): `http://localhost:8000`
- Sécurité backend: HTTP Basic (utilisateurs in-memory)
  - Admin: `admin` / `admin`
  - Client: `client` / `client`
- Recherche: `GET /api/games/search?q=...&categoryId=...&publisherId=...&authorId=...&priceMin=...&priceMax=...&inStock=...&page=0&size=20&sort=price,asc`
- Reco (FastAPI):
  - `GET /health`
  - `POST /events/batch`
  - `POST /train`
  - `POST /recommendations`

L’API Spring appelle l’API reco via `reco.base-url=http://localhost:8000` (config par défaut).

---

## Prérequis
- Java 21 (JDK Temurin recommandé)
- Maven Wrapper (fourni: `gamesUP/mvnw.cmd`)
- Python 3.11+ (recommandé: 3.11 ou 3.12+ avec `numpy>=2.1` — requirements déjà OK)
- Docker Desktop (optionnel, pour l’orchestration complète)

---

## Démarrage rapide

### Option A — Tout avec Docker Compose
```powershell
# À la racine du repo (là où est le docker-compose.yml)
cd "$(Split-Path $MyInvocation.MyCommand.Path)"

docker compose up --build
# Backend: http://localhost:8080
# Reco:    http://localhost:8000
# MySQL:   localhost:3306 (user/pass: gamesup/gamesup, DB: GamesUP)
```
Arrêt:
```powershell
docker compose down -v
```

### Option B — Local: Backend + Reco

- Backend Spring (profil `dev`, port 8080)
```powershell
cd .\gamesUP
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

- API Reco (FastAPI, port 8000)
```powershell
cd .\CodeApiPython
python -m venv .venv; .\.venv\Scripts\Activate.ps1
pip install -r requirements.txt
uvicorn main:app --host 0.0.0.0 --port 8000 --reload
```

---

## Tester les routes (PowerShell)

### 1) API Spring — endpoints publics (GET)
- Liste/Recherche (pas d’auth nécessaire sur GET):
```powershell
# Recherche simple (q + pagination)
Invoke-RestMethod "http://localhost:8080/api/games/search?q=catan&page=0&size=10"

# Catégories
Invoke-RestMethod "http://localhost:8080/api/categories"

# Auteurs
Invoke-RestMethod "http://localhost:8080/api/authors"

# Éditeurs
Invoke-RestMethod "http://localhost:8080/api/publishers"
```

### 2) API Spring — opérations protégées (ADMIN)
Créer un en-tête Basic Auth pour `admin:admin`:
```powershell
$pair = "admin:admin"
$bytes = [System.Text.Encoding]::ASCII.GetBytes($pair)
$b64 = [System.Convert]::ToBase64String($bytes)
$headers = @{ Authorization = "Basic $b64" }
```
Exemples:
```powershell
# Créer un jeu (exemple minimal — adaptez les champs selon vos DTO)
$body = @{ name = "Test Game"; price = 29.99 } | ConvertTo-Json
Invoke-RestMethod -Uri "http://localhost:8080/api/games" -Method Post -Headers $headers -ContentType "application/json" -Body $body

# Supprimer un jeu
Invoke-RestMethod -Uri "http://localhost:8080/api/games/1" -Method Delete -Headers $headers
```

### 3) API Reco FastAPI
```powershell
# Health
Invoke-RestMethod "http://localhost:8000/health"

# Envoyer des événements (VIEW/RATING/PURCHASE)
$events = @{ events = @(
    @{ type = 'VIEW'; user_id = 1; game_id = 101; ts = (Get-Date).ToString('o') },
    @{ type = 'PURCHASE'; user_id = 1; game_id = 102; ts = (Get-Date).ToString('o') }
) } | ConvertTo-Json -Depth 5
Invoke-RestMethod -Uri "http://localhost:8000/events/batch" -Method Post -ContentType "application/json" -Body $events

# Entraîner le modèle à la demande
Invoke-RestMethod -Uri "http://localhost:8000/train" -Method Post

# Obtenir des recommandations
$req = @{ user_id = 1; candidates = @(101,102,103,104); k = 3 } | ConvertTo-Json
Invoke-RestMethod -Uri "http://localhost:8000/recommendations" -Method Post -ContentType "application/json" -Body $req
```

---

## Tests & Couverture
- Lancer les tests Java:
```powershell
cd .\gamesUP
.\mvnw.cmd clean test
```
- Rapport JaCoCo (seuil 70% activé dans le profil CI):
```powershell
.\mvnw.cmd -Pci verify
# Rapport HTML: gamesUP/target/site/jacoco/index.html
```
Note: sous JDK 25, l’instrumentation JaCoCo peut échouer en local. Préférer JDK 21 ou s’appuyer sur la CI.

---

## Dépannage rapide
- Backend ne démarre pas: vérifier que MySQL (Docker) est healthy ou basculer H2 pour les tests uniquement.
- 401/403 en écriture: utiliser Basic Auth admin/admin (rôle ADMIN requis pour POST/PUT/DELETE).
- JaCoCo en local: utiliser Java 21 ou lancer la pipeline CI avec le profil `ci`.
- FastAPI venv: requirements déjà compatibles (numpy ≥ 2.1). En cas d’erreur liée au chemin Windows accentué, exécuter depuis un chemin simple (ex: `C:\...`).

---

## Pile technique
- Spring Boot 3.x, Java 21, Spring Data JPA, Security (Basic), H2 (tests), MySQL (dev/prod), JaCoCo.
- FastAPI, Pydantic v2, scikit-learn (KNN), numpy/pandas, Uvicorn.
- Docker Compose pour orchestration locale.

---

## Licence
Projet éducatif interne.