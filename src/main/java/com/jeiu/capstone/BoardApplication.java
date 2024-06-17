package com.jeiu.capstone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.filter.HiddenHttpMethodFilter;

/** 추후 빌드 및 배포를 위해 SpringBootServletInitializer 상속 */
@SpringBootApplication
@EnableJpaAuditing
public class BoardApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(BoardApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(BoardApplication.class, args);
	}
}

