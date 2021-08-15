docker network create net-dev
docker run -d -p 127.0.0.1:5432:5432 --name postgres-dev -e POSTGRES_PASSWORD=mrt@2018 --net net-dev postgres
docker run -d -p 5050:5050 --name pgadmin-dev --net net-dev thajeztah/pgadmin4
docker run -d --hostname rabbit-dev --name rabbit-dev -p 15672:15672 -p 5672:5672 rabbitmq:3-management
docker run -d -p 5431:5432 --name postgres-test -e POSTGRES_USER=mrt -e POSTGRES_PASSWORD=mrt -e POSTGRES_DB=mrt-integration-tests postgres


