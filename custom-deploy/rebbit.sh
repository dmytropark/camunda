#!/bin/bash

echo "https://hub.docker.com/_/rabbitmq"

docker run \
-p 5672:5672 -p 15672:15672 \
--name camunda-rabbit \
rabbitmq:3-management