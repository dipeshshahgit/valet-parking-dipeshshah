package com.zendesk.valetparking.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zendesk.valetparking.constants.ValetParkingConstants;
import com.zendesk.valetparking.constants.ValetParkingEventTypes;
import com.zendesk.valetparking.entity.VehicleType;

/**
 * @author dipeshshah
 * Validate File input events
 */
public class EventValidator {

	private static final Logger LOGGER = LoggerFactory.getLogger(EventValidator.class);

	/**
	 * @param event : example ("2 2")
	 * @return true/false
	 */
	public boolean validateCreateValetParkingLotEvent(String event) {

		String[] splitInput = event.split(" ");
		if (splitInput.length == VehicleType.values().length) {
			try {
				for (int index = 0; index < splitInput.length; index++) {

					int numberOfFreeSlots = Integer.parseInt(splitInput[index]);
					if (numberOfFreeSlots < 1) {
						LOGGER.error("Invalid parking capacity" + numberOfFreeSlots + " for : "
								+ VehicleType.values()[index]);
						return false;
					}
				}
				return true;

			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	/**
	 * @param validate event Enter: example ("Enter <VehicleType> <LicenseNumber> <EopchTime>")
	 * @return true/false
	 */
	public boolean validateEnterValetParkingLotEvent(String event) {
		LOGGER.debug("Validating Enter input: " + event);
		String[] splitInput = event.split(" ");
		if (splitInput.length == ValetParkingConstants.ENTER_EVENT_NUMBER_OF_PARAMETERES) {

			try {
				boolean typeExists = false;
				for (VehicleType type : VehicleType.values()) {
					if (splitInput[1].equalsIgnoreCase(type.toString())) {
						typeExists = true;
						break;
					}
				}
				if (!typeExists)
					return false;
				Long epochTime = Long.parseLong(splitInput[3]);
				if (epochTime < 0 || epochTime > Long.MAX_VALUE)
					return false;
				return true;

			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	/**
	 * @param Exit event example "Exit <LicenseNumber> <EpochTime>"
	 */
	public boolean validateExitValetParkingLotEvent(String event) {
		LOGGER.debug("Validating Exit input: " + event);
		String[] splitInput = event.split(" ");
		if (splitInput.length == ValetParkingConstants.EXIT_EVENT_NUMBER_OF_PARAMETERS) {
			try {
				if (!splitInput[0].equalsIgnoreCase(ValetParkingEventTypes.VEHICLE_EXITS_FROM_VALET_PARKING))
					return false;
				Long epochTime = Long.parseLong(splitInput[2]);
				if (epochTime < 0 || epochTime > Long.MAX_VALUE)
					return false;
				return true;

			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
}
