package com.eyebell.pojo;

public enum Action {
	SEND_NOTIFICATION("SEND_NOTIFICATION"), CAMERA_ON("CAMERA_ON"),
	CAMERA_OFF("CAMERA_OFF"), REGISTER_NEW_DEVICE("REGISTER_NEW_DEVICE"),
	REGISTER_PI("REGISTER_PI");

	private String url;

	Action(String envUrl) {
		this.url = envUrl;
	}

	public String getUrl() {
		return url;
	}
}
