gitlab:暂无

harbor:
    1.vim /etc/docker/daemon.json 添加支持http的不安全url  再docker重启！！！
    2.下载包到/docker-compose-cicd/project/harbor/harbor-xxx-offline.tar.gz
    3.把yml配置替换了,修改端口、data目录、log目录
    4../prepare
    5../install
    6.重启之后  docker-compose up -d/docker-compose down


jenkins:
    1.配置中文输入法 sshserver这些

git:
    1.git init;
    2.git pull url;
    3.git remote add origin url;
    4.git fetch
    5.git branch -a
    6.git config --global credential.helper store  设置一次登录后免密
    
maven:
    1.需要安装jdk
    export JAVA_HOME=/home/docker-compose/docker-compose-cicd/project/jdk/jdk1.8.0_144
    export MAVEN_HOME=/home/docker-compose/docker-compose-cicd/project/maven/apache-maven-3.6.3
    export PATH=$PATH:$MAVEN_HOME/bin:$JAVA_HOME/bin
    export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
    
    2.setting
ansible:
    查看变量：vim /etc/ansible/hosts
