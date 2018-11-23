package com.eyebell.util;

import org.eclipse.jetty.server.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
/* @PropertySource("Config.properties") */
public class Config {

	/*
	 * @Value("#{'${ucip.live.circle}'.split(',')}") public List<String>
	 * liveCircles;
	 */

	@Value("${cdr.server.ip}")
	public String cdr_ip;

	@Bean
	public Server server() {
		return new Server(4444);
	}

	@Bean
	public Server server1() {
		return new Server(4444);
	}
}
