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
PUBLISH_DEV="dev"
PUBLISH_HOST="hzz04"
elif [ "$4" = "prod" ];then
PUBLISH_DEV="prod"
PUBLISH_HOST="aliyunmaster"
PUBLISH_GIT_TAG="master"
elif [ "$4" = "pro" ];then
PUBLISH_DEV="pro"
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
elif [ "$6" = "prod" ];then
PUBLISH_GIT_TAG="prod"
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
cd target

PUBHOSTS=$(echo $PUBLISH_HOST |sed 's/,/ /g')
for host in $PUBHOSTS
do
echo $host
ansible $host -m shell -a "cd $BASE_PATH/publish/$PROJECT_NAME/$SERVICE_NAME && rm -rf super-nanny-server.jar"
echo 'waiting delete to aliyun'
sleep 1
ansible $host -m copy  -a "src=super-nanny-server.jar dest=/docker/docker-compose/project/publish/$PROJECT_NAME/$SERVICE_NAME "

echo 'waiting copy to aliyun'
sleep 1
echo 'deploy next node...'
done

cd ..

# 构建
BUILD_PATH=$HARBOR_HOSTNAME/$PROJECT_NAME/$SERVICE_NAME:$TAG
#docker build -t $BUILD_PATH .


#if [ $? -ne 0 ]
#then
 #       echo "ERROR:docker build error,exit!"
  #      exit 1
#fi

# 提交
#docker push $BUILD_PATH

#if [ $? -ne 0 ]
#then
 #       echo "ERROR:image push error,exit!"
 #       exit 1
#fi

ansible $host -m shell -a "cd $BASE_PATH/publish/$PROJECT_NAME/$SERVICE_NAME && docker build -t $BUILD_PATH ."
sleep 2
ansible $host -m shell -a "cd $BASE_PATH/publish/$PROJECT_NAME/$SERVICE_NAME && VER=$TAG PROJECT=$PROJECT_NAME SERVICE=$SERVICE_NAME docker-compose down"
sleep 2
ansible $host -m shell -a "cd $BASE_PATH/publish/$PROJECT_NAME/$SERVICE_NAME && VER=$TAG PROJECT=$PROJECT_NAME SERVICE=$SERVICE_NAME docker-compose up -d"


exit