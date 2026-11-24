# TravelGo Deployment

## Windows local setup
Install Java 21, Maven, Node.js 20, Docker Desktop, and MySQL 8. Copy `backend/.env.example` to `.env` and set a local password. Create the database with `CREATE DATABASE travelgo;` or let the JDBC URL create it.

Run the API with `cd backend; mvn spring-boot:run`. Run the UI with `cd frontend; npm install; npm run dev`. The UI is available at `http://localhost:5173`.

## Docker
From the repository root run `docker compose up --build`. Compose starts MySQL, waits for its health check, then starts Spring Boot and the Nginx-served frontend. Set `MYSQL_ROOT_PASSWORD` and `JWT_SECRET` in the shell for non-demo use.

## Netlify frontend with a hosted API
The frontend must not use `http://localhost:5000/api` in a deployed build. In Netlify, add the environment variable `VITE_API_URL` with the public Spring Boot API base URL ending in `/api` (for example, `https://your-api-host.example.com/api`), then trigger a new deploy. The backend must be publicly reachable over HTTPS and its `CORS_ALLOWED_ORIGINS` environment variable must include `https://travelgo-af1.netlify.app`. For a different Netlify site domain, use that exact origin instead. The backend defaults to allowing the local Vite origin and this Netlify origin.

## Free hosting: Netlify + Render + Aiven
| Part | Service | Redeploys on push to `main` |
|---|---|---|
| Frontend | Netlify (`netlify.toml`) | Yes, when the site is linked to the GitHub repo |
| Backend | Render free web service (`render.yaml`) | Yes, when files under `backend/` change |
| Database | Aiven free MySQL | No; data stays. Tables update through `ddl-auto=update` |

1. **Aiven**: create a free MySQL service. From its overview page, note the host, port, user (`avnadmin`) and password.
2. **Render**: New -> Blueprint -> this repository. Fill in:
   - `DB_URL` = `jdbc:mysql://<host>:<port>/defaultdb?sslMode=REQUIRED&serverTimezone=UTC`
   - `DB_USERNAME` = `avnadmin`, `DB_PASSWORD` = the Aiven password.
   `JWT_SECRET` is generated automatically. The first start creates the tables and seeds sample data and the admin account.
3. **Netlify**: Site configuration -> Environment variables -> `VITE_API_URL` = `https://<render-service>.onrender.com/api`, then Deploys -> Trigger deploy.
4. Check `https://<render-service>.onrender.com/actuator/health` returns `{"status":"UP"}`, then open the Netlify site.

Free-tier notes: the Render backend sleeps after about 15 idle minutes, and the first request then takes up to a minute (the frontend waits up to 90 seconds). Open the site a few minutes before a demo. To add data, use the Admin pages, or connect MySQL Workbench to Aiven with the same connection details.

## Kubernetes/GitOps
Create the namespace and secret from `k8s/secrets.example.yaml` after replacing values, then apply `kubectl apply -f k8s/`. Configure the Argo CD repository URL in `k8s/argocd-application.yaml`. Argo Rollouts advances frontend traffic through 10%, 25%, 50%, and 100% with pauses, allowing rollback with `kubectl argo rollouts undo rollout/travelgo-frontend -n travelgo`.
