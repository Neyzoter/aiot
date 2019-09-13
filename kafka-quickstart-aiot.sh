#!/bin/bash
# quick stark a kafka server
# please check bootstrap-server IP and port parammeters in application.properties

nohup bin/zookeeper-server-start.sh config/zookeeper.properties &
echo "Starting zookeeper..."
sleep 5s

nohup bin/kafka-server-start.sh  config/server.properties &
echo "Starting kafka server..."
sleep 5s

nohup bin/kafka-server-start.sh  config/server-2.properties &
echo "Starting kafka server-2..."
sleep 5s

nohup bin/kafka-server-start.sh  config/server-3.properties &
echo "Starting kafka server-3..."
sleep 5s


nohup bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 2 --partitions 3 --topic VehicleHttpPack &
echo "Creating topic with 3 partitions..."
sleep 5s

echo "Topics : "
bin/kafka-topics.sh --list --zookeeper localhost:2181

echo "Topic-VehicleHttpPack info : "
bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic VehicleHttpPack
