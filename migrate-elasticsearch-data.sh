#!/bin/bash
localElastic=$(docker ps | grep 'docker.elastic.co/elasticsearch/elasticsearch' | awk '{print $1}')

if [[ "$localElastic" != "" ]]
then
    from="http://elasticsearch:9200/$1"
    to=$2
    echo "Moving data from: $from to $to"

    docker run --name=elasticsearch-dump --network=docker_elk --link=${localElastic}:elasticsearch taskrabbit/elasticsearch-dump --input=${from} \
      --output=${to} \
      --type=data

    echo "Stopped docker container:" $(docker stop elasticsearch-dump)
    echo "Removed docker container:" $(docker rm elasticsearch-dump)

    echo "**********************************************************************************"
    echo "******************** Imported data to cloud elasticsearch! :) ********************"
    echo "**********************************************************************************"

else
    echo "**********************************************************************************"
    echo "************************** Local elastic is not running **************************"
    echo "**********************************************************************************"
fi