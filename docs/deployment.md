# TravelGo Deployment

## Windows local setup
Install Java 21, Maven, Node.js 20, Docker Desktop, and MySQL 8. Copy `backend/.env.example` to `.env` and set a local password. Create the database with `CREATE DATABASE travelgo;` or let the JDBC URL create it.

Run the API with `cd backend; mvn spring-boot:run`. Run the UI with `cd frontend; npm install; npm run dev`. The UI is available at `http://localhost:5173`.

## Docker
From the repository root run `docker compose up --build`. Compose starts MySQL, waits for its health check, then starts Spring Boot and the Nginx-served frontend. Set `MYSQL_ROOT_PASSWORD` and `JWT_SECRET` in the shell for non-demo use.

## Kubernetes/GitOps
Create the namespace and secret from `k8s/secrets.example.yaml` after replacing values, then apply `kubectl apply -f k8s/`. Configure the Argo CD repository URL in `k8s/argocd-application.yaml`. Argo Rollouts advances frontend traffic through 10%, 25%, 50%, and 100% with pauses, allowing rollback with `kubectl argo rollouts undo rollout/travelgo-frontend -n travelgo`.
