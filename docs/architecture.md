# TravelGo Architecture

TravelGo is a React/Vite single-page application backed by a stateless Spring Boot 3 REST API. The API uses Spring Data JPA to persist users, destinations, hotels, travel packages, and bookings in MySQL 8.

Authentication is JWT based. BCrypt hashes passwords, the token carries `userId`, `email`, and `role`, and the frontend Axios interceptor sends it as a Bearer token. Admin mutations are protected with Spring Security method authorization.

The delivery path is GitHub Actions -> container images tagged by commit SHA -> Argo CD -> Kubernetes. The frontend uses an Argo Rollout canary strategy while the backend and MySQL run as Kubernetes workloads.
