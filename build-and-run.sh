#!/bin/bash

echo "Building all services..."

mvn clean package -DskipTests

echo "Building Docker images..."

docker-compose build

echo "Starting all services..."

docker-compose up -d

echo "Services are starting..."
echo "API Gateway: http://localhost:8084"
echo "Payments Service: http://localhost:8080"
echo "Orders Service: http://localhost:8082"
echo "PostgreSQL: localhost:5432"

echo "To view logs: docker-compose logs -f"
echo "To stop services: docker-compose down" 