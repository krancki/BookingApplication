FROM openjdk:14
ADD target/bookingapp-0.0.1-SNAPSHOT.jar booking-app-0.0.1.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "booking-app-0.0.1.jar"]