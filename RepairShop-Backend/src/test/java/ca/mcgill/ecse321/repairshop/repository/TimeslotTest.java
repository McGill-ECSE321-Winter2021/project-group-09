package ca.mcgill.ecse321.repairshop.repository;

import ca.mcgill.ecse321.repairshop.model.Technician;
import ca.mcgill.ecse321.repairshop.model.TimeSlot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class TimeslotTest {

    @Autowired
    TimeSlotRepository timeslotRepo;

    @Autowired
    AppointmentRepository appointmentRepo;

    @Autowired
    TechnicianRepository technicianRepo;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        timeslotRepo.deleteAll();
        appointmentRepo.deleteAll();
        technicianRepo.deleteAll();
    }

    @Test
    public void testCreateTimeslot() {

        Timestamp endDate = Timestamp.valueOf("2021-03-01 10:00:00");
        Timestamp startDate = Timestamp.valueOf("2021-03-01 9:00:00");
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setEndDateTime(endDate);
        timeSlot.setStartDateTime(startDate);
        Long newTsID = timeslotRepo.save(timeSlot).getTimeSlotID();

        Optional<TimeSlot> foundTimeslot;
        foundTimeslot = timeslotRepo.findById(newTsID);

        assertNotNull(foundTimeslot);
        assertTrue(foundTimeslot.isPresent());
        assertEquals(endDate, foundTimeslot.get().getEndDateTime());
        assertEquals(startDate, foundTimeslot.get().getStartDateTime());
    }

    @Test
    public void testDeleteAssociatedTechnician() {
        //create technician
        String techEmail = "CustomerEmail";
        Technician tech = new Technician();
        tech.setEmail(techEmail);

        //create timeslot with tech associated
        Timestamp endDate = Timestamp.valueOf("2021-03-01 10:00:00");
        Timestamp startDate = Timestamp.valueOf("2021-03-01 9:00:00");
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setEndDateTime(endDate);
        timeSlot.setStartDateTime(startDate);
        Long newTsID = timeslotRepo.save(timeSlot).getTimeSlotID();
        LinkedList<TimeSlot> timeslots = new LinkedList<>();
        timeslots.add(timeSlot);
        tech.setTimeslots(timeslots);

        //save tech
        technicianRepo.save(tech);

        //test appointment was created as expected
        Optional<TimeSlot> foundTimeslot;
        foundTimeslot = timeslotRepo.findById(newTsID);

        assertNotNull(foundTimeslot);
        assertTrue(foundTimeslot.isPresent());
        assertEquals(endDate, foundTimeslot.get().getEndDateTime());
        assertEquals(startDate, foundTimeslot.get().getStartDateTime());

        //delete technician
        technicianRepo.deleteById(techEmail);
        //ensure the deletion cascaded
        foundTimeslot = timeslotRepo.findById(newTsID);
        assertFalse(foundTimeslot.isPresent());
    }
}
