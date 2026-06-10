package com.todo.todomanagementsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.todo.todomanagementsystem.mapper")
public class TodoManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoManagementSystemApplication.class, args);
	}

}
