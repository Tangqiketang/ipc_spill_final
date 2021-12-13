#!/bin/bash
#
# 这脚本作用是本地发布到线上，流程为：更新代码->打包->构建->提交，此脚本可被jenkins调用
# 开发dev环境 sh /docker/docker-compose/publish/publish-nanny.sh  supernanny  server  build`date "+%Y%m%d%H%M%S"`  dev  origin  dev
# 测试test环境 sh /docker/docker-compose/publish/publish-nanny.sh supernanny   server build`date "+%Y%m%d%H%M%S"`  test origin master
# 生产prod环境 sh /docker/docker-compose/publish/publish-nanny-aliyun.sh supernanny server build`date "+%Y%m%d%H%M%S"` prod origin prod
# # sh /docker/docker-compose/publish/publish-nanny.sh
#(1:项目名)supernanny  (2.服务名) server  (3.镜像tag)build`date "+%Y%m%d%H%M%S"` (4.部署环境)dev  (5.git本地orign/分支)origin  (6.git分支)dev
# 镜像名称 :10.0.40.66:8090/supernanny/server:build20211201144424
# 使用方法: ./publish.sh 项目名 工程名 TAG 部署环境  orgin  dev
#
source /etc/profile
# def env
BASE_PATH="/home/docker-compose/docker-compose-cicd/project/publish/"
HARBOR_HOSTNAME="192.168.40.131:8090"
PROJECT_NAME=$1
SERVICE_NAME=$2
TAG=$3
PUBLISH_DEV="192.168.40.131"

if [ "$4" = "homedev" ];then
PUBLISH_DEV=$PUBLISH_DEV
PUBLISH_HOST=$PUBLISH_DEV
PUBLISH_GIT_TAG="homedev"
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
cd $BASE_PATH/localGit
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

# 先构建镜像推送到harbor. 后面运行docker-compose指定harbor中镜像
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
if [ "$4" = "homedev" ];then
cd $BASE_PATH/docker-compose-yml/$PROJECT_NAME/$SERVICE_NAME && VER=$TAG PROJECT=$PROJECT_NAME SERVICE=$SERVICE_NAME docker-compose down
sleep 1
cd $BASE_PATH/docker-compose-yml/$PROJECT_NAME/$SERVICE_NAME && VER=$TAG PROJECT=$PROJECT_NAME SERVICE=$SERVICE_NAME docker-compose up -d
sleep 1
exit
fi
#docker-compose deploy
#PUBHOSTS=$(echo $PUBLISH_HOST |sed 's/,/ /g')
for host in $PUBHOSTS
do
echo $host
ansible $host -m shell -a "cd $BASE_PATH/$PROJECT_NAME/$SERVICE_NAME && VER=$TAG PROJECT=$PROJECT_NAME SERVICE=$SERVICE_NAME docker-compose down"
sleep 1
ansible $host -m shell -a "cd $BASE_PATH/$PROJECT_NAME/$SERVICE_NAME && VER=$TAG PROJECT=$PROJECT_NAME SERVICE=$SERVICE_NAME docker-compose up -d"

echo 'waiting app start completely...'
sleep 1
echo 'deploy next node...'
done
echo 'publish end!'

exit

