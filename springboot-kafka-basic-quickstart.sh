#!/bin/bash
# quick stark a kafka server for module "springboot-kafka-basic"

nohup bin/zookeeper-server-start.sh config/zookeeper.properties &
echo "Starting zookeeper..."
sleep 5s


nohup bin/kafka-server-start.sh  config/server.properties &
echo "Starting kafka server..."
sleep 5s

nohup bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic myTopic &
echo "Creating topic..."
sleep 5s

bin/kafka-topics.sh --list --bootstrap-server localhost:9092
