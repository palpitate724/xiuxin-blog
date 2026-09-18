# ===== 构建阶段 =====
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# 1. 先复制 pom.xml，利用缓存
COPY pom.xml .
COPY blog_common/pom.xml ./blog_common/
COPY blog_domain/pom.xml ./blog_domain/
COPY blog_web/pom.xml ./blog_web/

# 2. 下载依赖
RUN mvn dependency:go-offline -B -Dmaven.test.skip=true

# 3. 复制源码
COPY blog_common/src ./blog_common/src
COPY blog_domain/src ./blog_domain/src
COPY blog_web/src ./blog_web/src

# 4. 构建
RUN mvn clean package -DskipTests -B

# ===== 运行阶段 =====
FROM eclipse-temurin:17-jre-alpine

# 1. 装时区和 curl
RUN apk add --no-cache tzdata curl

# 2. 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 3. 工作目录
WORKDIR /app

# 4. 拷贝 JAR（使用通配符匹配）
COPY --from=builder /app/blog_web/target/*.jar app.jar

# 5. 暴露端口
EXPOSE 8080

# 6. JVM 参数
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# 7. 健康检查（检查端口是否可访问）
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/ || exit 1

# 8. 启动（用 exec 让 java 成为 PID 1）
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]