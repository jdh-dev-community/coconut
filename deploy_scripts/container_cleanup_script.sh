#!/bin/bash

# sudo 미적용으로 사용자의 .bash_profile에서 환경변수 로드 가능
echo "@@@@@ Clean up started @@@@@" >> /home/ec2-user/deploy.log

source ~/.bash_profile

# 컨테이너 정지 및 삭제
if docker ps -a | grep -q "$CONTAINER_NAME"; then
     "Stopping and removing container: $CONTAINER_NAME" >> /home/ec2-user/deploy.log
     docker stop "$CONTAINER_NAME"
     docker rm "$CONTAINER_NAME"
else
    echo "Container $CONTAINER_NAME does not exist." >> /home/ec2-user/deploy.log
fi

# Docker 이미지 삭제
IMAGE="$USER_ID.dkr.ecr.$REGION.amazonaws.com/$REPO"
if docker images | grep -q "$REPO"; then
    echo "Removing Docker image: $IMAGE" >> /home/ec2-user/deploy.log
    docker rmi "$IMAGE"
else
    echo "Image $IMAGE does not exist." >> /home/ec2-user/deploy.log
fi

echo "@@@@@ Clean up end @@@@@" >> /home/ec2-user/deploy.log