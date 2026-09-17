# Final Project Flow

Developer
  -> GitHub application repository
  -> GitHub Actions CI
  -> Docker image
  -> Container registry
  -> GitOps repository
  -> Argo CD
  -> Kubernetes
  -> Argo Rollouts
  -> Canary 10% -> 25% -> 50% -> 100%
  -> Prometheus/Grafana monitoring
  -> Automatic/manual rollback

## Demo
1. Open TravelGo.
2. Search a destination.
3. Create a demo booking.
4. Change UI text.
5. Push to GitHub.
6. Show CI build.
7. Update image tag in GitOps.
8. Argo CD syncs.
9. Show Argo Rollouts canary progression.
10. Show monitoring and rollback concept.
