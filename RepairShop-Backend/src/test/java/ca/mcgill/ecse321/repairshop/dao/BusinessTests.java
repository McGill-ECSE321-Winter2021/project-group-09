package ca.mcgill.ecse321.repairshop.dao;

import ca.mcgill.ecse321.repairshop.model.Business;
import ca.mcgill.ecse321.repairshop.model.TimeSlot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BusinessTests {

    @Autowired
    private BusinessRepository businessRepository;
    @Autowired
    private TimeSlotRepository timeSlotRepository; //???

    @AfterEach

    public void clearDatabase() {
        businessRepository.deleteAll();
        timeSlotRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadBusiness() {

        //Create 1st TimeSlot vacation (vacation1)
        TimeSlot vacation = new TimeSlot();

        Timestamp startDateTime = Timestamp.valueOf("2021-10-15 00:00:00.00");
        Timestamp endDateTime = Timestamp.valueOf("2021-10-30 23:02:03.00");

        vacation.setStartDateTime(startDateTime);
        vacation.setEndDateTime(endDateTime);

        //Create 2nd TimeSlot vacation (vacation2)
        TimeSlot vacation2 = new TimeSlot();

        Timestamp startDateTime2 = Timestamp.valueOf("2021-12-20 00:00:00.00");
        Timestamp endDateTime2 = Timestamp.valueOf("2021-12-31 23:02:03.00");

        vacation2.setStartDateTime(startDateTime2);
        vacation2.setEndDateTime(endDateTime2);

        //Create List<TimeSlot> vacationsList
        List<TimeSlot> vacationsList = new ArrayList<>();
        vacationsList.add(vacation);
        vacationsList.add(vacation2);

        //TODO: I'm not sure if I should save the TimeSlots vacation & vacation2.
        timeSlotRepository.save(vacation);
        timeSlotRepository.save(vacation2);

        int businessId = 1;
        String name = "TestBusinessName";
        String email = "example@server.ca";
        String phoneNumber = "(123)-456-789";
        int numberOfRepairSpots = 10;
        Business business = new Business();
        business.setName(name);
        business.setEmail(email);
        business.setPhoneNumber(phoneNumber);
        business.setNumberOfRepairSpots(10);
        business.setVacations(vacationsList);

        businessRepository.save(business);

        business = null;

        business = businessRepository.findBusinessByName(name);
        assertNotNull(business);
        assertEquals(businessId, business.getBusinessID());
        assertEquals(name, business.getName());
        assertEquals(email, business.getEmail());
        assertEquals(phoneNumber, business.getPhoneNumber());
        assertEquals(numberOfRepairSpots, business.getNumberOfRepairSpots());
        assertEquals(vacationsList, business.getVacations());
    }

}
