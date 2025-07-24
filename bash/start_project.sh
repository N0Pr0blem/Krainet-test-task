#! /bin/bash


set -e

cd ..

echo "Stoping containers..."
sudo docker-compose stop

echo "Starting Maven build..."
mvn clean install -T 1C

echo "Starting containers..."
sudo docker-compose up --build -d

sudo docker ps