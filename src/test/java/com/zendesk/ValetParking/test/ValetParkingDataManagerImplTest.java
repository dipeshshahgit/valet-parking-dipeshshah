package com.zendesk.ValetParking.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.zendesk.valetparking.constants.ValetParkingConstants;
import com.zendesk.valetparking.dao.ValetParkingDataManagerImpl;
import com.zendesk.valetparking.entity.Car;
import com.zendesk.valetparking.entity.Motorcycle;
import com.zendesk.valetparking.entity.Vehicle;
import com.zendesk.valetparking.entity.VehicleType;
import com.zendesk.valetparking.exception.DataLayerException;

public class ValetParkingDataManagerImplTest {

	private ValetParkingDataManagerImpl ValetParkingDataManagerimpl;
	private static Long epochTime = (long) 1600000000;
	private static Long epochTimeAfterAnHour = (long) 1600003600;
	private static Long epochTimeAfterAnHourAndOneMinute = (long) 1600003660;
	private static String licenseNumber = "SPZ1231A";
	private static String licenseNumberAnother = "SPZ1231B";

	@Before
	public void setUp() {
		ValetParkingDataManagerimpl = new ValetParkingDataManagerImpl();
	}

	@Test(expected = DataLayerException.class)
	public void createValetParkingLotAgain() throws DataLayerException {
		ValetParkingDataManagerimpl.createValetParkingLot(1, VehicleType.values()[0]);
		ValetParkingDataManagerimpl.createValetParkingLot(1, VehicleType.values()[0]);
	}

	@Test(expected = DataLayerException.class)
	public void createValetParkingLotZeroCapacity() throws DataLayerException {
		ValetParkingDataManagerimpl.createValetParkingLot(0, VehicleType.values()[0]);
	}

	@Test(expected = DataLayerException.class)
	public void createValetParkingLotOverMaxCapacity() throws DataLayerException {
		ValetParkingDataManagerimpl.createValetParkingLot(Integer.MAX_VALUE, VehicleType.values()[0]);
	}

	@Test
	public void createValetParkingLotNonZeroCapacity() throws DataLayerException {
		ValetParkingDataManagerimpl.createValetParkingLot(ValetParkingConstants.MAX_VEHICLE_CAPACITY,
				VehicleType.values()[0]);
	}

	@Test
	public void enterValetParkingHappyCarCase() throws DataLayerException {
		ValetParkingDataManagerimpl.createValetParkingLot(1, VehicleType.Car);
		Vehicle testVehicle = new Car(licenseNumber);

		String response = ValetParkingDataManagerimpl.enter(testVehicle, epochTime);
		assertEquals("Accept CarLot1", response);
	}

	@Test
	public void enterValetParkingHappyMotorcycleCase() throws DataLayerException {
		ValetParkingDataManagerimpl.createValetParkingLot(1, VehicleType.Motorcycle);
		Vehicle testVehicle = new Motorcycle(licenseNumber);
		String response = ValetParkingDataManagerimpl.enter(testVehicle, epochTime);
		assertEquals("Accept MotorcycleLot1", response);
	}

	@Test(expected = DataLayerException.class)
	public void enterValetParkingInvalidVehicleType1() throws DataLayerException {
		ValetParkingDataManagerimpl.createValetParkingLot(1, VehicleType.Motorcycle);
		Vehicle testVehicle = new Car(licenseNumber);
		ValetParkingDataManagerimpl.enter(testVehicle, epochTime);
	}

	@Test(expected = DataLayerException.class)
	public void enterValetParkingInvalidVehicleType2() throws DataLayerException {
		ValetParkingDataManagerimpl.createValetParkingLot(1, VehicleType.Car);
		Vehicle testVehicle = new Motorcycle(licenseNumber);
		ValetParkingDataManagerimpl.enter(testVehicle, epochTime);
	}

	@Test(expected = DataLayerException.class)
	public void enterValetParkingEnterInvalidEpoch() throws DataLayerException {
		ValetParkingDataManagerimpl.createValetParkingLot(1, VehicleType.Motorcycle);
		Vehicle testVehicle = new Motorcycle(licenseNumber);
		ValetParkingDataManagerimpl.enter(testVehicle, -1L);
	}

