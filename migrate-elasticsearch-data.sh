#!/bin/bash
localElastic=$(docker ps | grep 'docker.elastic.co/elasticsearch/elasticsearch' | awk '{print $1}')

if [[ "$localElastic" != "" ]]
then
    input="http://elasticsearch:9200/shakespeare"
    echo "Moving data from: $input to $1"

    docker run --name=elasticsearch-dump --link=${localElastic}:elasticsearch taskrabbit/elasticsearch-dump --input=${input} \
      --output=$1 \
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