#!/bin/sh

usage() {
    echo "Usage: sh 执行脚本.sh [start|stop|restart|status]"
    exit 1
}

is_exist(){
  pid=`ps -ef|grep ipc_spill_final.jar|grep -v grep|awk '{print $2}' `
  if [ -z "${pid}" ]; then
   return 1
  else
    return 0
  fi
}
stop(){
  is_exist
  if [ $? -eq "0" ]; then 
    echo ">>> PID = $pid begin kill -9 $pid  <<<"
    kill -9  $pid
    sleep 10
    echo ">>> ipc_spill_final process stopped <<<"  
  else
    echo ">>> ipc_spill_final is not running <<<"
  fi  
}

start(){
is_exist
if [ $? -eq "0" ]; then
echo ">>>  ipc_spill_final  is already running PID=${pid} <<<"
else
nohup java -Xms512m -Xmx2048m  -jar ipc_spill_final.jar --spring.cloud.nacos.discovery.ip=10.0.30.153 --spring.cloud.nacos.config.server-addr=10.0.30.153:8848 --spring.cloud.nacos.config.namespace=203ec45d-5654-4974-9609-7be239e6816e &
echo ">>> start  ipc_spill_final successed PID=$! <<<"
fi
}


restart(){
	stop
	start
	echo ">>>  restart success <<<"
}


case "$1" in
  "start")
    start
    ;;
  "stop")
    stop
    ;;
  "status")
    status
    ;;
  "restart")
    restart
    ;;
  *)
    usage
    ;;
esac
exit 0
