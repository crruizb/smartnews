package com.github.cristianrb.smartnews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.SpringVersion;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan
public class SmartnewsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartnewsApplication.class, args);
	}


}
