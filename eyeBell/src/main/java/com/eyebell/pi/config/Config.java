package com.eyebell.pi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eyebell.pi.lib.PiClient;
import com.eyebell.pi.lib.SignalListener;

@Configuration
public class Config 
{
	
	@Bean
	public PiClient piClient()
	{
		PiClient c = new PiClient("ws://103.206.248.236:8025/ws/chat","p1");
		//c.setServer("ws://127.0.0.1:8025/ws/chat");
		//c.setPiId("p1");
		System.out.println("object created pf piclient ["+c.getServer()+"] ["+c.getPiId()+"]");
		return c;
	}
	
	@Bean
	public SignalListener signalListener()
	{
		return new SignalListener();
	}
}
