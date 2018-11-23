package com.eyebell.server.config;

import java.util.HashMap;

public class Config 
{
	private static HashMap<String, PIInfo> piDeviceMapping = new HashMap<String , PIInfo>();
	public void load()
	{
		
	}

	public static void registerNewDevice(PIInfo piinfo)
	{
	//	PIInfo piinfo = new PIInfo();
		piinfo.setAppid("appid");
		piinfo.setDeviceId("deviceid");
		piinfo.setMsisdn("msisdn");
		piinfo.setPiId("piid");
		piDeviceMapping.put("piid", piinfo);
	}
}
