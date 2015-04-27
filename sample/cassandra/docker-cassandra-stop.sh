#!/bin/bash
docker ps | grep cassandra | awk '{ print $1 }' | xargs -I {} bash -c "sudo docker stop {}; sudo docker rm {}"
