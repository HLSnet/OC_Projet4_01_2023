package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.DiscountRate;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    @BeforeAll
    public static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    public void setUpPerTest() {
        ticket = new Ticket();
    }

    @Test
    public void calculateFareCar(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareBike(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareUnkownType(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithFutureInTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice() );
    }

    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (0.75 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithMoreThanADayParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  24 * 60 * 60 * 1000) );//24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (24 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

    @ParameterizedTest(name = "Le prix pour une dur??e de {0} min doit ??tre ??gal ?? 0")
    @ValueSource(ints = {0, 15,29})
    public void calculateFareBikeWithLessThanHalfAnHourParkingTime(int arg){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  arg * 60 * 1000) );// number of minutes < 30 parking time should give 0 parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((0.0), ticket.getPrice() );
    }    

    
    @Test
    public void calculateFareBikeWithHalfAnHourParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - ( 30 * 60 * 1000) );//30 minutes parking time should give 1/2 parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((0.5 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice() );
    }
    
    
    @ParameterizedTest(name = "Le prix pour une dur??e de {0} min doit ??tre ??gal ?? 0")
    @ValueSource(ints = {0, 15,29})
    public void calculateFareCarWithLessThanHalfAnHourParkingTime(int arg){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  arg * 60 * 1000) );// number of minutes < 30 parking time should give 0 parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (0.0) , ticket.getPrice());
    }    
    
    @Test
    public void calculateFareCarWithHalfAnHourParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - ( 30 * 60 * 1000) );//30 minutes parking time should give 1/2 parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((0.5 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice() );
    }    

    
    @Test
    public void calculateFareCarForARegisteredCar(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  10 * 60 * 60 * 1000) ); // 10 heures de stationnement par exemple
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setRecurringUser(true);  // On d??clare comme client r??current donc devant b??n??ficier de la r??duction de 5%
        fareCalculatorService.calculateFare(ticket);
        assertEquals(((1 - DiscountRate.DISCOUNT_RATE) * 10 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice() );        
    } 
    
    
    @Test
    public void calculateFareCarForANotRegisteredCar(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  10 * 60 * 60 * 1000) ); // 10 heures de stationnement par exemple
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setRecurringUser(false);  // On d??clare comme nouveau client donc ne b??n??ficiant pas de la r??duction de 5%
        fareCalculatorService.calculateFare(ticket);
        assertEquals((DiscountRate.NO_DISCOUNT * 10 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice() );        
    }     
    
    
    @Test
    public void calculateFareBikeForARegistereBike(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  10 * 60 * 60 * 1000) ); // 10 heures de stationnement par exemple
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setRecurringUser(true);  // On d??clare comme client r??current donc devant b??n??ficier de la r??duction de 5%
        fareCalculatorService.calculateFare(ticket);
        assertEquals(((1 - DiscountRate.DISCOUNT_RATE) * 10 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice() );        
    } 
    
    
    @Test
    public void calculateFareBikeForANotRegisteredBike(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  10 * 60 * 60 * 1000) ); // 10 heures de stationnement par exemple
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setRecurringUser(false);  // On d??clare comme nouveau client donc ne b??n??ficiant pas de la r??duction de 5%
        fareCalculatorService.calculateFare(ticket);
        assertEquals((DiscountRate.NO_DISCOUNT * 10 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice() );        
    }      
    
}
