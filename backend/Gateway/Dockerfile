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

COPY --from=builder /app/target/gateway-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
