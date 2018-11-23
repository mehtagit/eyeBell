package com.eyebell.pi.lib;

import java.text.SimpleDateFormat;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;

import com.eyebell.pojo.Request;
import com.google.gson.Gson;

@ClientEndpoint
// @ClientEndpoint
public class Client {

	@OnMessage
	public void onMessage(String message) {
		
		System.out.println("from server["+message+"]");
		// Recieve Request from Server It may be ON_CAMERA and OFF_CAMERA
	}

}
