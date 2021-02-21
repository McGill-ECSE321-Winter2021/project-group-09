package ca.mcgill.ecse321.repairshop.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.mcgill.ecse321.repairshop.model.Technician;
import ca.mcgill.ecse321.repairshop.model.TimeSlot;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class TechnicianTest {

	@Autowired
	private TechnicianRepository technicianRepository;
	
	
	@BeforeEach
	@AfterEach
	public void clearDatabase() {
		technicianRepository.deleteAll();
	}
	

	@Test
	public void testPersistAndLoadTechnician() {
		
		//create technician
		String techName = "TestCustomer";
		String techEmail = "CustomerEmail";
		String techPassword = "CustomerPassword";
		String techAddress = "ABCD";
		String techPhone = "63534525453";
		Technician tech = new Technician();
		
		tech.setName(techName);
		tech.setAddress(techAddress);
		tech.setEmail(techEmail);
		tech.setPassword(techPassword);
		tech.setPhoneNumber(techPhone);
		technicianRepository.save(tech);

		tech = null;
		
		
		//load technician
		tech = technicianRepository.findTechnicianByEmail(techEmail);
		assertNotNull(tech);
		
		assertEquals(techEmail, tech.getEmail());
		assertEquals(techName, tech.getName());
		assertEquals(techPhone, tech.getPhoneNumber());
		
		
	}
	
	
}