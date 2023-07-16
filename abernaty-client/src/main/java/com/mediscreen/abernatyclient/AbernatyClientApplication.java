package com.mediscreen.abernatyclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.mediscreen")
public class AbernatyClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(AbernatyClientApplication.class, args);
	}

}
