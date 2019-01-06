package com.eyebell.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.eyebell.pi.lib.PiApp;
import com.eyebell.pi.lib.PiAppTest;
import com.eyebell.server.ServerMain;
public class Start
{
	public static ApplicationContext ctx;
	
	
	public static void main(String gg []) throws Exception
	{
		ctx = new ClassPathXmlApplicationContext("springObjectInjection.xml");

		if (gg[0].equals("client"))
		{
			PiApp piApp = ctx.getBean("piApp",PiApp.class);
			System.out.println("piApp in the main ["+piApp+"]");
			piApp.start();
			
		}
		else if (gg[0].equals("ctest"))
		{
			PiAppTest piapp = ctx.getBean("piAppTest",PiAppTest.class);
			piapp.start();
		}
		else if (gg[0].equals("server"))
		{
			ServerMain server = ctx.getBean("serverApp",ServerMain.class);
			System.out.println("ServerMain in the main ["+server+"]");
			server.Start();
		}
	}
}
