#!/bin/bash

# sudo 미적용으로 사용자의 .bash_profile에서 환경변수 로드 가능
echo "[$(date '+%Y-%m-%d %H:%M:%S')] @@@@@ Start up started @@@@@" >> /home/ec2-user/deploy.log

source ~/.bash_profile

echo "ECR Login try" >> /home/ec2-user/deploy.log
aws ecr get-login-password --region "$REGION" | docker login --username AWS --password-stdin "$USER_ID.dkr.ecr."$REGION".amazonaws.com"
echo "ECR Login success" >> /home/ec2-user/deploy.log


IMAGE="$USER_ID.dkr.ecr.$REGION.amazonaws.com/$REPO:$IMAGE_TAG"
docker pull "$IMAGE"
docker run -d -p 80:"$PORT" --name "$REPO" "$IMAGE"

echo "[$(date '+%Y-%m-%d %H:%M:%S')] @@@@@ Start up finished @@@@@" >> /home/ec2-user/deploy.log