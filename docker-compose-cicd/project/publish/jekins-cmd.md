dev:
sh /docker/docker-compose/publish/publish-nanny.sh supernanny server build`date "+%Y%m%d%H%M%S"` dev origin dev

test:
sh /docker/docker-compose/publish/publish-nanny.sh supernanny server build`date "+%Y%m%d%H%M%S"` test origin master

aliyun:
sh /docker/docker-compose/publish/publish-nanny-aliyun.sh supernanny server build`date "+%Y%m%d%H%M%S"` prod origin prod