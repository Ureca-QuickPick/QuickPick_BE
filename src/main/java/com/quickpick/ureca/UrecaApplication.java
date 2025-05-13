package com.quickpick.ureca;

import com.quickpick.ureca.OAuth.auth.config.JwtPropertiesOAuth;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableConfigurationProperties(JwtPropertiesOAuth.class)
public class UrecaApplication {
	public static void main(String[] args) {
		SpringApplication.run(UrecaApplication.class, args);
	}
}