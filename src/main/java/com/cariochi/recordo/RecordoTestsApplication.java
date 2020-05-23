package com.cariochi.recordo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RecordoTestsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecordoTestsApplication.class, args);
	}

}
