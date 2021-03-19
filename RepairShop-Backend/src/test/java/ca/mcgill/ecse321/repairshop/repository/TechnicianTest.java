package ca.mcgill.ecse321.repairshop.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.mcgill.ecse321.repairshop.model.Technician;

@ActiveProfiles("test")
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

		//load technician
		tech = technicianRepository.findTechnicianByEmail(techEmail);
		assertNotNull(tech);
		
		assertEquals(techEmail, tech.getEmail());
		assertEquals(techName, tech.getName());
		assertEquals(techPhone, tech.getPhoneNumber());
		
	}
	
	
	
	@Test
	public void testDeleteTechnician() {
		
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

		//load technician
		tech = technicianRepository.findTechnicianByEmail(techEmail);
		assertNotNull(tech);
		assertEquals(techEmail, tech.getEmail());
		assertEquals(techName, tech.getName());
		assertEquals(techPhone, tech.getPhoneNumber());

		//delete and try loading technician
		technicianRepository.deleteTechnicianByEmail(techEmail);
		tech = technicianRepository.findTechnicianByEmail(techEmail);
		assertNull(tech);
		
		
	}
	
	
	@Test
	public void testFindAllTechnician() {
		//create technician
		String techName = "TestCustomer1";
		String techEmail = "CustomerEmail1";
		String techPassword = "CustomerPassword";
		String techAddress = "ABCD";
		String techPhone = "63534525453";
		Technician tech1 = new Technician();
				
		tech1.setName(techName);
		tech1.setAddress(techAddress);
		tech1.setEmail(techEmail);
		tech1.setPassword(techPassword);
		tech1.setPhoneNumber(techPhone);
		technicianRepository.save(tech1);
		
		
		//create technician
		String tech2Name = "TestCustomer2";
		String tech2Email = "CustomerEmail2";
		String tech2Password = "CustomerPassword";
		String tech2Address = "ABCD";
		String tech2Phone = "63534525453";
		Technician tech2 = new Technician();
						
		tech2.setName(tech2Name);
		tech2.setAddress(tech2Address);
		tech2.setEmail(tech2Email);
		tech2.setPassword(tech2Password);
		tech2.setPhoneNumber(tech2Phone);
		technicianRepository.save(tech2);
		
		
		List<Technician> techList = technicianRepository.findAll();
		assertEquals(2, techList.size());
		assertEquals("CustomerEmail1", techList.get(0).getEmail());
		assertEquals("CustomerEmail2", techList.get(1).getEmail());
		
		
	}
	
	
	
}