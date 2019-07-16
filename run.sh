#!/bin/bash
cd docker
docker-compose down
docker-compose rm -f
docker-compose up --build -d

function checkContainerIsHealthy(){
	while [ true ]; do
        if [ $(docker inspect --format="{{json .State.Health.Status }}" "$1") == "healthy" ]; then
                echo "$1 is healthy. :D"
                break
        else
                echo "$1 is not healthy. Sleep for 5 second"
                sleep 5
                break
        fi
	done
}

checkContainerIsHealthy elasticsearch
checkContainerIsHealthy kibana

echo "**********************************************************************************"
echo "************************** All containers are ready! :) **************************"
echo "**********************************************************************************"

cd ..

if [[ $1 == "debug" ]]; then
	echo ""
    echo "You can run the applications in debug mode :) " 
else
	cd collector
	mvn spring-boot:run &
	cd ..
fi

exit 0
