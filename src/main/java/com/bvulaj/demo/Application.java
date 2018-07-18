package com.bvulaj.demo;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.time.Duration;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.bvulaj.demo.handler.EmployeeHandler;
import com.bvulaj.demo.model.Employee;
import com.bvulaj.demo.repo.EmployeeRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class Application {

  @Autowired
  private EmployeeRepository repo;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public RouterFunction<ServerResponse> employeeRoutes(EmployeeHandler handler, EmployeeRepository repository) {
    // @formatter:off
    return route(
            GET("/employees/{id}")
            .and(
                
              accept(APPLICATION_JSON)), 
            handler::getEmployee) // Utilizing a Handler
        
          .andRoute(
              GET("/employees")
              .and(
                accept(APPLICATION_JSON)),
              request -> ServerResponse.ok() // Utilizing a lambda
                        .contentType(APPLICATION_JSON)
                        .body(repository.findAll(), Employee.class))
          
          .andRoute(
              GET("/employees")
              .and(
                accept(TEXT_EVENT_STREAM)), // Will stream data rather than one bulk response
              request -> ServerResponse.ok()
                        .contentType(MediaType.TEXT_EVENT_STREAM)
                        .body(repository.findAll().delayElements(Duration.ofMillis(Long.valueOf(request.queryParam("rate").orElse("0")))), Employee.class)) 
          
          .andRoute(
              POST("/employees")
              .and(
                contentType(APPLICATION_JSON)), 
              handler::createEmployee)
          
          .andRoute(
              PUT("/employee/{id}")
              .and(
                accept(APPLICATION_JSON)), 
              handler::updateEmployee)
          
          .andRoute(
              DELETE("/employee/{id}")
              .and(
                accept(APPLICATION_JSON)), 
              handler::deleteEmployee)
          
          .filter((request, next) -> {
              log.info("{} request to {}", request.methodName(), request.path());
              return next.handle(request);
          });
    // @formatter:on
  }
}
