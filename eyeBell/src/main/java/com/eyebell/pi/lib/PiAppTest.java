package com.eyebell.pi.lib;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.eyebell.pi.lib.PiClient;
import com.eyebell.pi.lib.SignalListener;
import com.eyebell.pojo.Action;
import com.eyebell.pojo.Request;

@Component
public class PiAppTest
{
	
	@Autowired
	private PiClient piClient;
	
	@Autowired
	private SignalListener signalListener;
	

	
	public void start()
	{
		System.out.println("piclient ["+piClient.getPiId()+"] sr ["+signalListener+"]");
		piClient.connectToServer();
		Request request = new Request();
		request.setAction(Action.REGISTER_PI);
		request.setPiId(piClient.getPiId());
		piClient.sendMessage(request);
		//new Thread(signalListener).start();
		try{
		Thread.sleep(20000);
		}catch(Exception e){
		}
		System.out.println("signal receiver init");
		signalListener.test();
		
	}
	
	
	
}
