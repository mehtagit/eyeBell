package com.eyebell.pojo;

import java.util.HashMap;

import org.springframework.stereotype.Component;

@Component
public class Request {

	private String msisdn;
	private String piId;
	private Action action;
	private HashMap<String,String> data;

	public String getMsisdn() {
		return msisdn;
	}

	public HashMap<String, String> getData() {
		return data;
	}

	public void setData(HashMap<String, String> data) {
		this.data = data;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getPiId() {
		return piId;
	}

	public void setPiId(String piId) {
		this.piId = piId;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	@Override
	public String toString() {
		return "Request [msisdn=" + msisdn + ", pid=" + piId + ", action=" + action + "] data ["+data+"]";
	}

	
}
