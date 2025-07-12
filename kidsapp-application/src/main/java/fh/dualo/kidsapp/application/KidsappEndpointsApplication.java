package fh.dualo.kidsapp.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class KidsappEndpointsApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(KidsappEndpointsApplication.class, args);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
				.addMapping("/api/**")
				.allowedOrigins("http://localhost:5173", "http://localhost:8080")
				.allowedOriginPatterns("*")//Ich gebe auf alter
				.allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
				.allowCredentials(true);
	}
}
