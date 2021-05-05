package com.zendesk.valetparking.dao;

import java.util.List;

import com.zendesk.valetparking.entity.Vehicle;
import com.zendesk.valetparking.entity.VehicleType;
import com.zendesk.valetparking.exception.DataLayerException;

/**
 * @author dipeshshah
 * Interface for dao
 */
public interface ValetParkingDataManager {

	boolean createValetParkingLot(int capacity, VehicleType type) throws DataLayerException;

	String enter(Vehicle vehicle, Long enterEpoch) throws DataLayerException;

	String exit(String regNumber, Long exitEpoch) throws DataLayerException;

	List<String> status() throws DataLayerException;
}
