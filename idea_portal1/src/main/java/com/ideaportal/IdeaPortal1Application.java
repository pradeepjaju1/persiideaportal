package com.ideaportal;

import org.springframework.boot.SpringApplication;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"com.ideaportal"})
public class IdeaPortal1Application {

	public static void main(String[] args) {
		SpringApplication.run(IdeaPortal1Application.class, args);
	}
}
