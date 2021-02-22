package ca.mcgill.ecse321.repairshop.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.mcgill.ecse321.repairshop.model.Admin;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class AdminTest {
	
	@Autowired
	private AdminRepository administratorRepository;
	
	@BeforeEach
	@AfterEach
	public void clearDatabase() {
		administratorRepository.deleteAll();
	}
	
	@Test
	public void testPersistAndLoadCustomer() {
		
		//create customer
		String adminName = "KaterinaMoluscu";
		String adminEmail = "kmoluscu@RepairShop.com";
		String adminPassword = "its_4am_in_the_morning";
		String adminAddress = "234 BigMountain Road";
		String adminPhone = "5143456789";
		
		Admin administrator = new Admin();
		
		administrator.setName(adminName);
		administrator.setAddress(adminAddress);
		administrator.setEmail(adminEmail);
		administrator.setPassword(adminPassword);
		administrator.setPhoneNumber(adminPhone);
		
		// save is used to add/update an entry in the database 
		administratorRepository.save(administrator); // persists the customer data types in DB
		
		administrator = null;
		
		// load customer
		administrator = administratorRepository.findAdminByEmail(adminEmail);
		assertNotNull(administrator);
		assertEquals(adminEmail, administrator.getEmail());
		assertEquals(adminName, administrator.getName());
		assertEquals(adminAddress, administrator.getAddress());
		assertEquals(adminPassword, administrator.getPassword());
		assertEquals(adminPhone, administrator.getPhoneNumber());
	}
	
    @Test
    void testDeleteAdmin() {

        // create administrator
        Admin administrator = new Admin();
        administrator.setName("Ron Carter");
        administrator.setEmail("ronnyCart@bassPlayer.com");
        administrator.setAddress("52 Street");
        administrator.setPassword("i played w/ miles davis");
        administrator.setPhoneNumber("i communicate by pigeonmail");

        // save administrator in db
        administratorRepository.save(administrator);
        
        administrator = administratorRepository.findAdminByEmail(administrator.getEmail());
        assertNotNull(administrator);

        // delete from db
        administratorRepository.deleteById(administrator.getEmail());

        // assertion
        assertNull(administratorRepository.findAdminByEmail(administrator.getEmail()));

    }
}
