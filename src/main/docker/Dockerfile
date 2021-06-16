FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD ipc_spill_final.jar ipc_spill_final.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/ipc_spill_final.jar"]