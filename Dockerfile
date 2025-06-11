FROM openjdk:17-jdk-slim

WORKDIR /app

# 复制 Maven 构建文件
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# 复制源代码
COPY src src

# 使用 Maven 构建应用
RUN ./mvnw package -DskipTests

# 运行 jar 包
EXPOSE 8080
ENTRYPOINT ["java","-jar","target/letsrock-0.0.1-SNAPSHOT.jar"] 