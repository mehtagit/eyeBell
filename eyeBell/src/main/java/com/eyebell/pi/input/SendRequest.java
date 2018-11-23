package com.eyebell.pi.input;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eyebell.pojo.Action;
import com.eyebell.pojo.Request;
import com.google.gson.Gson;

public class SendRequest extends HttpServlet 
{
	protected void doGet(HttpServletRequest httpRequest, HttpServletResponse response)
	{
		try 
		{
			String data = null;
			String piId = null;
			data = httpRequest.getParameter("data");
			piId = httpRequest.getParameter("piId");
			System.out.println("client sending request  data ["+data+"]");			
			Request request = new Gson().fromJson(data, Request.class);
			Action action = request.getAction();
			switch(action)
			{
			case SEND_NOTIFICATION:
				PiApp.client.sendMessage(request);
				System.out.println("data send from client ["+data+"]");
				break;
			case CAMERA_ON:
				int O_ID = cameraON();
				response.getWriter().write(O_ID);
				break;
			case CAMERA_OFF:
				int C_ID = cameraON();
				response.getWriter().write(C_ID);
				break;
			default:
				System.out.println("action not found");
				break;
			}
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	
	public int cameraON()
	{
		int rand = (int)Math.random();
		System.out.println("camera is set to ON with id = "+rand);
		open2("https://103.206.248.236:8443/NOW.html?home="+rand);
		return rand;
	}
	public void open2(String args) {
		String url = args;// "http://www.google.com";
		String os = System.getProperty("os.name").toLowerCase();
		System.out.println("OS found is : " + os);
		Runtime rt = Runtime.getRuntime();
		try {
			if (os.indexOf("win") >= 0) {
				// this doesn't support showing urls in the form of "page.html#nameLink"
				rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
			} else if (os.indexOf("mac") >= 0) {
				rt.exec("open " + url);
			} else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {
				// Do a best guess on unix until we get a platform independent way
				// Build a list of browsers to try, in this order.
				String[] browsers = { "chromium", "mozilla" };
				// Build a command string which looks like "browser1 "url" || browser2 "url"
				// ||..."
				StringBuffer cmd = new StringBuffer();
				for (int i = 0; i < browsers.length; i++) {
					cmd.append((i == 0 ? "" : " || ") + browsers[i] + " \"" + url + "\" ");
				}
				System.out.println("command formed : " + cmd);
				rt.exec(new String[] { "sh", "-c", cmd.toString() });
			} else {
				System.out.println("mo OS found");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		return;
	}

	public int cameraOFF()
	{
		System.out.println("camera is now off");
		return 0;
	}
}
