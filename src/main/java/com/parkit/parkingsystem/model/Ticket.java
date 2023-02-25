package com.parkit.parkingsystem.model;

import java.util.Date;

import org.apache.commons.lang.SerializationUtils;

public class Ticket {
    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price;
    private Date inTime;
    private Date outTime;
	private Boolean recurringUser = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getInTime() {
        return (Date) SerializationUtils.clone(inTime);
    }

    public void setInTime(Date inTime) {
        this.inTime = (Date) SerializationUtils.clone(inTime);
    }

    public Date getOutTime() {
        return (Date) SerializationUtils.clone(outTime);
    }

    public void setOutTime(Date outTime) {
        this.outTime = (Date) SerializationUtils.clone(outTime);
    }
    
	public Boolean getRecurringUser() {
		return recurringUser;
	}

	public void setRecurringUser(Boolean recurringUser) {
		this.recurringUser = recurringUser;
	}    
    
}
