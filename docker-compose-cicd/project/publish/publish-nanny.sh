#!/bin/bash
#
# 这脚本作用是本地发布到线上，流程为：更新代码->打包->构建->提交，此脚本可被jenkins调用
# 使用方法: ./publish.sh 项目名 工程名 TAG
#
source /etc/profile
# def env
BASE_PATH="/docker/docker-compose/project"
HARBOR_HOSTNAME="10.0.40.66:8090"
PROJECT_NAME=$1
SERVICE_NAME=$2
TAG=$3
PUBLISH_DEV="10.0.40.66"

if [ "$4" = "dev" ];then
PUBLISH_DEV=$PUBLISH_DEV
PUBLISH_HOST=$PUBLISH_DEV
PUBLISH_GIT_TAG="dev"
elif [ "$4" = "test" ];then
PUBLISH_DEV=$PUBLISH_DEV
PUBLISH_HOST="testenviroment"
PUBLISH_GIT_TAG="master"
elif [ "$4" = "prod" ];then
PUBLISH_DEV=$PUBLISH_DEV
PUBLISH_HOST="hzz06"
else
PUBLISH_HOST=$PUBLISH_DEV
fi

if [ "$5" = "origin" ];then
PUBLISH_GIT_REMOTE="origin"
elif [ "$5" != "" ];then
PUBLISH_GIT_REMOTE=$5
else
PUBLISH_GIT_REMOTE="origin"
fi

if [ "$6" = "master" ];then
PUBLISH_GIT_TAG="master"
elif [ "$6" != "" ];then
PUBLISH_GIT_TAG=$6
else
PUBLISH_GIT_TAG="master"
fi

# 更新代码
cd $BASE_PATH
cd $PROJECT_NAME/$SERVICE_NAME
git checkout $PUBLISH_GIT_TAG
git pull $PUBLISH_GIT_REMOTE $PUBLISH_GIT_TAG


# todo list 判断如果是某些项目存在依赖包的话，带个参数是否去打包

# todo list 直接进入mvn.sh

# 打包

bash -c "mvn clean && mvn package -Dmaven.test.skip=true"

if [ $? -ne 0 ]
then
        echo "ERROR:maven build error,exit!"
        exit 1
fi

# 构建
BUILD_PATH=$HARBOR_HOSTNAME/$PROJECT_NAME/$SERVICE_NAME:$TAG
docker build -t $BUILD_PATH .

if [ $? -ne 0 ]
then
        echo "ERROR:docker build error,exit!"
        exit 1
fi

# 提交
if [ "$4" = "test" ];then
docker push $BUILD_PATH

  if [ $? -ne 0 ]
  then
        echo "ERROR:image push error,exit!"
        exit 1
  fi
fi

PUBHOSTS=$(echo $PUBLISH_HOST |sed 's/,/ /g')
if [ "$4" = "dev" ];then
cd $BASE_PATH/publish/$PROJECT_NAME/$SERVICE_NAME && VER=$TAG PROJECT=$PROJECT_NAME SERVICE=$SERVICE_NAME docker-compose down
sleep 1
cd $BASE_PATH/publish/$PROJECT_NAME/$SERVICE_NAME && VER=$TAG PROJECT=$PROJECT_NAME SERVICE=$SERVICE_NAME docker-compose up -d
sleep 1
exit
fi
#docker-compose deploy
#PUBHOSTS=$(echo $PUBLISH_HOST |sed 's/,/ /g')
for host in $PUBHOSTS
do
echo $host
ansible $host -m shell -a "cd $BASE_PATH/publish/$PROJECT_NAME/$SERVICE_NAME && VER=$TAG PROJECT=$PROJECT_NAME SERVICE=$SERVICE_NAME docker-compose down"
sleep 1
ansible $host -m shell -a "cd $BASE_PATH/publish/$PROJECT_NAME/$SERVICE_NAME && VER=$TAG PROJECT=$PROJECT_NAME SERVICE=$SERVICE_NAME docker-compose up -d"

echo 'waiting app start completely...'
sleep 1
echo 'deploy next node...'
done
echo 'publish end!'

exit

