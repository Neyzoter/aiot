# 指定基础镜像，在其上进行定制
FROM java:8
# 这里的 /tmp 目录就会在运行时自动挂载为匿名卷，任何向 /tmp 中写入的信息都不会记录进容器存储层
VOLUME /tmp
# 维护者信息
MAINTAINER neyzoter
# 添加jar
ADD *.jar app.jar
# 声明运行时容器提供服务端口，这只是一个声明，在运行时并不会因为这个声明应用就会开启这个端口的服务
EXPOSE 8085

# 指定容器启动程序及参数   <ENTRYPOINT> "<CMD>"
ENTRYPOINT ["java","-Xmx2048m","-Duser.timezone=GMT+08","-jar","/app.jar"]