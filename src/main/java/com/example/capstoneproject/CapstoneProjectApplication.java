package com.example.capstoneproject;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@OpenAPIDefinition(
		servers = {
				@Server(url = "https://cvbuilder-api.monoinfinity.net", description = "Default Server URL")
		}
)
@SpringBootApplication
public class CapstoneProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapstoneProjectApplication.class, args);
	}

}
