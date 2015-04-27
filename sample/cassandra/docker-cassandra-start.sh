#!/bin/bash

# https://registry.hub.docker.com/u/abh1nav/cassandra/
docker run -d -p 7199:7199 -p 7000:7000 -p 7001:7001 -p 9160:9160 -p 9042:9042 -d abh1nav/cassandra:latest
