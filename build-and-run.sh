#!/bin/bash

docker-compose build

docker-compose up -d

echo "Services are starting..."
echo "API Gateway: http://localhost:8084"
echo "Payments Service: http://localhost:8080"
echo "Orders Service: http://localhost:8082"
echo "Frontend: http://localhost:8081"

echo "To view logs: docker-compose logs -f"
echo "To stop services: docker-compose down" 