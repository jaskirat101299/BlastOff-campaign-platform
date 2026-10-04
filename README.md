# BlastOff: Bulk Campaign Platform

An event-driven platform that delivers bulk email/SMS campaigns using
Spring Boot microservices, Apache Kafka, Docker, and Kubernetes.

> Status: in development

## Planned architecture
- campaign-service: create campaigns, publish one Kafka message per recipient
- sender-worker: consume and send messages (horizontally scalable)
- reporting-service: aggregate results for live progress tracking
- frontend: Angular dashboard

## Run infrastructure locally
    cd deploy
    docker compose up -d