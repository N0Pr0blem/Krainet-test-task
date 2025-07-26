#! /bin/bash


set -e

cd ..

echo "Stoping containers..."
sudo docker-compose stop

echo "Starting containers..."
sudo docker-compose up --build -d

sudo docker ps