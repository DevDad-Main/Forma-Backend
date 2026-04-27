package com.devdad.Forma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.devdad.Forma.config.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class) // Needed initialize the custom JWT properties set in app.properties
public class FormaApplication {

	public static void main(String[] args) {
		SpringApplication.run(FormaApplication.class, args);
	}

}
