# BookingApp

Two way to connect this application 
# 1.Local run  

Requirements:
- java 14
- docker

How to run app 
- maven install
- docker-compose up


If you want run local profile:
- docker-compose -f docker-compose-local.yaml up -d 
- maven spring-boot:run
- docker-compose -f docker-compose-local.yaml down

If you want run docker profile(All in docker)
- maven install
- docker-compose up -d
- docker-compose down (STOP)

In this project swagger's a client 
- check http://localhost:8888/swagger-ui.html

# 2.Connect to my server 
Against this you can connect to my website
krancki.ddns.net:8888/swagger-ui.html


# Example request for api

/rooms - GET need value  from=2000-01-10 and to=2000-01-15

/reservations - POST need {"roomId":"1","nickName":"kranki","from":"2000-01-10","to":"2000-01-15"}  

/reservations/{reservationId}/{nickName} - DEL   /reservation/1/krancki

/reservations/{nickName} - GET  return all krancki's reservations

/notifications/{nickName} - GET return all notification where notification is unread and after delivery date

/notifications/{nickName}/all - GET all notification 