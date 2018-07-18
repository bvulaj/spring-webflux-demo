package com.bvulaj.demo.model;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Document(collection = "employees")
public class Employee {
  @Id
  private String id;
  @NotNull
  private String name;
  @NotNull
  private String jobTitle;
}
