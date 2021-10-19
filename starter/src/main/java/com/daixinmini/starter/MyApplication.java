package com.daixinmini.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(
		scanBasePackages = "com.daixinmini"
)
public class MyApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(MyApplication.class, args);
	}

}
