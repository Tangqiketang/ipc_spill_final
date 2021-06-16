在linux服务器上装docker/docker-compose
1.把项目中docker-compose文件夹复制到linux服务器
2.将项目maven打包之后，将ipc_spill_final.jar放到/docker-compose/project/app/目录中。
3.在/docker-compose/project/compose目录下运行：
    docker-compose -p ipc_all_final  up -d  
  即可生成对应的服务组
  
注意赋予文件 chmod +777  -R fold