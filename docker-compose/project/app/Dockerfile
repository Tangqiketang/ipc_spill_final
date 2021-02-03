# Version 0.1
# 基础镜像
FROM centos:7
# 维护者信息
MAINTAINER WangMin 641718840@qq.com
VOLUME /tmp
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8
#自动安装依赖
RUN  cd /etc/yum.repos.d/ \
    && yum -y install wget \
    && wget -O /etc/yum.repos.d/epel.repo http://mirrors.aliyun.com/repo/epel-7.repo \
    && yum clean all \
    && yum makecache \
    && yum update -y \
    && yum -y  install  java-1.8.0-openjdk java-1.8.0-openjdk-devel  \
    && yum install epel-release -y \
    && touch /etc/init.d/start.sh \
    && touch ipc_spill_final.log \
    && chmod +x /etc/init.d/start.sh \
    && echo "#!/bin/bash  " >> /etc/init.d/start.sh \
    && echo " java -Xms1024m -Xmx2048m -jar /ipc_spill_final.jar --spring.profiles.active=prod " >> /etc/init.d/start.sh
# 前端迁移到系统文件中 默认是80端口 同级目录下的html地址
#ADD  ant-design-vue-jeecg/dist/ /var/www/html/
# 拷贝相关的jar包



ADD  ipc_spill_final.jar ipc_spill_final.jar
EXPOSE  12444
ENTRYPOINT /bin/sh -c   /etc/init.d/start.sh

#启动脚本
#docker rmi $(docker images | grep "^<none>" | awk "{print $3}") 删除<none>

#docker build -t ipc_spill_final:centos .
#docker run --privileged=true  -itd  --restart=always  --name ipc_spill_final -v /opt/upgrade:/opt/upgrade  -v /etc/timezone:/etc/timezone:ro  -v /etc/localtime:/etc/localtime:ro  -p 12333:12333 -p 6379:6379 -p 5650:5650  ipc_spill:centos
#docker logs -f -t ipc_spill_final
