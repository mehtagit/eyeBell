package com.eyebell.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.eyebell.pi.input.PiApp;
import com.eyebell.pi.lib.ClientMain;
import com.eyebell.server.ServerMain;
public class Start
{
	public static ApplicationContext ctx;
	public static void main(String gg []) throws Exception
	{
		ctx = new ClassPathXmlApplicationContext("springObjectInjection.xml");
		if (gg[0].equals("client"))
		{
			/*
			ClientMain client = ctx.getBean("client",ClientMain.class);
			client.Start();*/
			PiApp piApp = ctx.getBean("piApp",PiApp.class);
			piApp.start();
			
		}
		else if (gg[0].equals("server"))
		{
			ServerMain server = ctx.getBean("server",ServerMain.class);
			server.Start();
		}
	}
}
