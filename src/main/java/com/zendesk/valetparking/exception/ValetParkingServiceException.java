package com.zendesk.valetparking.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dipeshshah
 * ValetParkingServiceException class
 */
public class ValetParkingServiceException extends Exception {
	private static final long serialVersionUID = 502330855020526284L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ValetParkingServiceException.class);

	public ValetParkingServiceException(String message) {
		super(message);
		LOGGER.debug("Raising exception from ValetParkingServiceException");
	}

	public ValetParkingServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValetParkingServiceException(Exception exception) {
		super(exception);
	}

}
