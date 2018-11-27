package com.eyebell.server.config;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

public class Config 
{
	@Value("${cdr.server.ip}")
	public String cdr_ip;

	private static HashMap<String, PIInfo> piDeviceMapping = new HashMap<String , PIInfo>();
	public void load()
	{
		
	}

	public static void registerNewDevice(PIInfo piinfo)
	{
	//	PIInfo piinfo = new PIInfo();
		piinfo.setDeviceId("deviceid");
		piinfo.setMsisdn("msisdn");
		piinfo.setPiId("piid");
		piDeviceMapping.put("piid", piinfo);
	}
}
