package com.thacbao.neki;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NekiApplication {

	public static void main(String[] args) {
		SpringApplication.run(NekiApplication.class, args);
	}

}
