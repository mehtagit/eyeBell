package com.eyebell.pi.lib;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.eyebell.pi.lib.PiClient;
import com.eyebell.pojo.Action;
import com.eyebell.pojo.Request;
import com.eyebell.util.Utillity;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
//import com.whileidea.pojo.Image;
//import com.whileidea.util.UtilBase64Image;

public class SignalListener implements Runnable {

	@Autowired
	PiClient piClient;

	@Autowired
	@Qualifier("piUtillity")
	com.eyebell.pi.config.Utillity utillity;

	private Boolean isRinged = false;

	public static GpioPinDigitalOutput lightPin = null;
	public void run() {
		Runnable ringCheckedTask = () -> {
			while (true) {
				try {
					if (isRinged) {
						TimeUnit.MINUTES.sleep(1);
						isRinged = false;
					}
					if (!piClient.isConnected()) {
						piClient.connectToServer();
						Request request = new Request();
						request.setAction(Action.REGISTER_PI);
						request.setPiId(piClient.getPiId());
						piClient.sendMessage(request);
						Request req = new Request();
						req.setAction(Action.SEND_NOTIFICATION);
						req.setPiId("p1");
						HashMap map = new HashMap<String,String>();
						map.put("type", "connected");
						req.setData(map);
						piClient.sendMessage(req);
					}
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		};
		new Thread(ringCheckedTask).start();
		System.out.println("<--Pi4J--> GPIO Listen Example ... started.");

		// create gpio controller
		final GpioController gpio = GpioFactory.getInstance();

		// provision gpio pin #02 as an input pin with its internal pull down
		// resistor
		// enabled
		final GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02,
				PinPullResistance.PULL_DOWN);
		lightPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01,"Tube Light",PinState.HIGH);
		lightPin.setShutdownOptions(true, PinState.LOW);
		
		// set shutdown state for this input pin
		myButton.setShutdownOptions(true);

		// create and register gpio pin listener
		myButton.addListener(new GpioPinListenerDigital() {
			// @Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				// display pin state on console
				System.out
						.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState().getValue());
				if (event.getState().getValue() == 1) {

					if (piClient.isConnected()) {
						if (!isRinged) {
							isRinged = true;
							int camId = utillity.cameraON();

							// send notification
							// String json_data =
							// "{\"to\":\"dYKGjAQdvbg:APA91bHFMmNubb-6PNPOfff6wPkmoSlYi43kjeirfLDwKpZpjirSqgIFeM_q-UF66mPMoD22k7CW2ZvBs2o_XPXwxLFU3Cwnqt6o3F-xHO6JOgbiAOhw1iYi8xDVvAf54LsAxcxf5ax4\",\"data\":
							// {\"message\": \"This is a Firebase Cloud
							// Messaging
							// Topic
							// Message!#"+randonid+"\",\"title\":\"IOT\"}}";

							Request req = new Request();
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("RAND", "" + camId);
							map.put("type", "bell");
							req.setData(map);
							req.setAction(com.eyebell.pojo.Action.SEND_NOTIFICATION);
							req.setPiId(piClient.getPiId());
							// send to server

							piClient.sendMessage(req);

						}
					} else {
						System.out.println("pi is not connected");
					}
				}
			}

		});

		System.out.println(" ... complete the GPIO #02 circuit and see the listener feedback here in the console.");

		// keep program running until user aborts (CTRL-C)
		while (true) {
			try {
				Thread.sleep(10000);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		// stop all GPIO activity/threads by shutting down the GPIO controller
		// (this method will forcefully shutdown all GPIO monitoring threads and
		// scheduled tasks)
		// gpio.shutdown(); <--- implement this method call if you wish to
		// terminate the
		// Pi4J GPIO controller
	}

	public void test() {
		// camera on
		int camId = utillity.cameraON();

		// send notification
		// String json_data =
		// "{\"to\":\"dYKGjAQdvbg:APA91bHFMmNubb-6PNPOfff6wPkmoSlYi43kjeirfLDwKpZpjirSqgIFeM_q-UF66mPMoD22k7CW2ZvBs2o_XPXwxLFU3Cwnqt6o3F-xHO6JOgbiAOhw1iYi8xDVvAf54LsAxcxf5ax4\",\"data\":
		// {\"message\": \"This is a Firebase Cloud Messaging Topic
		// Message!#"+randonid+"\",\"title\":\"IOT\"}}";

		Request req = new Request();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("RAND", "" + camId);
		req.setData(map);
		req.setAction(com.eyebell.pojo.Action.SEND_NOTIFICATION);
		req.setPiId(piClient.getPiId());
		// send to server
		piClient.sendMessage(req);
		while (true) {
			try {
				Thread.sleep(10000);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}
}
