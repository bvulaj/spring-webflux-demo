# spring-webflux-demo
Playground and examples for Spring Webflux and related projects

## Functional, Reactive Endpoint Declaration
### Using the HandlerFunction Functional Interfaces
See [RouterConfiguration.java](src/main/java/com/bvulaj/demo/RouterConfiguration.java#L44)

```java
// RouterConfiguration.java
route(
  GET("/employees/{id}")
  .and(
    accept(APPLICATION_JSON)), 
  handler::getEmployee)
```
And [EmployeeHandler.java](src/main/java/com/bvulaj/demo/handler/EmployeeHandler.java#L21)

```java
// EmployeeHandler.java
public Mono<ServerResponse> getEmployee(ServerRequest request) {
  return ServerResponse
          .ok()
          .contentType(MediaType.APPLICATION_JSON)
          .body(repository.findById(request.pathVariable("id")), Employee.class);
}
```

### Using Lambda Function Declaration
See [RouterConfiguration.java](src/main/java/com/bvulaj/demo/RouterConfiguration.java#L50)

```java
// RouterConfiguration.java
route(
  GET("/employees")
  .and(
    accept(APPLICATION_JSON)),
  request -> ServerResponse.ok() // Utilizing a lambda
            .contentType(APPLICATION_JSON)
            .body(repository.findAll(), Employee.class))
```

## Example Queries
Get all employees

```
$ curl localhost:8080/employees
```
Get all employees, streamed

```
$ curl localhost:8080/employees -H "Accept: text/event-stream"
```
Get all employees, streamed with back pressure. One every 2000ms.

```
$ curl localhost:8080/employees?rate=2000 -H "Accept: text/event-stream"
```
Create a new employee

```
$ curl localhost:8080/employees -H "Content-Type: application/json" -X POST -d '{"name":"Brandon Vulaj", "jobTitle":"Software Developer"}'
```
Get an employee by ID

```
$ curl localhost:8080/employees/5b4f9ecc04f26007a3efffa6
```

## Using the Reactive WebClient
WIP