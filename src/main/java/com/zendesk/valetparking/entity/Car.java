package com.zendesk.valetparking.entity;

/**
 * @author dipeshshah
 *
 */
public class Car extends Vehicle {

	public Car(String licenseNumber) {
		super(licenseNumber, VehicleType.Car);
	}
}
