@echo off
java -jar target/order-1.0-SNAPSHOT.jar > startup.log 2>&1
type startup.log