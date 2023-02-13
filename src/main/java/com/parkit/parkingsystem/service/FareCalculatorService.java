package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        int inDay = ticket.getInTime().getDate();
        int outDay = ticket.getOutTime().getDate();
        
        int inHour = ticket.getInTime().getHours();
        int outHour = ticket.getOutTime().getHours();
        
        int inMin = ticket.getInTime().getMinutes();
        int outMin = ticket.getOutTime().getMinutes();   
        
        
        int durationDays = (outDay - inDay) * 24;        
        int durationHours = outHour - inHour;
        float durationMins = (outMin - inMin)/60.0f;    
                
        float duration =  durationDays + durationHours + durationMins;

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}