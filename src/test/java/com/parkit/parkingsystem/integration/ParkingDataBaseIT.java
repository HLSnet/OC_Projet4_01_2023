package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    public static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    public void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    public static void tearDown(){

    }

    @Test
    public void testParkingACar(){
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        

        Ticket ticket = ticketDAO.getTicket("ABCDEF");
		assertNotNull(ticket);
		
		ParkingSpot parkingSpot = ticket.getParkingSpot();
		
		assertFalse(parkingSpot.isAvailable());
		
		assertThat(parkingSpot.getParkingType()).isEqualByComparingTo(ParkingType.CAR);
        
    }

    @Test
    public void testParkingLotExit(){
        testParkingACar();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();
        
        
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
		assertNotNull(ticket);

		Double priceInDb = ticket.getPrice();

		new FareCalculatorService().calculateFare(ticket);
		
		Double priceToBePaid = ticket.getPrice();

		assertThat(priceInDb).isEqualTo(priceToBePaid);
    }

    @Test
    public void testParkingRecurrentClient(){

    	//***************  1er passage ***********************************************
        System.out.println( "1er passage");
    	// Une voiture entre pour la 1??re fois dans le parking
        testParkingACar();        
        faireUnePause(1);
        
    	// La voiture sort du parking
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();
        faireUnePause(1);
        
        //  On va lire dans la table ticket le statut de ce client (nouveau ou ancien)
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
              
	assertNotNull(ticket);		
		
	// On v??rifie dans la table ticket qu'il est bien enregistr?? comme nouveau client 
	assertFalse(ticket.getRecurringUser());
		
				
    	//***************  2??me passage ***********************************************
        System.out.println( "2??me passage");
    	// La voiture entre pour la 2??me fois dans le parking  
        testParkingACar();
        faireUnePause(1);              
        
    	// La voiture sort du parking
        //parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();        
        faireUnePause(1);
        
        //  On va lire dans la table ticket le statut de ce client (nouveau ou ancien)
        ticket = ticketDAO.getTicket("ABCDEF");

	assertNotNull(ticket);
		
	// On v??rifie dans la table ticket qu'il est enregistr?? dor??navant comme ancien client 
	assertTrue(ticket.getRecurringUser());
    }
   
    private void faireUnePause(int duree) {
	    // Attendre 1 seconde
	    try {
			Thread.sleep(duree * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }    
    
}