	@Test
	public void enterValetParkingAllUsedSlots() throws DataLayerException {
		ValetParkingDataManagerimpl.createValetParkingLot(1, VehicleType.Motorcycle);
		Vehicle testVehicle1 = new Motorcycle(licenseNumber);
		Vehicle testVehicle2 = new Motorcycle(licenseNumberAnother);
		ValetParkingDataManagerimpl.enter(testVehicle1, epochTime);
		String response = ValetParkingDataManagerimpl.enter(testVehicle2, epochTime);
		assertEquals("Reject", response);
	}

	@Test(expected = DataLayerException.class)
	public void enterSameVehicleInValetParkingAgain() throws DataLayerException {
		ValetParkingDataManagerimpl.createValetParkingLot(1, VehicleType.Motorcycle);
		Vehicle testVehicle = new Motorcycle(licenseNumber);
		ValetParkingDataManagerimpl.enter(testVehicle, epochTime);
		ValetParkingDataManagerimpl.enter(testVehicle, epochTime);
	}

	@Test(expected = DataLayerException.class)
	public void exitVehicleInValetParkingBeforeEntering() throws DataLayerException {
		ValetParkingDataManagerimpl.createValetParkingLot(1, VehicleType.Motorcycle);
		ValetParkingDataManagerimpl.exit(licenseNumber, epochTime);
	}

	@Test(expected = DataLayerException.class)
	public void exitVehicleTwiceAfterEntering() throws DataLayerException {
		ValetParkingDataManagerimpl.createValetParkingLot(1, VehicleType.Motorcycle);
		Vehicle testVehicle = new Motorcycle(licenseNumber);
		ValetParkingDataManagerimpl.enter(testVehicle, epochTime);
		String response = ValetParkingDataManagerimpl.exit(testVehicle.getLicenseNumber(), epochTimeAfterAnHour);
		assertEquals("MotorcycleLot1 1", response);
		ValetParkingDataManagerimpl.exit(testVehicle.getLicenseNumber(), epochTimeAfterAnHourAndOneMinute);
	}

	@Test
	public void vehicleParkingTicketVehicleOneHour() throws DataLayerException {
		ValetParkingDataManagerimpl.createValetParkingLot(1, VehicleType.Car);
		Vehicle testVehicle = new Car(licenseNumber);
		String response1 = ValetParkingDataManagerimpl.enter(testVehicle, epochTime);
		assertEquals("Accept CarLot1", response1);
		String response2 = ValetParkingDataManagerimpl.exit(licenseNumber, epochTimeAfterAnHour);
		assertEquals("CarLot1 2", response2);
	}

	@Test
	public void vehicleParkingTicketVehicleOneHourOneMinute() throws DataLayerException {
		ValetParkingDataManagerimpl.createValetParkingLot(1, VehicleType.Car);
		Vehicle testVehicle = new Car(licenseNumber);
		String response1 = ValetParkingDataManagerimpl.enter(testVehicle, epochTime);
		assertEquals("Accept CarLot1", response1);
		String response2 = ValetParkingDataManagerimpl.exit(testVehicle.getLicenseNumber(),
				epochTimeAfterAnHourAndOneMinute);
		assertEquals("CarLot1 4", response2);
	}

	@Test
	public void vehicleParkingTicketVehicleExitTimeLessThanEnterTime() throws DataLayerException {
		ValetParkingDataManagerimpl.createValetParkingLot(1, VehicleType.Car);
		Vehicle testVehicle = new Car(licenseNumber);
		String response1 = ValetParkingDataManagerimpl.enter(testVehicle, epochTime);
		assertEquals("Accept CarLot1", response1);
		String response2 = ValetParkingDataManagerimpl.exit(testVehicle.getLicenseNumber(),
				epochTimeAfterAnHour - 7200);
		assertEquals("CarLot1 0", response2);
	}

	@Test
	public void vehicleParkingTicketVehicleEnterZeroEpochTime() throws DataLayerException {
		ValetParkingDataManagerimpl.createValetParkingLot(1, VehicleType.Car);
		Vehicle testVehicle = new Car(licenseNumber);
		String response1 = ValetParkingDataManagerimpl.enter(testVehicle, 0L);
		assertEquals("Accept CarLot1", response1);
		String response2 = ValetParkingDataManagerimpl.exit(testVehicle.getLicenseNumber(), 1L);
		assertEquals("CarLot1 2", response2);
	}
}
