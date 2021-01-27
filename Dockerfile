FROM hseeberger/scala-sbt:11.0.9.1_1.4.6_2.13.4 as build
COPY ./ /app
RUN cd /app && sbt assembly

FROM openjdk:8u212-b04-jre-slim-stretch
COPY --from=build /app/target/scala-2.13/overbeck-backend.jar /
CMD ["java", "-jar", "/overbeck-backend.jar"]
