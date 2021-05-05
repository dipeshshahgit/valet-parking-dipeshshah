package com.zendesk.valetparking.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dipeshshah
 * FactoryClass to create different Vehicle objects based on Type of Vehicle.
 *
 */
public class VehicleFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(VehicleFactory.class);

	public Vehicle getVehicleObject(String licenseNumber, String typeOfVehicle) {

		if (typeOfVehicle.equalsIgnoreCase(VehicleType.Car.name())) {
			LOGGER.debug("Creating " + VehicleType.Car + " Object with licenseNumber: " + licenseNumber);
			return new Car(licenseNumber);
		} else if (typeOfVehicle.equalsIgnoreCase(VehicleType.Motorcycle.name())) {
			LOGGER.debug("Creating " + VehicleType.Motorcycle + " Object with licenseNumber: " + licenseNumber);
			return new Motorcycle(licenseNumber);
		}
		LOGGER.error("Returning Null Object");
		return null;
	}

}
