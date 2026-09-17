# Monitoring starter

Use Prometheus + Grafana for the final demo. The included config is a starter Kubernetes scrape configuration.

Recommended dashboards:
- CPU and memory
- Request rate
- HTTP error rate
- Latency
- Pod health
- Canary rollout status

For a real cluster, install the kube-prometheus-stack with Helm and then add application metrics/ServiceMonitors.
