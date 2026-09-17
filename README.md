# TravelGo

TravelGo is a full-stack travel booking system for the final-year project **TravelGo — Travel Booking System with GitOps CI/CD and Progressive Delivery**.

## Stack

- Frontend: React, Vite, React Router, Axios, Lucide React
- Backend: Java 21, Spring Boot 3.3, Spring Web, Spring Data JPA, Spring Security, JWT, Bean Validation
- Database: MySQL 8
- Delivery: Docker Compose, Kubernetes, GitHub Actions, Argo CD, Argo Rollouts

## Run on Windows

Prerequisites: Java 21, Maven, Node.js 20, and MySQL 8 or Docker Desktop.

```powershell
cd backend
mvn spring-boot:run
```

In another terminal:

```powershell
cd frontend
npm install
npm run dev
```

Open `http://localhost:5173`. The API runs at `http://localhost:5000`.

For local MySQL, create the database with:

```sql
CREATE DATABASE travelgo;
```

Set `DB_USERNAME`, `DB_PASSWORD`, `DB_URL`, `JWT_SECRET`, and `CORS_ALLOWED_ORIGINS` using `backend/.env.example` as a reference. The development initializer adds an admin account `admin@travelgo.dev` with password `Admin@123`; change it before any shared deployment.

## Docker

```powershell
$env:MYSQL_ROOT_PASSWORD="replace-me"
$env:JWT_SECRET="replace-with-a-long-random-secret"
docker compose up --build
```

The stack is MySQL -> Spring Boot API -> Nginx-served React frontend. No PostgreSQL or Node.js backend is used.

## Kubernetes and GitOps

The `k8s/` directory is the GitOps manifest directory for this repository. Create a real secret from `k8s/secrets.example.yaml`, then deploy:

```powershell
kubectl apply -f k8s/
kubectl get pods -n travelgo
kubectl argo rollouts get rollout travelgo-frontend -n travelgo --watch
```

Argo CD watches the repository and Argo Rollouts releases frontend images through 10%, 25%, 50%, and 100% canary stages with pauses. Images are tagged with the Git commit SHA, not only `latest`.

## CI/CD

`.github/workflows/ci.yml` checks out the code, builds/tests Maven, builds React, builds both Docker images, and scans the backend image with Trivy. A production workflow can push the SHA-tagged images and update the GitOps repository image references.

## Project guides

- [Architecture](docs/architecture.md)
- [API documentation](docs/api-documentation.md)
- [Deployment](docs/deployment.md)
- [Monitoring](monitoring/README.md)

## Demo flow

Register a user, sign in, browse destinations and packages, book a future trip, inspect My Bookings, cancel it, then sign in as the seeded admin and demonstrate the protected admin surface. For the project presentation, show the REST API, MySQL relationships, Docker Compose health ordering, CI run, Argo CD sync, and canary rollout progression.

# travel
