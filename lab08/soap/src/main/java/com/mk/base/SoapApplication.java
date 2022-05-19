package com.mk.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import hibernate.HibernateUtil;

@SpringBootApplication
public class SoapApplication {

	public static void main(String[] args) {
		HibernateUtil.populateDB();
		SpringApplication.run(SoapApplication.class, args);
	}

}
