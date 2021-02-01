FROM centos:centos7

#MAINTAINER 维护者信息
MAINTAINER WangMin 641718840@qq.com
ADD default.conf /etc/nginx/conf.d/
ADD nginx.repo /etc/yum.repos.d/
#RUN 执行以下命令
RUN yum install -y nginx

#EXPOSE 映射端口
EXPOSE 80

#CMD 运行以下命令，daemon off后台运行，否则启动完就自动关闭
CMD ["/usr/sbin/nginx", "-g","daemon off;"]
