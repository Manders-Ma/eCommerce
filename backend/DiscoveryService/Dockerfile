# ============================================================
# STAGE 1: BUILD — 用 JDK + Maven 編譯出 .jar
# ============================================================
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# 先複製 Maven wrapper 與 pom.xml，利用 Docker layer 快取：
# 只要 pom.xml 沒變，下載依賴的這層就會被快取，重建時不必重新下載。
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline -B -q

# 再複製原始碼並打包成 fat jar（跳過測試以加快建置）。
COPY src/ src/
RUN ./mvnw package -DskipTests -B -q

# ============================================================
# STAGE 2: RUNTIME — 只用輕量 JRE 執行 jar
# ============================================================
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# 只從 builder 階段複製打包好的 jar，捨棄 JDK 與 Maven 快取，縮小 image。
COPY --from=builder /app/target/discovery-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8010
ENTRYPOINT ["java", "-jar", "app.jar"]
