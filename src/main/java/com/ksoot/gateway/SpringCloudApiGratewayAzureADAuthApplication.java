package com.ksoot.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
//@EnableWebFlux
//@EnableWebFluxSecurity
public class SpringCloudApiGratewayAzureADAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudApiGratewayAzureADAuthApplication.class, args);
	}

}
