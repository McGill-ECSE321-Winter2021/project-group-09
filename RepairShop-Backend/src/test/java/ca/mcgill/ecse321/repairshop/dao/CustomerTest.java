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

import ca.mcgill.ecse321.repairshop.model.Customer;

//SPRING_DATASOURCE_URL=jdbc:postgresql://ec2-18-204-74-74.compute-1.amazonaws.com:5432/d1m5i3iat1kupg?password=cda24c8de5f9716a759400e1e8726eaf7791c72b8fbe3b5f6515787dbe02d0da&sslmode=require&user=lecviquyprfidz

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class CustomerTest {

	@Autowired
	private CustomerRepository customerRepository;
	
	@BeforeEach
	@AfterEach
	public void clearDatabase() {
		customerRepository.deleteAll();
	}
	
	@Test
	public void testPersistAndLoadCustomer() {
		
		//create customer
		String customerName = "Eloisa Burgoise";
		String customerEmail = "eburgoise@yahoo.com";
		String customerPassword = "gummybearsrock29";
		String customerAddress = "1212 MormonTrek Blvd";
		String customerPhone = "5142253789";
		
		Customer customer = new Customer();
		
		customer.setName(customerName);
		customer.setAddress(customerAddress);
		customer.setEmail(customerEmail);
		customer.setPassword(customerPassword);
		customer.setPhoneNumber(customerPhone);
		
		// save is used to add/update an entry in the database 
		customerRepository.save(customer); // persists the customer data types in DB
		
		customer = null;
		
		// load customer
		customer = customerRepository.findCustomerByEmail(customerEmail);
		assertNotNull(customer);
		assertEquals(customerEmail, customer.getEmail());
		assertEquals(customerName, customer.getName());
		assertEquals(customerAddress, customer.getAddress());
		assertEquals(customerPassword, customer.getPassword());
		assertEquals(customerPhone, customer.getPhoneNumber());
		
	}
	
	@Test
	public void testPersistAndLoadAppointment() {
		
		//create appointment
		
	}
	
	@Test 
	public void testPersistAndLoadReminder() {
		
	}
}
