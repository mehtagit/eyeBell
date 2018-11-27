package com.eyebell.pi.lib;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;

@ClientEndpoint
public class Client {

	@OnMessage
	public void receiveMessage(String message) {
		
		System.out.println("from server["+message+"]");
		// Recieve Request from Server It may be ON_CAMERA and OFF_CAMERA
	}

}
