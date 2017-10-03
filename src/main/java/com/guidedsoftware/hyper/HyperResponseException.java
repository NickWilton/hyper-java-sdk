package com.guidedsoftware.hyper;

/**
 * Created by nwilton on 11/7/17.
 */
public class HyperResponseException extends HyperException {

	int code;
	String message;

	public HyperResponseException(int code, String responseMessage) {
		super(code + " " + responseMessage);
		this.code = code;
		this.message = responseMessage;
	}

	public int getStatusCode() {
		return code;
	}

	public String getResponseMessage() {
		return message;
	}
}
