package com.zendesk.ValetParking.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.zendesk.valetparking.entity.VehicleType;
import com.zendesk.valetparking.validator.EventValidator;

public class EventValidatorTest {

	private EventValidator eventValidator;

	@Before
	public void setUp() {
		eventValidator = new EventValidator();
	}

	@Test
	public void validateCreateValetParkingLotEventTest() {
		assertTrue(eventValidator.validateCreateValetParkingLotEvent("1 2"));
	}

	@Test
	public void validateCreateValetParkingLotEventNegative1Test() {
		assertFalse(eventValidator.validateCreateValetParkingLotEvent("12"));
	}

	@Test
	public void validateCreateValetParkingLotEventMoreThanExpectedLotsTest() {
		String event = "1";
		for (int i = 2; i < VehicleType.values().length + 2; i++) {
			event += " " + i;
		}
		System.out.println(event);
		assertFalse(eventValidator.validateCreateValetParkingLotEvent(event));
	}

	@Test
	public void validateEnterValetParkingLotEventTest() {
		assertTrue(eventValidator.validateEnterValetParkingLotEvent("Enter Car SGX123GG 123123123"));
	}

	@Test
	public void validateEnterValetParkingLotEventInvalidVehicalTest() {
		assertFalse(eventValidator.validateEnterValetParkingLotEvent("Enter Car1 SGX123GG 123123123"));
	}

	@Test
	public void validateEnterValetParkingLotEventInvalidEpochTest() {
		assertFalse(eventValidator.validateEnterValetParkingLotEvent("Enter Car SGX123GG -1"));
	}

	@Test
	public void validateEnterValetParkingLotEventInvalidParamTest() {
		assertFalse(eventValidator.validateEnterValetParkingLotEvent("Enter Car SGX123GG"));
	}

	@Test
	public void validateExitValetParkingLotEventTest() {
		assertTrue(eventValidator.validateExitValetParkingLotEvent("Exit SGX123GG 123123123"));
	}

	@Test
	public void validateExitValetParkingLotEventNegative1Test() {
		assertFalse(eventValidator.validateExitValetParkingLotEvent("Exit1 SGX123GG 123123123"));
	}

	@Test
	public void validateExitValetParkingLotEventNegative2Test() {
		assertFalse(eventValidator.validateExitValetParkingLotEvent("Exit Car SGX123GG 123123123"));
	}
}
