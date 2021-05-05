package com.zendesk.valetparking.entity;

/**
 * @author dipeshshah
 *
 */
public class ParkingSpot {
	private String number;
	private boolean free;
	private Vehicle vehicle;
	private Long epochEnter;
	private Long epochExit;

	public ParkingSpot(String number) {
		super();
		this.number = number;
		this.free = true;
	}

	/**
	 * @return parking charges based on Vehicle Type, route to nearest hour
	 *         completion example: 1 hour 1 minute will be rounded to 2 hours for
	 *         charge calculation
	 * @throws Exception
	 */
	public int getTicketAmount() throws Exception {
		if (epochExit <= epochEnter)
			return 0;
		float diff = epochExit - epochEnter;
		diff = diff / 3600;
		Long hours = (long) Math.ceil(diff);
		Long totalTicketPrice = hours * vehicle.getType().getHourlyParkingPrice();
		if (totalTicketPrice > Integer.MAX_VALUE)
			throw new Exception("Total Parking ticket Price " + totalTicketPrice + " exceeded Integer.MAX_VALUE of "
					+ Integer.MAX_VALUE);
		return totalTicketPrice.intValue();
	}

	public boolean IsFree() {
		return free;
	}

	public void assignVehicle(Vehicle vehicle, Long epochEnter) {
		this.vehicle = vehicle;
		this.epochEnter = epochEnter;
		free = false;
	}

	public void removeVehicle() {
		this.vehicle = null;
		free = true;
		epochEnter = 0L;
		epochExit = 0L;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public Long getEpochEnter() {
		return epochEnter;
	}

	public void setEpochEnter(Long epochEnter) {
		this.epochEnter = epochEnter;
	}

	public Long getEpochExit() {
		return epochExit;
	}

	public void setEpochExit(Long epochExit) {
		this.epochExit = epochExit;
	}

	@Override
	public String toString() {
		return "ParkingSpot [number=" + number + ", free=" + free + ", vehicle=" + vehicle + ", epochEnter="
				+ epochEnter + ", epochExit=" + epochExit + "]";
	}

	public int getSpotIntegerNumber() {
		String spotId = this.getNumber();
		String[] splitted = spotId.split("Lot");
		return Integer.parseInt(splitted[1]);
	}

}
