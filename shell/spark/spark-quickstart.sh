#!/bin/bash

# Quick start Spark

echo "SPARK_HOME = $SPARK_HOME"
echo "Starting Spark Master..."
$SPARK_HOME/sbin/start-master.sh
echo "Started Spark Master OK"
cat $SPARK_HOME/logs/*.out

echo "Starging a Worker..."
nohup $SPARK_HOME/bin/spark-class org.apache.spark.deploy.worker.Worker spark://neyzoter:7077 &
echo "Started a Worker OK"