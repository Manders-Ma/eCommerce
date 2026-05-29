# ============================================================
# STAGE 1: BUILD — 用 JDK + Maven 編譯出 .jar
# ============================================================
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline -B -q

COPY src/ src/
RUN ./mvnw package -DskipTests -B -q

# ============================================================
# STAGE 2: RUNTIME — 只用輕量 JRE 執行 jar
# ============================================================
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

COPY --from=builder /app/target/order-service-0.0.1-SNAPSHOT.jar app.jar

# 應用本身用 server.port=0（隨機 port，由 Eureka 註冊）；
# actuator 的 management port 由 docker-compose 設為固定 8081 供 health check 使用。
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
