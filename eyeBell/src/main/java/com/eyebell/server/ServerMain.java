package com.eyebell.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eyebell.server.lib.DeviceMapper;
import com.eyebell.server.lib.Receiver;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpPrincipal;
import com.sun.net.httpserver.HttpServer;



@Component("serverApp")
public class ServerMain {
	
	@Autowired
	Receiver receiver;	
	
	//static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
	//static HashMap<String,Session> peers = new HashMap<String,Session>();
	
		
		public void Start() {
			
			System.out.println("config is loaded");
			//startServer();
			org.glassfish.tyrus.server.Server gfserver =
					new org.glassfish.tyrus.server.Server("103.122.168.35", 8025, "/ws", null,Server.class);
		
			HttpServer httpServer = null;
			
		try {
				httpServer = HttpServer.create(new InetSocketAddress(8026), 1);
				HttpContext context = httpServer.createContext("/");
		        context.setHandler(receiver);
				httpServer.start();
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

				
		private static void handleRequest1(HttpExchange exchange) throws IOException 
		{
			System.out.println("-- headers --");
		      Headers requestHeaders = exchange.getRequestHeaders();
		      //requestHeaders.entrySet().forEach(System.out::println);

		      System.out.println("-- principle --");
		      HttpPrincipal principal = exchange.getPrincipal();
		      System.out.println(principal);

		      System.out.println("-- HTTP method --");
		      String requestMethod = exchange.getRequestMethod();
		      System.out.println(requestMethod);

		      System.out.println("-- query --");
		      URI requestURI = exchange.getRequestURI();
		      System.out.println("url path  : "+requestURI.getPath());
		      String query = requestURI.getQuery();
		      System.out.println(query);
		      
		      
		      
	        String response = "Hi there!";
	        exchange.sendResponseHeaders(200, response.getBytes().length);        OutputStream os = exchange.getResponseBody();
	        os.write(response.getBytes());
	        os.close();
	    }
				
}

