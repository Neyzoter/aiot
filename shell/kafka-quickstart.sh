#!/bin/bash
# quick stark a kafka server
# please check bootstrap-server IP and port parammeters in application.properties

echo "Starting zookeeper..."
nohup $KAFKA_HOME/bin/zookeeper-server-start.sh config/zookeeper.properties &
echo "Started zookeeper"

echo "Starting kafka server..."
nohup $KAFKA_HOME/bin/kafka-server-start.sh  config/server.properties &
echo "Started kafka server"

echo "Starting kafka server-2..."
nohup $KAFKA_HOME/bin/kafka-server-start.sh  config/server-2.properties &
echo "Started kafka server-2..."

echo "Starting kafka server-3..."
nohup $KAFKA_HOME/bin/kafka-server-start.sh  config/server-3.properties &
echo "Started kafka server-3..."

echo "Topics : "
$KAFKA_HOME/bin/kafka-topics.sh --list --zookeeper localhost:2181
