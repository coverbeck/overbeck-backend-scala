FROM hseeberger/scala-sbt:11.0.9.1_1.4.6_2.13.4 as build
COPY ./ /app
# We use 'set test in assembly := {}' to skip tests specifically for the assembly task
RUN cd /app && sbt 'set test in assembly := {}' clean assembly

FROM openjdk:8u342-slim
COPY --from=build /app/target/scala-2.13/overbeck-backend.jar /
CMD ["java", "-jar", "/overbeck-backend.jar"]