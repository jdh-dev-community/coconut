#!/bin/bash

# sudo 미적용으로 사용자의 .bash_profile에서 환경변수 로드 가능
echo "[$(date '+%Y-%m-%d %H:%M:%S')] @@@@@ Host start up started @@@@@" >> /home/ec2-user/deploy.log

source ~/.bash_profile

sudo rm -rf "/home/ec2-user/$REPO"

echo "[$(date '+%Y-%m-%d %H:%M:%S')] @@@@@ Host start up finished @@@@@" >> /home/ec2-user/deploy.log