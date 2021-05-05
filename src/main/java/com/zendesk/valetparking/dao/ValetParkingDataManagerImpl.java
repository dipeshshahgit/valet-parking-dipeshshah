package com.zendesk.valetparking.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

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
 * Creates and maintains Data structure for free and allocated parking lots with Different Types of Vehicle ( Car, Motorcycle)
 * Notes: 
 * freeValetParkingLots is using TreeMap: this allows us to maintain and retrieve lowest number free parking lot.
 * allocatedParkingLots is using LinkedHashMap: this allows us to maintain list of Vehicles in the order of Enter & Exit processed from file
 * regNumberParkingSpotMap us map to maintain and fast retrieval of ParkingSpot of given vehicle registration number.
 */
public class ValetParkingDataManagerImpl implements ValetParkingDataManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValetParkingDataManagerImpl.class);

	HashMap<VehicleType, TreeMap<Integer, ParkingSpot>> freeValetParkingLots = new HashMap<VehicleType, TreeMap<Integer, ParkingSpot>>();
	HashMap<VehicleType, LinkedHashMap<String, ParkingSpot>> allocatedParkingLots = new HashMap<VehicleType, LinkedHashMap<String, ParkingSpot>>();
	HashMap<String, ParkingSpot> regNumberParkingSpotMap = new HashMap<String, ParkingSpot>();

	/**
	 * @param capacity for given VehicleType
	 * @return true : When capacity of given VehicleType is allocated. 
	 */
	@Override
	public boolean createValetParkingLot(int capacity, VehicleType type) throws DataLayerException {

		LOGGER.debug("Creating ValetParkingLot with capacity: " + capacity + ", VehicleType: " + type);
		if (freeValetParkingLots.containsKey(type) || allocatedParkingLots.containsKey(type))
			throw new DataLayerException("Can't call createValetParkingLot twice for same Vehicletype " + type.name());

		if (capacity < ValetParkingConstants.MIN_VEHICLE_CAPACITY
				|| capacity > ValetParkingConstants.MAX_VEHICLE_CAPACITY)
			throw new DataLayerException("Current MAX_VEHICLE_CAPACITY supported range between "
					+ ValetParkingConstants.MIN_VEHICLE_CAPACITY + " - " + ValetParkingConstants.MAX_VEHICLE_CAPACITY);
		TreeMap<Integer, ParkingSpot> freeParkingLotMap = new TreeMap<>();
		LinkedHashMap<String, ParkingSpot> allocatedParkingLot = new LinkedHashMap<>();

		for (int i = 1; i <= capacity; i++) {
			String freeSpotNumber = type + "Lot" + i;
			ParkingSpot spot = new ParkingSpot(freeSpotNumber);
			freeParkingLotMap.put(i, spot);
		}
		freeValetParkingLots.put(type, freeParkingLotMap);
		allocatedParkingLots.put(type, allocatedParkingLot);
		return true;
	}

	/**
	 * @param Vehicle : Object containing registration number of Vehicle and Vehicle type
	 * @param epochEnter: Vehicle entry time in epoch.
	 * @return Either "Accept VehicalLot<Number>" or Reject
	 */
	@Override
	public String enter(Vehicle vehicle, Long epochEnter) throws DataLayerException {

		if (!freeValetParkingLots.containsKey(vehicle.getType()))
			throw new DataLayerException("ValetParkingLot for " + vehicle.getType().name() + "is not initialized");
		if (epochEnter < 0)
			throw new DataLayerException("epochEnter should not be zero or negative, received input: " + epochEnter);
		TreeMap<Integer, ParkingSpot> freeVehicleLot = freeValetParkingLots.get(vehicle.getType());
		LinkedHashMap<String, ParkingSpot> allocatedParkingLot = allocatedParkingLots.get(vehicle.getType());
		if (regNumberParkingSpotMap.containsKey(vehicle.getLicenseNumber()))
			throw new DataLayerException(
					"Car with LicenseNumber " + vehicle.getLicenseNumber() + " already exists in parking lot");
		else if (freeVehicleLot.size() != 0) {
			ParkingSpot LowestFreeParkingSpot = freeVehicleLot.pollFirstEntry().getValue();
			LowestFreeParkingSpot.assignVehicle(vehicle, epochEnter);
			allocatedParkingLot.put(LowestFreeParkingSpot.getNumber(), LowestFreeParkingSpot);
			regNumberParkingSpotMap.put(vehicle.getLicenseNumber(), LowestFreeParkingSpot);
			return "Accept " + LowestFreeParkingSpot.getNumber();
		}
		return "Reject";
	}

	/**
	 * @param regNumber : registration number of Vehicle
	 * @param exitEpoch: Vehicle exit time in epoch.
	 * @return: "VehicalLot<Number>  <parkingCharges>" depending on Vehicle Type 
	 */
	@Override
	public String exit(String regNumber, Long exitEpoch) throws DataLayerException {

		if (!regNumberParkingSpotMap.containsKey(regNumber))
			throw new DataLayerException("Car with LicenseNumber " + regNumber + " does not exists in parking lot");
		ParkingSpot spot = regNumberParkingSpotMap.remove(regNumber);
		spot.setEpochExit(exitEpoch);
		String message = new String();
		try {
			message = spot.getNumber() + " " + spot.getTicketAmount();
		} catch (Exception e) {
			e.printStackTrace();
		}

		LinkedHashMap<String, ParkingSpot> allocatedParkingLot = allocatedParkingLots.get(spot.getVehicle().getType());
		allocatedParkingLot.remove(spot.getNumber());

		TreeMap<Integer, ParkingSpot> freeVehicleLot = freeValetParkingLots.get(spot.getVehicle().getType());
		spot.removeVehicle();
		freeVehicleLot.put(spot.getSpotIntegerNumber(), spot);
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