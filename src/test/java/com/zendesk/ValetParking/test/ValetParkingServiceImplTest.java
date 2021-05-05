package com.zendesk.ValetParking.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.zendesk.valetparking.exception.ValetParkingServiceException;
import com.zendesk.valetparking.service.ValetParkingServiceImpl;

public class ValetParkingServiceImplTest {

	private ValetParkingServiceImpl valetParkingServiceImpl = new ValetParkingServiceImpl();

	@Test
	public void validateCreateParkingLotInputTest() throws ValetParkingServiceException {
		assertTrue(valetParkingServiceImpl.createValetParkingLot("1 2"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void negativeValidateCreateParkingLotInputTest() throws ValetParkingServiceException {
		assertFalse(valetParkingServiceImpl.createValetParkingLot("1 2 3 4 1 2 2"));
	}

	@Test
	public void enterHappyTest() throws ValetParkingServiceException {
		valetParkingServiceImpl.createValetParkingLot("1 2");
		assertTrue(valetParkingServiceImpl.enter("Enter Car SGD123XA 123123123"));
	}

	@Test
	public void exitCarHappyTest() throws ValetParkingServiceException {
		valetParkingServiceImpl.createValetParkingLot("1 2");
		valetParkingServiceImpl.enter("Enter Car SGD123XA 123123123");
		assertTrue(valetParkingServiceImpl.exit("Exit SGD123XA 123123124"));
	}

	@Test
	public void exitMotorcycleHappyTest() throws ValetParkingServiceException {
		valetParkingServiceImpl.createValetParkingLot("1 2");
		valetParkingServiceImpl.enter("Enter Motorcycle SGD123XA 123123123");
		assertTrue(valetParkingServiceImpl.exit("Exit SGD123XA 123123124"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void negativeEnterTest() throws ValetParkingServiceException {
		valetParkingServiceImpl.createValetParkingLot("1 2");
		assertFalse(valetParkingServiceImpl.enter("Enter Car1 SGD123XA 123123123"));

	}

	@Test(expected = IllegalArgumentException.class)
	public void negativeExitTest() throws ValetParkingServiceException {
		valetParkingServiceImpl.createValetParkingLot("1 2");
		assertTrue(valetParkingServiceImpl.enter("Enter Motor SGD123XA 123123123"));
		assertFalse(valetParkingServiceImpl.exit("Exit1 SGD123XA 123123124"));
	}

}
