package com.eyebell.server.lib;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.eyebell.server.config.PIInfo;

@Component
public class DeviceMapper 
{

	private HashMap<String , PIInfo> mappingDb = new HashMap<>();
	private HashMap <String,String> piToApp = new HashMap<String,String>();
	private HashMap <String,String> appToPi = new HashMap<String,String>();
	
	public HashMap<String, PIInfo> getMappingDb() {
		return mappingDb;
	}

	public void setMappingDb(HashMap<String, PIInfo> mappingDb) {
		this.mappingDb = mappingDb;
	}

	public HashMap<String, String> getPiToApp() {
		return piToApp;
	}

	public void setPiToApp(HashMap<String, String> piToApp) {
		this.piToApp = piToApp;
	}

	public HashMap<String, String> getAppToPi() {
		return appToPi;
	}

	public void setAppToPi(HashMap<String, String> appToPi) {
		this.appToPi = appToPi;
	}

	public boolean mapDeviceToPi(String request)
	{
		return true;
	}
}
