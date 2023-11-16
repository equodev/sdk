package com.equo.application.client;

public class AppSettings {
	public static final String TEXT_HTML = "text/html";

	private String contentType = TEXT_HTML;

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}
