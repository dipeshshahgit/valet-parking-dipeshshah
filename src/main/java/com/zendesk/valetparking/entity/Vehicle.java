package com.zendesk.valetparking.entity;

/**
 * @author dipeshshah
 * Abstract Vehicle parent class
 */
public abstract class Vehicle {
	private String licenseNumber;
	private final VehicleType type;

	public Vehicle(String licenseNumber, VehicleType type) {
		super();
		this.licenseNumber = licenseNumber;
		this.type = type;
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public VehicleType getType() {
		return type;
	}

	@Override
	public String toString() {
		return "Vehicle [licenseNumber=" + licenseNumber + ", type=" + type.name() + "]";
	}

}
