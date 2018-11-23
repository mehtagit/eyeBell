package com.eyebell.pi.lib;

import java.net.URI;
import javax.websocket.Session;

import org.glassfish.tyrus.client.ClientManager;
import org.springframework.stereotype.Component;

import com.eyebell.pojo.Action;
import com.eyebell.pojo.Request;
import com.google.gson.Gson;


@Component("client")
public class ClientMain {
	private String SERVER =null;
	private Session session = null;
	private String clientId = null;
	ClientManager client = null;
	
	public void startAndConnect() throws Exception 
	{
		connectToServer();
		System.out.println("My client id ["+getClientId()+"] session id ["+session.getId()+"]");
	}

	public void start()
	{
		
	}
	
	public String getSERVER() {
		return SERVER;
	}

	public void setSERVER(String sERVER) {
		SERVER = sERVER;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void connectToServer() {
		try {
			client = ClientManager.createClient();
			System.out.println("going to connect to "+getSERVER());
			session = client.connectToServer(Client.class, new URI(getSERVER()));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void closeResources() {
		try {
			session.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			client.shutdown();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public boolean sendMessage(Request request) {
		try {

			String msg = new Gson().toJson(request);
			this.session.getBasicRemote().sendText(msg);
			System.out.println("I have sent msg [" + msg + "]");

			return true;
		} catch (IllegalStateException ise) {
			closeResources();
			connectToServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}


}
