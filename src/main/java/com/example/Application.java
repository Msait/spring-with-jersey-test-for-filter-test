package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.example")
public class Application {

	@Bean
	public UserService userService() {
		return new UserService();
	}

	/*public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}*/
}
