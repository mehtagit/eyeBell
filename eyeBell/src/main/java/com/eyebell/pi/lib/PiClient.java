package com.eyebell.pi.lib;

import java.net.URI;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import javax.websocket.Session;

import org.glassfish.tyrus.client.ClientManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eyebell.pojo.Request;
import com.google.gson.Gson;


public class PiClient 
{
	
	private String server =null;
	private Session session = null;
	private ClientManager clientManager = null;
	private String piId = null;
	@Autowired
	private Gson gson;
	private boolean isConnected;
	
	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	//public PiClient(){}
	public PiClient(String server, String piId)
	{
		this.server = server;
		this.piId = piId;
		System.out.println("Pi client is created with id ["+piId+"] server ["+server+"]");
	}
	
	public void connectToServer() {
		try {
			clientManager = ClientManager.createClient();
			this.session = clientManager.connectToServer(Client.class, new URI(server));
			System.out.println("pi ["+piId+"] is connected to server ["+server+"] sessionId ["+session.getId()+"]");
			isConnected = true;
		} catch (Exception e) {
			isConnected = false;
			System.out.println("pi ["+piId+"] is unable to connect to server ["+server+"] Exception ["+e+"]");
			e.printStackTrace();
		}
	}

	public void closeResources() {
		try {
			isConnected=false;
			session.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			clientManager.shutdown();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public boolean sendMessage(Request request) 
	{
		String msg = null;
		try 
		{
			msg = gson.toJson(request);
			this.session.getBasicRemote().sendText(msg);
			System.out.println("PI ["+piId+"] has sent msg [" + msg + "]");
			return true;
		} 
		catch (IllegalStateException ise) 
		{
			System.out.println("PI ["+piId+"] unable to send msg [" + msg + "] Exception ["+ise+"]");
			closeResources();
			connectToServer();
			return false;
		} 
		catch (Exception e) 
		{
			System.out.println("PI ["+piId+"] unable to send msg [" + msg + "] Exception ["+e+"]");
			e.printStackTrace();
			return false;
		}
	}
	
	

	public String getPiId() {
		return piId;
	}

	public void setPiId(String piId) {
		this.piId = piId;
	}
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}

		
}
