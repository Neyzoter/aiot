#!/bin/bash
# quick stark a kafka server
# please check bootstrap-server IP and port parammeters in application.properties

cd $(dirname $0)
dir_cmd=`pwd`
KAFKA_HOME=$dir_cmd
echo "Starting zookeeper..."
nohup $KAFKA_HOME/bin/zookeeper-server-start.sh $KAFKA_HOME/config/zookeeper.properties &
echo "Started zookeeper"

echo "Starting kafka server..."
nohup $KAFKA_HOME/bin/kafka-server-start.sh  $KAFKA_HOME/config/server.properties &
echo "Started kafka server  OK"

echo "Starting kafka server-2..."
nohup $KAFKA_HOME/bin/kafka-server-start.sh  $KAFKA_HOME/config/server-2.properties &
echo "Started kafka server-2 OK"

echo "Starting kafka server-3..."
nohup $KAFKA_HOME/bin/kafka-server-start.sh  $KAFKA_HOME/config/server-3.properties &
echo "Started kafka server-3 OK"

echo "Topics : "
$KAFKA_HOME/bin/kafka-topics.sh --list --zookeeper localhost:2181
