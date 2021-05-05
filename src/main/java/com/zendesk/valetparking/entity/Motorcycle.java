package com.zendesk.valetparking.entity;

/**
 * @author dipeshshah
 *
 */
public class Motorcycle extends Vehicle {

	public Motorcycle(String licenseNumber) {
		super(licenseNumber, VehicleType.Motorcycle);
	}
}
