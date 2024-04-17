# spring-boot-kotlin-tracing-example

### Things todo list

1. Clone this repository: `git clone https://github.com/hendisantika/spring-boot-kotlin-tracing-example.git`
2. Navigate to the folder: `cd spring-boot-kotlin-tracing-example`
3. Start Zipkin Tracer via
   ```shell
   docker run -p 9411:9411 openzipkin/zipkin
   ```
4. Start application with
   ```shell
   ./gradlew clean :bootRun
   ```` 
5. Call test endpoint: http://localhost:8080/test
6. Call actuator endpoint: http://localhost:8080/actuator
7. Open Zipkin UI and query for traces: http://localhost:9411/zipkin/
   
   
