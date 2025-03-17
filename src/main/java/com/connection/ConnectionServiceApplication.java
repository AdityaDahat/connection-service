package com.connection;

import com.connection.security.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ConnectionServiceApplication {

//	@Autowired
//	private ApplicationProperties applicationProperties;

	public static void main(String[] args) {
		SpringApplication.run(ConnectionServiceApplication.class, args);
	}
}
