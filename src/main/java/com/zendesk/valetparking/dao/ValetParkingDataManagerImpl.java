package com.zendesk.valetparking.dao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zendesk.valetparking.constants.ValetParkingConstants;
import com.zendesk.valetparking.entity.ParkingSpot;
import com.zendesk.valetparking.entity.Vehicle;
import com.zendesk.valetparking.entity.VehicleType;
import com.zendesk.valetparking.exception.DataLayerException;

/**
 * @author dipeshshah 
 * Implementation of ValetParkingDataManager interface.
 * This Class creates and maintains Data structure for free and allocated parking 
 * lots with different Types of Vehicle ( Car, Motorcycle) 
 * 
 * Notes:
 * newFreeParkingLots is using TreeSet: This data structure maintains a list of free parking slots 
 * and when polled returns free parking slot with lowest parking slot number.
 * 
 * newAllocatedParkingLots: This HashMap contains a mapping of vehicle LicenseNumber as Key and its 
 * ParkingSpot Object as value
 */
public class ValetParkingDataManagerImpl implements ValetParkingDataManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValetParkingDataManagerImpl.class);
	int numberOfTypes = VehicleType.values().length;

	int capacityArray[] = new int[numberOfTypes];
	int pointerArray[] = new int[numberOfTypes];
	@SuppressWarnings("unchecked")
	TreeSet<Integer> newFreeParkingLots[] = new TreeSet[numberOfTypes];
	HashMap<String, ParkingSpot> newAllocatedParkingLots = new HashMap<>();

	/**
	 * @param capacity for given VehicleType
	 * @return true : When capacity of given VehicleType is allocated.
	 */
	@Override
	public boolean createValetParkingLot(int capacity, VehicleType type) throws DataLayerException {

		LOGGER.debug("Creating ValetParkingLot with capacity: " + capacity + ", VehicleType: " + type);
		if (capacity < ValetParkingConstants.MIN_VEHICLE_CAPACITY
				|| capacity > ValetParkingConstants.MAX_VEHICLE_CAPACITY)
			throw new DataLayerException("Current MAX_VEHICLE_CAPACITY supported range between "
					+ ValetParkingConstants.MIN_VEHICLE_CAPACITY + " - " + ValetParkingConstants.MAX_VEHICLE_CAPACITY);

		int vehicleOrdinal = type.ordinal();

		if (newFreeParkingLots[vehicleOrdinal] != null)
			throw new DataLayerException(
					"Can't call createValetParkingLot more than once for same Vehicle Type :" + type.name());

		capacityArray[vehicleOrdinal] = capacity + 1;
		pointerArray[vehicleOrdinal] = 1;
		newFreeParkingLots[vehicleOrdinal] = new TreeSet<>();

		return true;
	}

	/**
	 * @param Vehicle: Object containing registration number of Vehicle and Vehicle type
	 * @param epochEnter: Vehicle entry time in epoch.
	 * @return Either "Accept VehicleLot<Number>" or Reject
	 */
	@Override
	public String enter(Vehicle vehicle, Long epochEnter) throws DataLayerException {

		int vehicleOrdinal = vehicle.getType().ordinal();
		String vehicleTypeName = vehicle.getType().name();
		String vehicleLicenseNumber = vehicle.getLicenseNumber();

		if (vehicleOrdinal >= newFreeParkingLots.length || newFreeParkingLots[vehicleOrdinal] == null)
			throw new DataLayerException("ValetParkingLot for " + vehicleTypeName + "is not initialized");
		else if (newAllocatedParkingLots.containsKey(vehicleLicenseNumber))
			throw new DataLayerException(
					"Car with LicenseNumber " + vehicleLicenseNumber + " already exists in parking lot");
		else if (epochEnter < 0)
			throw new DataLayerException(
					"epochEnter time should not be zero or negative, received input: " + epochEnter);

		int vehiclePosition;

		if (newFreeParkingLots[vehicleOrdinal].size() == 0) {
			if (pointerArray[vehicleOrdinal] == capacityArray[vehicleOrdinal]) {
				return "Reject";
			}
			vehiclePosition = pointerArray[vehicleOrdinal];
			pointerArray[vehicleOrdinal] += 1;
		} else {
			vehiclePosition = newFreeParkingLots[vehicleOrdinal].pollFirst();
		}

		ParkingSpot spot = new ParkingSpot(vehicleTypeName + "Lot" + vehiclePosition);
		spot.assignVehicle(vehicle, epochEnter);
		newAllocatedParkingLots.put(vehicleLicenseNumber, spot);
		return "Accept " + vehicleTypeName + "Lot" + vehiclePosition;
	}

	/**
	 * @param regNumber  : registration number of Vehicle
	 * @param exitEpoch: Vehicle exit time in epoch.
	 * @return: "VehicleLot<Number> <parkingCharges>" depending on Vehicle Type
	 */
	@Override
	public String exit(String regNumber, Long exitEpoch) throws DataLayerException {
		if (!newAllocatedParkingLots.containsKey(regNumber))
			throw new DataLayerException("Car with LicenseNumber " + regNumber + " does not exists in parking lot");

		ParkingSpot spot = newAllocatedParkingLots.remove(regNumber);
		spot.setEpochExit(exitEpoch);

		String message = new String();
		try {
			message = spot.getNumber() + " " + spot.getTicketAmount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		newFreeParkingLots[spot.getVehicle().getType().ordinal()].add(spot.getSpotIntegerNumber());
		spot.removeVehicle();
		return message;
	}

	/**
	 * TODO: For future extension of the application
	 */
	@Override
	public List<String> status() throws DataLayerException {
		// TODO Auto-generated method stub
		// example method-stub, kept for future extension.
		return null;
	}

}