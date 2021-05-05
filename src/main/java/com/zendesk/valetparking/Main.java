package com.zendesk.valetparking;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zendesk.valetparking.constants.ValetParkingEventTypes;
import com.zendesk.valetparking.exception.ValetParkingServiceException;
import com.zendesk.valetparking.service.ValetParkingService;
import com.zendesk.valetparking.service.ValetParkingServiceImpl;

/**
 *
 * Valet Parking application
 * 
 * @author Dipesh Shah
 * @version 1.0
 * @since 2021-05-05
 */

public class Main {
	private static final ValetParkingService valetParkingService = new ValetParkingServiceImpl();

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws ValetParkingServiceException {
		LOGGER.debug("Initializing Valet Parking Application..");

		List<String> events = Collections.emptyList();

		if (args.length != 1) {
			LOGGER.error("incorrect input params passed: " + args.length + ", Expected: 1");
		} else {
			try {
				events = Files.readAllLines(Paths.get(args[0]), StandardCharsets.UTF_8);
			} catch (IOException e) {
				e.printStackTrace();
			}
			boolean status = valetParkingService.createValetParkingLot(events.remove(0));
			if (status) {
				for (String event : events) {
					if (!executeEvent(event)) {
						LOGGER.error("Invalid input, can't process event! Ignoring: \"" + event + "\"");
					}
				}
			} else {
				System.out.println("Aborting due to error in parsing Parking Lot Capacity due to invalid file content");
			}

		}
		LOGGER.debug("Application Ended..");
	}

	/**
	 * @param event file events ( Enter / Exit followed by other details )
	 * @return true/false based on event processing success/failure
	 */
	private static boolean executeEvent(String event) {
		Boolean status = false;
		try {
			String[] input = event.split(" ");
			if (input.length == 0) {
				System.out.println("Invalid Input");
			}
			switch (input[0]) {
			case ValetParkingEventTypes.VEHICLE_ENTERS_INTO_VALET_PARKING:
				status = valetParkingService.enter(event);
				break;
			case ValetParkingEventTypes.VEHICLE_EXITS_FROM_VALET_PARKING:
				status = valetParkingService.exit(event);
				break;
			default:
				status = false;

			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
			status = false;
		}
		return status;
	}
}
