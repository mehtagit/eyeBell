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

import com.eyebell.pojo.Action;
import com.eyebell.pojo.Request;
import com.eyebell.server.lib.DeviceMapper;
import com.eyebell.util.Start;
import com.eyebell.util.Utillity;
import com.google.gson.Gson;

@ServerEndpoint(value = "/chat")
public class Server {

	
	DeviceMapper deviceMapper;
	@Autowired
	Gson gson;
	public Server()
	{
		this.deviceMapper = Start.ctx.getBean(DeviceMapper.class);
	}
	
	@OnOpen
	public void onOpen(Session session) {
		System.out.println(String.format("%s joined the chat room.", session.getId()));
		//ServerMain.peers.add(session);
		System.out.println("total clients [" + deviceMapper.getSessionCount() + "]");
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
			System.out.println("I have received [" + this + "] session [" + session.getId() + "] piID ["+deviceMapper.getPiId(session)+"] message ["+ message + "]");
			System.out.println("size of clients [" + deviceMapper.getSessionCount() + "]");
			Request request = new Gson().fromJson(message, Request.class);
			
					Action action = request.getAction();
					switch (action) 
					{
						case SEND_NOTIFICATION:
							String randonid = request.getData().get("RAND");
							String fmcId = deviceMapper.getFcmId(request);
							//String appid = "dYKGjAQdvbg:APA91bHFMmNubb-6PNPOfff6wPkmoSlYi43kjeirfLDwKpZpjirSqgIFeM_q-UF66mPMoD22k7CW2ZvBs2o_XPXwxLFU3Cwnqt6o3F-xHO6JOgbiAOhw1iYi8xDVvAf54LsAxcxf5ax4";
							//appid=deviceMapper.getMappingDb().get(request.getPiId()).getDeviceId();
							System.out.println("To send notification I have found fcm id ["+fmcId+"] for piid["+request.getPiId()+"]");
							String json_data = "{\"to\":\""+fmcId+"\",\"data\": {\"message\": \"This is a Firebase Cloud Messaging Topic Message!#"+randonid+"\",\"title\":\"IOT\"}}";
							Utillity.sendPost("https://fcm.googleapis.com/fcm/send", json_data, "application/json", 5000, 5000);
						break;
						
						case REGISTER_PI:
							if(deviceMapper.isValidPi(request))
							{
								deviceMapper.saveSession(session, request.getPiId());
								System.out.println("pi ["+request.getPiId()+"] is stored with session ["+session+"]");
							}
							else
							{
								//disconnect this connection
							}
						break;
								
						default:
							System.out.println("cant understatnd the action..!!");
						break;
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
		String piId = deviceMapper.getPiId(session);
		deviceMapper.updateStatusOfPi(piId, "N");
		deviceMapper.removeSession(session);
		
	}

	public void sendMessage(String piId, Request request) 
	{
		String msg = null;
		try 
		{
			msg = gson.toJson(request);
			Session session = deviceMapper.getSessionfromPiId(piId);
			session.getBasicRemote().sendText(msg);
			System.out.println("Msg [" + msg + "] has sent to PI ["+piId+"]");
		} 
		catch (IllegalStateException ise) 
		{
			System.out.println("unable to send Msg [" + msg + "] to PI ["+piId+"]  Exception ["+ise+"]");
		} 
		catch (Exception e) 
		{
			System.out.println("unable to send Msg [" + msg + "] to PI ["+piId+"]  Exception ["+e+"]");
			e.printStackTrace();
		}
	}
	
}