#cicd 插件指定的docker目录
#FROM openjdk:8-jdk-alpine
#MAINTAINER wangmin <641718840@qq.com>
#RUN mkdir -p /opt/upgrade/
#ADD ipc_spill_final.jar ipc_spill_final.jar
#CMD java -jar ipc_spill_final.jar

FROM davidcaste/alpine-java-unlimited-jce:jre8
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.ustc.edu.cn/g' /etc/apk/repositories
RUN apk add --no-cache tzdata
RUN mkdir -p /opt/upgrade/
RUN mkdir -p /opt/jre/  \
&& ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
&& echo "Asia/Shanghai" > /etc/timezone \
&&rm -rf /var/cache/apk/* /tmp/* /var/tmp/* $HOME/.cache
ENV TZ=Asia/Shanghai

#VOLUME
ADD ipc_spill_final.jar  /opt/jre/ipc_spill_final.jar
RUN bash -c 'touch /opt/jre/ipc_spill_final.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/opt/jre/ipc_spill_final.jar"]
