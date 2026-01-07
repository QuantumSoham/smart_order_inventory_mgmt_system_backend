package com.chubb.agent.agent_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients(basePackages = "com.chubb.agent.agent_server.feignclient")
@EnableDiscoveryClient
public class AgentServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgentServerApplication.class, args);
	}

}
