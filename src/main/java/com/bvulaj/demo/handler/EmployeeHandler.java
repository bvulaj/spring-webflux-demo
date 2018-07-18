package com.bvulaj.demo.handler;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.bvulaj.demo.model.Employee;
import com.bvulaj.demo.repo.EmployeeRepository;

import reactor.core.publisher.Mono;

@Component
public class EmployeeHandler {
  private EmployeeRepository repository;

  public EmployeeHandler(EmployeeRepository repository) {
    this.repository = repository;
  }

  public Mono<ServerResponse> getEmployee(ServerRequest request) {
    return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(repository.findById(request.pathVariable("id")), Employee.class);
  }

  public Mono<ServerResponse> createEmployee(ServerRequest request) {
    return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(repository.saveAll(
              request.bodyToMono(Employee.class)), 
              Employee.class);
  }
  
  public Mono<ServerResponse> updateEmployee(ServerRequest request) {
    Mono<Employee> employee = request.bodyToMono(Employee.class)
      .map(e -> {
          e.setId(request.pathVariable("id")); 
          return e;
      });
        
    return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                repository.saveAll(employee), 
                Employee.class);
  }
  
  public Mono<ServerResponse> deleteEmployee(ServerRequest request) {
    repository.deleteById(request.pathVariable("id"));
    return ServerResponse.noContent().build();
  }
}
