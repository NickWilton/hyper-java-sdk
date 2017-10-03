package com.guidedsoftware.hyper;

/**
 * Created by nwilton on 25/6/17.
 */
public class HyperException extends Exception {
	public HyperException(String reason) {
		super(reason);
	}
	public HyperException(String reason, Throwable parent) {
		super(reason, parent);
	}
}
