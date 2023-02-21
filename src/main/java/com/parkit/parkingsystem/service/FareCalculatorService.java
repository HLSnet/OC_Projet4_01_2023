package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.DiscountRate;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        float discountRate = (ticket.getRecurringUser() == true) ?(1-DiscountRate.DISCOUNT_RATE) : DiscountRate.NO_DISCOUNT ;  
        
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
        
    	if ( duration < 0.5) { duration = 0;} 
        
        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * discountRate  * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * discountRate  * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}