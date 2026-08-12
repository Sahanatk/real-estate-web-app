package com.example.myOwnRealtorWebsite;

import com.example.myOwnRealtorWebsite.model.Agent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableConfigurationProperties(Agent.class)

public class MyOwnRealtorWebsiteApplication {

	public static void main(String[] args) {

		SpringApplication.run(MyOwnRealtorWebsiteApplication.class, args);
//		ApplicationContext ctx = SpringApplication.run(MyOwnRealtorWebsiteApplication.class, args);
//		Agent agent = ctx.getBean(Agent.class);
//		System.out.println("=== AGENT DEBUG ===");
//		System.out.println("Name: " + agent.getName());
//		System.out.println("Bio: " + agent.getBiography());
//		System.out.println("Photo: " + agent.getProfilePictureUrl());
//		System.out.println("===================");
	}

}
