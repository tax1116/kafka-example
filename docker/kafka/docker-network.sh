#!/bin/bash

if docker network create kafka-network; then
  echo "create docker network 'kafka-network' successfully."
else
  echo "error occurred while creating docker network."
fi