package com.eyebell.pojo;

import java.util.HashMap;

import org.springframework.stereotype.Component;

@Component
public class Request {

	private String msisdn;
	private String id;
	private Action action;
	private HashMap<String,String> Data;

	public String getMsisdn() {
		return msisdn;
	}

	public HashMap<String, String> getData() {
		return Data;
	}

	public void setData(HashMap<String, String> data) {
		Data = data;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	@Override
	public String toString() {
		return "Request [msisdn=" + msisdn + ", id=" + id + ", action=" + action + "]";
	}

	
}
