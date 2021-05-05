package com.zendesk.valetparking.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dipeshshah
 * DataLayerException class
 */
public class DataLayerException extends Exception {

	private static final long serialVersionUID = -5363730528825959895L;
	private static final Logger LOGGER = LoggerFactory.getLogger(DataLayerException.class);

	public DataLayerException(String message) {
		super(message);
		LOGGER.debug("Raising exception from DataLayerException");
	}

	public DataLayerException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataLayerException(Exception exception) {
		super(exception);
	}

}