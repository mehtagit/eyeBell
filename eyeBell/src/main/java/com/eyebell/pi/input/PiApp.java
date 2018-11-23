package com.eyebell.pi.input;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.glassfish.grizzly.http.server.Request;

import com.eyebell.pi.lib.*;
import com.eyebell.pi.sr.SignalListener;
import com.eyebell.util.Utillity;
import com.google.gson.Gson;
@Component
public class PiApp {
	
	@Autowired
	@Qualifier("server")
	private Server server;
	
	public static ClientMain client;
	
	public ClientMain getClient() {
		return client;
	}

	public void setClient(ClientMain client) {
		this.client = client;
	}

	public void start()
	{
		initClient();
		startServer();
		new Thread(new SignalListener(client)).start();
		Utillity.callUrl("http://127.0.0.1:4444/connect", 5000, 5000);
	}
	
	public void startServer()
	{
		try 
		{
			ServletHandler sh = new ServletHandler();
			server.setHandler(sh);
			sh.addServletWithMapping(Connect.class, "/connect");
			sh.addServletWithMapping(SendRequest.class, "/send");
			server.start();
			server.join();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	public static void main(String gg[])
	{
		com.eyebell.pojo.Request req = new com.eyebell.pojo.Request();
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("piid", "abc");
		req.setData(map);
		req.setAction(com.eyebell.pojo.Action.CAMERA_ON);
		req.setId("1111");
		req.setMsisdn("99332");
		System.out.println(new Gson().toJson(req));
	}
	public void initClient()
	{
		client = new ClientMain();
		client.setClientId("Pi_1");
		client.setSERVER("ws://127.0.0.1:8888/ws/chat");
		System.out.println("client init  "+client.getSERVER());
	}
}
