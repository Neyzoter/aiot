# 指定基础镜像，在其上进行定制
FROM java:8
# 这里的 /tmp 目录就会在运行时自动挂载为匿名卷，任何向 /tmp 中写入的信息都不会记录进容器存储层
VOLUME /tmp
# 维护者信息
MAINTAINER neyzoter
# 添加jar
ADD *.jar app.jar

# 指定容器启动程序及参数   <ENTRYPOINT> "<CMD>"
ENTRYPOINT ["java","-Xmx2048m","-Duser.timezone=GMT+08","-jar","/app.jar"]