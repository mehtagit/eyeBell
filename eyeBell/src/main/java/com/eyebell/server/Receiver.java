package com.eyebell.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eyebell.pojo.Action;
import com.eyebell.pojo.Request;
import com.eyebell.server.config.Config;
import com.eyebell.server.config.PIInfo;
import com.google.gson.Gson;

public class Receiver extends HttpServlet 
{
	protected void doGet(HttpServletRequest httpRequest, HttpServletResponse response)
	{
		try 
		{
			String data = null;
			String piId = null;
			data = httpRequest.getParameter("data");
			piId = httpRequest.getParameter("deviceId");
			System.out.println("client sending request  data ["+data+"]");			
			Request request = new Gson().fromJson(data, Request.class);
			Action action = request.getAction();
			switch (action) 
			{
			case REGISTER_NEW_DEVICE:
				PIInfo piinfo = new PIInfo();
				piinfo.setAppid("appid");
				piinfo.setDeviceId("deviceid");
				piinfo.setMsisdn("msisdn");
				piinfo.setPiId("piid");
				Config.registerNewDevice(piinfo);
				break;
				
			case CAMERA_OFF:
				
				break;
			default:
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
		return 0;
	}
	
	public int cameraOFF()
	{
		return 0;
	}
}
