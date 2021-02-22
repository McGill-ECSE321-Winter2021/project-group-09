package ca.mcgill.ecse321.repairshop.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Timestamp;
import java.util.ArrayList;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.mcgill.ecse321.repairshop.model.Appointment;
import ca.mcgill.ecse321.repairshop.model.Customer;
import ca.mcgill.ecse321.repairshop.model.Reminder;
import ca.mcgill.ecse321.repairshop.model.ReminderType;

//SPRING_DATASOURCE_URL=jdbc:postgresql://ec2-18-204-74-74.compute-1.amazonaws.com:5432/d1m5i3iat1kupg?password=cda24c8de5f9716a759400e1e8726eaf7791c72b8fbe3b5f6515787dbe02d0da&sslmode=require&user=lecviquyprfidz

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class CustomerTest {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired 
	private AppointmentRepository appointmentRepository;
	
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
	public void testPersistAndLoadCustomerFromAppointment() {

		// create 3 appointments for customer
		Appointment appointment1 = new Appointment();
		Appointment appointment2 = new Appointment();
		Appointment appointment3 = new Appointment();
		// get appointment IDs
		Long appointmentID1 = appointmentRepository.save(appointment1).getAppointmentID();
		Long appointmentID2 = appointmentRepository.save(appointment2).getAppointmentID();
		Long appointmentID3 = appointmentRepository.save(appointment3).getAppointmentID();
		
		ArrayList<Appointment> totalAppointments = new ArrayList<Appointment>();
		totalAppointments.add(appointment1);
		totalAppointments.add(appointment2);
		totalAppointments.add(appointment3);
		
		//create customer
		String customerName = "Thelonious Monk";
		String customerEmail = "knowThelonious@ancientEmail.com";
		String customerPassword = "BemshaSwing";
		String customerAddress = "8th Street";
		String customerPhone = "1800-too-cool-for-cellphones";
		
		Customer customer = new Customer();
		
		customer.setName(customerName);
		customer.setAddress(customerAddress);
		customer.setEmail(customerEmail);
		customer.setPassword(customerPassword);
		customer.setPhoneNumber(customerPhone);
		customer.setAppointments(totalAppointments);
		
		// save is used to add/update an entry in the database 
		customerRepository.save(customer); // persists the customer data types in DB
		
		appointment1 = null;
		appointment2 = null;
		appointment3 = null;
		
		// load customer
		appointment1 = appointmentRepository.findAppointmentByAppointmentID(appointmentID1);
		appointment2 = appointmentRepository.findAppointmentByAppointmentID(appointmentID2);
		appointment3 = appointmentRepository.findAppointmentByAppointmentID(appointmentID3);
		
		assertNotNull(customer);
		assertNotNull(appointment1);
		assertNotNull(appointment2);
		assertNotNull(appointment3);
		
		assertEquals(customer.getName(), appointment1.getCustomer().getName());
		assertEquals(customer.getName(), appointment2.getCustomer().getName());
		assertEquals(customer.getName(), appointment3.getCustomer().getName());
		
		
	}
}
