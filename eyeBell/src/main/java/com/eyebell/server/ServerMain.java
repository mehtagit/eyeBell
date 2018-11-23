package com.eyebell.server;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.Session;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.springframework.stereotype.Component;

import com.eyebell.server.config.Config;



@Component("server")
public class ServerMain {
	
	static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
	private org.eclipse.jetty.server.Server server;
		
		public void Start() {
			new Config().load();
			startServer();
		org.glassfish.tyrus.server.Server server =
				new org.glassfish.tyrus.server.Server("127.0.0.1", 8888, "/ws", null,Server.class);
		try {
		     
	            server.start();
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
				server = new Server(5555);
				ServletHandler sh = new ServletHandler();
				server.setHandler(sh);
				sh.addServletWithMapping(Receiver.class, "/send");
				server.start();
				server.join();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
}

