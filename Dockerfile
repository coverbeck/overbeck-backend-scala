FROM hseeberger/scala-sbt:8u212_2.13.0_1.2.8 as build
COPY ./ /app
RUN cd /app && sbt assembly

FROM openjdk:8u212-b04-jre-slim-stretch
COPY --from=build /app/target/scala-2.13/*.jar /anagram-micro-service.jar
CMD ["java", "-jar", "/anagram-micro-service.jar"]
