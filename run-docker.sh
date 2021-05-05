#!/bin/bash

DOCKER_BUILDKIT=1 docker build -t zendesk:ValetParking .
docker run zendesk:ValetParking
