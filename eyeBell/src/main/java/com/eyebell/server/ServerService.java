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

@Component
public class ServerService {

	@Autowired
	DeviceMapper deviceMapper;
	@Autowired
	Gson gson;
	
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