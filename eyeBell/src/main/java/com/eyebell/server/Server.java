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

import com.eyebell.pojo.Action;
import com.eyebell.pojo.Request;
import com.eyebell.util.Utillity;
import com.google.gson.Gson;

@ServerEndpoint(value = "/chat")
public class Server {

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
	public void onMessage(Session session, byte[] data) throws IOException, EncodeException
	// public void onMessage(String message, Session session) throws IOException,
	// EncodeException
	{
		try {
			System.out.println(
					"I have received from thi [" + this + "] session [" + session.getId() + "] message [" + data + "]");
			ByteBuffer buffer = ByteBuffer.wrap(data);
			session.getBasicRemote().sendBinary(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OnMessage
	// public void onMessage(Message message, Session session) throws IOException,
	// EncodeException
	public void onMessage(String message, Session session) throws IOException, EncodeException {
		try {
			System.out.println("I have received from thi [" + this + "] session [" + session.getId() + "] message ["
					+ message + "]");
			System.out.println("size of clients [" + ServerMain.peers.size() + "]");
			Request request = new Gson().fromJson(message, Request.class);

			for (Session peer : ServerMain.peers) {
				if (session.getId().equals(peer.getId())) {

					Action action = request.getAction();
					switch (action) {
					case SEND_NOTIFICATION:
						int randonid = 123456789;
						String json_data = "{\"to\":\"dYKGjAQdvbg:APA91bHFMmNubb-6PNPOfff6wPkmoSlYi43kjeirfLDwKpZpjirSqgIFeM_q-UF66mPMoD22k7CW2ZvBs2o_XPXwxLFU3Cwnqt6o3F-xHO6JOgbiAOhw1iYi8xDVvAf54LsAxcxf5ax4\",\"data\": {\"message\": \"This is a Firebase Cloud Messaging Topic Message!#"+randonid+"\",\"title\":\"IOT\"}}";
						Utillity.sendPost("https://fcm.googleapis.com/fcm/send", json_data, "application/json", 5000, 5000);
						break;

					default:
						break;
					}
					request.setAction(Action.CAMERA_ON);

					peer.getBasicRemote().sendText(new Gson().toJson(request));
	
				} else {
					System.out.println("trying to send to id [" + peer.getId() + "]");
					request.setAction(Action.CAMERA_ON);

					peer.getBasicRemote().sendText(new Gson().toJson(request));
				}
			}
		} catch (Exception e) {
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