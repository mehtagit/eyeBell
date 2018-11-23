package com.eyebell.server;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.Session;

import org.springframework.stereotype.Component;

import com.eyebell.server.config.Config;



@Component("serverApp")
public class ServerMain {
	
	static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
	
		
		public void Start() {
			new Config().load();
			System.out.println("config is loaded");
			//startServer();
			System.out.println("jetty server started....");
			org.glassfish.tyrus.server.Server gfserver =
					new org.glassfish.tyrus.server.Server("127.0.0.1", 8025, "/ws", null,Server.class);
		try {
		     
			System.out.println("Initialized server");
	            gfserver.start();
	            System.out.println("Press any key to stop the server..");
	            waitHere();
	            //new Scanner(System.in).nextLine();
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        } finally {
	       //     server.stop();
	        }
	}
		
		public synchronized void waitHere() throws InterruptedException
        {
        	wait();
        }

		public void startServer()
		{
			try 
			{
				org.eclipse.jetty.server.Server jettyserver = new org.eclipse.jetty.server.Server(5555);
				org.eclipse.jetty.servlet.ServletHandler sh = new org.eclipse.jetty.servlet.ServletHandler();
				jettyserver.setHandler(sh);
				sh.addServletWithMapping(Receiver.class, "/send");
				jettyserver.start();
			//	server.join();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
}

