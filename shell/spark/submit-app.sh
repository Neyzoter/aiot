#!/bin/bash

# submit a application
$SPARK_HOME/bin/spark-submit \
    --class cn.neyzoter.aiot.fddp.FddpApplication \
    --master spark://localhost:7077 \
    /home/scc/code/java/aiot/fddp/target/fddp*.jar