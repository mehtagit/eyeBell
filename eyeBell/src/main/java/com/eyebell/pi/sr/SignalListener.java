package com.eyebell.pi.sr;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.eyebell.pi.lib.Client;
import com.eyebell.pi.lib.ClientMain;
import com.eyebell.pojo.Request;
import com.eyebell.util.Utillity;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
//import com.whileidea.pojo.Image;
//import com.whileidea.util.UtilBase64Image;

@Component("listeningBell")
public class SignalListener implements Runnable {
	
	ClientMain client= null;
	
	public SignalListener(ClientMain client)
	{
		this.client = client;
	}

	public void run() {
		System.out.println("<--Pi4J--> GPIO Listen Example ... started.");

		// create gpio controller
		final GpioController gpio = GpioFactory.getInstance();

		// provision gpio pin #02 as an input pin with its internal pull down resistor
		// enabled
		final GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02,
				PinPullResistance.PULL_DOWN);

		// set shutdown state for this input pin
		myButton.setShutdownOptions(true);

		// create and register gpio pin listener
		myButton.addListener(new GpioPinListenerDigital() 
		{
			// @Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				// display pin state on console
				System.out
						.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState().getValue());
				if (event.getState().getValue() == 1)
				{
					// bell is on
					//camera on
					String randonid= Utillity.callUrl("http://127.0.0.1:4444/send?data={\"msisdn\":\"99332\",\"id\":\"1111\",\"action\":\"CAMERA_ON\",\"Data\":{\"piid\":\"abc\"}}\r\n" , 5000, 5000);
					//send notification
					String json_data = "{\"to\":\"dYKGjAQdvbg:APA91bHFMmNubb-6PNPOfff6wPkmoSlYi43kjeirfLDwKpZpjirSqgIFeM_q-UF66mPMoD22k7CW2ZvBs2o_XPXwxLFU3Cwnqt6o3F-xHO6JOgbiAOhw1iYi8xDVvAf54LsAxcxf5ax4\",\"data\": {\"message\": \"This is a Firebase Cloud Messaging Topic Message!#"+randonid+"\",\"title\":\"IOT\"}}";
					
					Request req = new Request();
					HashMap<String,String> map = new HashMap<String,String>();
					map.put("piid", "abc");
					map.put("json_data",json_data);
					map.put("url","https://fcm.googleapis.com/fcm/send ");
					req.setData(map);
					req.setAction(com.eyebell.pojo.Action.SEND_NOTIFICATION);
					req.setId("1111");
					req.setMsisdn("99332");
					client.sendMessage(req);
				}			
				}

		});

		System.out.println(" ... complete the GPIO #02 circuit and see the listener feedback here in the console.");

		// keep program running until user aborts (CTRL-C)
		while (true) {
			try {
				Thread.sleep(10000);
				{
					test();
				}
				Thread.sleep(10000);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		// stop all GPIO activity/threads by shutting down the GPIO controller
		// (this method will forcefully shutdown all GPIO monitoring threads and
		// scheduled tasks)
		// gpio.shutdown(); <--- implement this method call if you wish to terminate the
		// Pi4J GPIO controller
	}

		public void test()
		{
			String randonid= Utillity.callUrl("http://127.0.0.1:4444/send?data={\"msisdn\":\"99332\",\"id\":\"1111\",\"action\":\"CAMERA_ON\",\"Data\":{\"piid\":\"abc\"}}\r\n" , 5000, 5000);
			//send notification
			String json_data = "{\"to\":\"dYKGjAQdvbg:APA91bHFMmNubb-6PNPOfff6wPkmoSlYi43kjeirfLDwKpZpjirSqgIFeM_q-UF66mPMoD22k7CW2ZvBs2o_XPXwxLFU3Cwnqt6o3F-xHO6JOgbiAOhw1iYi8xDVvAf54LsAxcxf5ax4\",\"data\": {\"message\": \"This is a Firebase Cloud Messaging Topic Message!#"+randonid+"\",\"title\":\"IOT\"}}";
			
			Request req = new Request();
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("piid", "abc");
			map.put("json_data",json_data);
			map.put("url","https://fcm.googleapis.com/fcm/send ");
			req.setData(map);
			req.setAction(com.eyebell.pojo.Action.SEND_NOTIFICATION);
			req.setId("1111");
			req.setMsisdn("99332");
			client.sendMessage(req);

		}
}

