package com.eyebell.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eyebell.pi.input.PiApp;
import com.eyebell.pojo.Action;
import com.eyebell.pojo.Request;
import com.eyebell.server.config.PIInfo;
import com.eyebell.server.lib.DeviceMapper;
import com.eyebell.util.Start;
import com.eyebell.util.Utillity;
import com.google.gson.Gson;

@ServerEndpoint(value = "/chat")
public class Server {

	
	DeviceMapper deviceMapper;
			     //deviceMapper;
	
	public Server()
	{
		this.deviceMapper = Start.ctx.getBean(DeviceMapper.class);
	}
	
	@OnOpen
	public void onOpen(Session session) {
		System.out.println(String.format("%s joined the chat room.", session.getId()));
		ServerMain.peers.add(session);
		System.out.println("total clients [" + ServerMain.peers.size() + "]");
		try {
			session.getBasicRemote().sendText("connected..!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		try {
			System.out.println("Error in communication with " + session.getId());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@OnMessage
	public void onMessage(String message, Session session) throws IOException, EncodeException 
	{
		try 
		{
			System.out.println("I have received [" + this + "] session [" + session.getId() + "] message ["+ message + "]");
			System.out.println("size of clients [" + ServerMain.peers.size() + "]");
			Request request = new Gson().fromJson(message, Request.class);
			
			for (Session peer : ServerMain.peers) 
			{
				if (session.getId().equals(peer.getId())) 
				{
					Action action = request.getAction();
					switch (action) 
					{
						case SEND_NOTIFICATION:
							String randonid = request.getData().get("RAND");
							String appid = "dYKGjAQdvbg:APA91bHFMmNubb-6PNPOfff6wPkmoSlYi43kjeirfLDwKpZpjirSqgIFeM_q-UF66mPMoD22k7CW2ZvBs2o_XPXwxLFU3Cwnqt6o3F-xHO6JOgbiAOhw1iYi8xDVvAf54LsAxcxf5ax4";
							appid=deviceMapper.getMappingDb().get(request.getPiId()).getDeviceId();
							System.out.println("To send notification I have found fcm id ["+appid+"] for piid["+request.getPiId()+"]");
							String json_data = "{\"to\":\""+appid+"\",\"data\": {\"message\": \"This is a Firebase Cloud Messaging Topic Message!#"+randonid+"\",\"title\":\"IOT\"}}";
							Utillity.sendPost("https://fcm.googleapis.com/fcm/send", json_data, "application/json", 5000, 5000);
						break;
						
						case REGISTER_PI:
							String piId = request.getPiId();
							PIInfo piinfo = new PIInfo();
							piinfo.setPiId(piId);
							piinfo.setSession(session);
							System.out.println("devicemapper ["+deviceMapper+"]");
							deviceMapper.getMappingDb().put(piId,piinfo);
						break;
						
						default:
							System.out.println("cant understatnd the action..!!");
						break;
					}
					
					//peer.getBasicRemote().sendText(new Gson().toJson(request));
	
				} 
				else 
				{
				}
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	@OnClose
	public void onClose(Session session) throws IOException, EncodeException {
		System.out.println(String.format("%s left the chat room.", session.getId()));
		ServerMain.peers.remove(session);
		// notify peers about leaving the chat room
		for (Session peer : ServerMain.peers) {
			String msg = "";
			peer.getBasicRemote().sendObject(msg);
		}
	}

	public void sendMessage()
	{
		
	}
}