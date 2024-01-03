# Dockerfile

# 使用Tomcat基础镜像
FROM tomcat:9.0-jre11

# 复制WAR包到Tomcat的webapps目录下
COPY target/java-web-1.0.war /usr/local/tomcat/webapps/
