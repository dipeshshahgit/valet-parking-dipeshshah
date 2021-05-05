/**
 * 
 */
package com.zendesk.valetparking.entity;

import com.zendesk.valetparking.constants.*;

/**
 * @author dipeshshah 
 * Enum class containing VehicleType and Hourly Vehicle parking Charge
 */
public enum VehicleType {

	Car(2), Motorcycle(1);

	private int price;

	VehicleType(int price) {
		this.price = price;
	}

	public int getHourlyParkingPrice() {
		return this.price;
	}
}
