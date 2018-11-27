package com.eyebell.server.config;

public class PIInfo {
private String piId;
private String msisdn;
private String deviceId;
private javax.websocket.Session session;

public javax.websocket.Session getSession() {
	return session;
}
public void setSession(javax.websocket.Session session) {
	this.session = session;
}
public String getPiId() {
	return piId;
}
public void setPiId(String piId) {
	this.piId = piId;
}
public String getMsisdn() {
	return msisdn;
}
public void setMsisdn(String msisdn) {
	this.msisdn = msisdn;
}
public String getDeviceId() {
	return deviceId;
}
public void setDeviceId(String deviceId) {
	this.deviceId = deviceId;
}

}
