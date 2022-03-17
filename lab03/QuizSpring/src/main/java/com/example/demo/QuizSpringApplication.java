package com.example.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class QuizSpringApplication {

	public static void main(String[] args) {
		//SpringApplication.run(QuizSpringApplication.class, args);
	
		RestTemplate restTemplate = new RestTemplate();

		// Send request with GET method and default Headers.
		var url = "https://api.teleport.org/api/countries/iso_alpha2%3APL/admin1_divisions/";
		String result = restTemplate.getForObject(url, String.class);

		System.out.println(result);
	}

}
