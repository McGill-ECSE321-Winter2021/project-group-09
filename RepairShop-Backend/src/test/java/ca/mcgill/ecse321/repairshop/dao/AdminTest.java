package ca.mcgill.ecse321.repairshop.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.mcgill.ecse321.repairshop.model.Admin;

//SPRING_DATASOURCE_URL=jdbc:postgresql://ec2-18-204-74-74.compute-1.amazonaws.com:5432/d1m5i3iat1kupg?password=cda24c8de5f9716a759400e1e8726eaf7791c72b8fbe3b5f6515787dbe02d0da&sslmode=require&user=lecviquyprfidz

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
}