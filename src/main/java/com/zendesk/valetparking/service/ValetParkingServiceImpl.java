package com.zendesk.valetparking.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zendesk.valetparking.dao.ValetParkingDataManager;
import com.zendesk.valetparking.dao.ValetParkingDataManagerImpl;
import com.zendesk.valetparking.entity.VehicleFactory;
import com.zendesk.valetparking.entity.VehicleType;
import com.zendesk.valetparking.exception.DataLayerException;
import com.zendesk.valetparking.exception.ValetParkingServiceException;
import com.zendesk.valetparking.validator.EventValidator;

/**
 * @author dipeshshah
 * ValetParkingService Implementation
 */
public class ValetParkingServiceImpl implements ValetParkingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValetParkingServiceImpl.class);
	private EventValidator eventValidator = new EventValidator();
	private final ValetParkingDataManager valetParkingDataManager = new ValetParkingDataManagerImpl();
	private final VehicleFactory vehicleFactory = new VehicleFactory();

	/**
	 * @param example "2 2" creates 2 parking slots for given VehicleTypes ( Car & Motorcycle ) 
	 */
	@Override
	public boolean createValetParkingLot(String event) throws ValetParkingServiceException {

		try {
			if (eventValidator.validateCreateValetParkingLotEvent(event)) {
				LOGGER.debug("Creating Valet parking lots with input " + event);
				String[] capacities = event.split(" ");
				for (int index = 0; index < capacities.length; index++) {
					int maxValetParkingCapacity = Integer.parseInt(capacities[index]);
					if (valetParkingDataManager.createValetParkingLot(maxValetParkingCapacity,
							VehicleType.values()[index])) {
						LOGGER.debug("Created a parking lot with " + maxValetParkingCapacity + " slots");
					} else {
						throw new ValetParkingServiceException("Failed to create parking lot");
					}
				}

			} else {
				throw new IllegalArgumentException("Invalid Input");
			}
		} catch (DataLayerException e) {
			throw new ValetParkingServiceException(e);
		}
		return true;
	}

	/**
	 * @param Enter event example "Enter <VehicleType> <LicenseNumber> <EpochTime>"
	 */
	@Override
	public boolean enter(String event) throws ValetParkingServiceException {
		LOGGER.debug("Vehicle Enters: " + event);
		try {
			if (eventValidator.validateEnterValetParkingLotEvent(event)) {
				String[] enterEvent = event.split(" ");
				String licenseNumber = enterEvent[2];

				String eventPushMessage = valetParkingDataManager.enter(
						vehicleFactory.getVehicleObject(licenseNumber, enterEvent[1]), Long.parseLong(enterEvent[3]));
				System.out.println(eventPushMessage);
			} else {
				throw new IllegalArgumentException("Invalid Input");
			}
		} catch (DataLayerException e) {
			throw new ValetParkingServiceException(e);
		}
		return true;
	}

	/**
	 * @param Exit event example "Exit <LicenseNumber> <EpochTime>"
	 */
	@Override
	public boolean exit(String event) throws ValetParkingServiceException {
		LOGGER.debug("Vehicle exits: " + event);

		try {
			if (eventValidator.validateExitValetParkingLotEvent(event)) {
				String[] enterEvent = event.split(" ");
				String licenseNumber = enterEvent[1];
				Long epochExit = Long.parseLong(enterEvent[2]);
				String eventPushMessage = valetParkingDataManager.exit(licenseNumber, epochExit);
				System.out.println(eventPushMessage);
			} else {
				throw new IllegalArgumentException("Invalid Input");
			}
		} catch (DataLayerException e) {
			throw new ValetParkingServiceException(e);
		}
		return true;
	}

	/**
	 * Method for future extension
	 */
	@Override
	public void status() throws ValetParkingServiceException {
		// TODO Auto-generated method stub
		try {
			valetParkingDataManager.status();
		} catch (DataLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
