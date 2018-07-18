/**
 * 
 */
package com.bvulaj.demo.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.bvulaj.demo.model.Employee;

/**
 * @author bvulaj
 *
 */
public interface EmployeeRepository extends ReactiveMongoRepository<Employee, String>, ReactiveCrudRepository<Employee, String> {

}
