package com.eyebell.pi.lib;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.eyebell.pi.config.Utillity;
import com.eyebell.pojo.Request;
import com.eyebell.util.Start;
import com.google.gson.Gson;

@ClientEndpoint
public class Client {

	@Autowired
	Gson gson;
	Utillity utillity;

	public Client()
	{
		utillity = Start.ctx.getBean(Utillity.class);
	}
	
	@OnMessage
	public void receiveMessage(String message) {
		
		System.out.println("from server["+message+"]");
		
		// Recieve Request from Server It may be ON_CAMERA and OFF_CAMERA
		Request request = null;
		try{
			request = new Gson().fromJson(message, Request.class);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("action ["+request.getAction()+"]");
		switch (request.getAction()) {
		case CAMERA_OFF:
			utillity.cameraOFF();
		break;
			
		case CAMERA_ON:
			utillity.cameraON((String)request.getData().get("RAND"));
		break;
		
		case LIGHT_ON:
			String lightId = "";//(String)request.getData().get("RAND");
			utillity.lightOn(lightId);
		break;
		
		case LIGHT_OFF:
			String lightId1 = "";//(String)request.getData().get("RAND");
			utillity.lightOff(lightId1);
		break;
		
		case RESET_SOFT:
			utillity.soft_reset();
		break;

		default:
			System.out.println("some unknown action received....");
			break;
		}
	}

}
