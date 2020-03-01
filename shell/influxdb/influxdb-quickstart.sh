#!/bin/bash
# quick stark InfluxDB

echo "Starting InfluxDB ..."
nohup influxd &
echo "Started InfluxDB OK"