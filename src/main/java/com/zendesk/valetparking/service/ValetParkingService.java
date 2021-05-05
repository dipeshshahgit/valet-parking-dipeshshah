package com.zendesk.valetparking.service;

import com.zendesk.valetparking.exception.ValetParkingServiceException;

/**
 * @author dipeshshah
 * Interface for service class
 */
public interface ValetParkingService {
	boolean createValetParkingLot(String input) throws ValetParkingServiceException;

	boolean enter(String input) throws ValetParkingServiceException;

	boolean exit(String input) throws ValetParkingServiceException;

	void status() throws ValetParkingServiceException;
}
