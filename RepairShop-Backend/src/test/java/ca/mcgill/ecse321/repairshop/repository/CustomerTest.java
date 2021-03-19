package ca.mcgill.ecse321.repairshop.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import ca.mcgill.ecse321.repairshop.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//SPRING_DATASOURCE_URL=jdbc:postgresql://ec2-18-204-74-74.compute-1.amazonaws.com:5432/d1m5i3iat1kupg?password=cda24c8de5f9716a759400e1e8726eaf7791c72b8fbe3b5f6515787dbe02d0da&sslmode=require&user=lecviquyprfidz

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class CustomerTest {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired 
	private AppointmentRepository appointmentRepository;

	@Autowired
	private ServiceRepository serviceRepository;

	@Autowired
	private TechnicianRepository technicianRepository;
	
	@BeforeEach
	@AfterEach
	public void clearDatabase() {
		customerRepository.deleteAll();
		appointmentRepository.deleteAll();
		serviceRepository.deleteAll();
		technicianRepository.deleteAll();
	}

	
	public Technician createTechnician() {
		String techName = "asd";
		String techEmail = "jasd@asd.ca";
		String techPassword = "CustomerPassword";
		String techAddress = "ABCD";
		String techPhone = "63534525453";
		Technician tech = new Technician();

		tech.setName(techName);
		tech.setAddress(techAddress);
		tech.setEmail(techEmail);
		tech.setPassword(techPassword);
		tech.setPhoneNumber(techPhone);
		tech = technicianRepository.save(tech);
		return tech;
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
	public void testPersistAndLoadCustomerAppointments() {

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

		// save the customer
		customer = customerRepository.save(customer);

		//create and save service
		Service service = new Service();
		service.setName("Oil change");
		service.setDuration(3);
		service.setPrice(60.69);
		service = serviceRepository.save(service);

		// create 3 appointments for customer
		Appointment appointment1 = new Appointment();
		appointment1.setCustomer(customer);
		appointment1.setService(service);
		appointment1.setTechnician(createTechnician());
		Appointment appointment2 = new Appointment();
		appointment2.setCustomer(customer);
		appointment2.setService(service);
		appointment2.setTechnician(createTechnician());
		Appointment appointment3 = new Appointment();
		appointment3.setCustomer(customer);
		appointment3.setService(service);
		appointment3.setTechnician(createTechnician());

		// save appointments
		appointmentRepository.save(appointment1);
		appointmentRepository.save(appointment2);
		appointmentRepository.save(appointment3);

		//update customer with appointments
		ArrayList<Appointment> totalAppointments = new ArrayList<>();
		totalAppointments.add(appointment1);
		totalAppointments.add(appointment2);
		totalAppointments.add(appointment3);
		customer.setAppointments(totalAppointments);
		customerRepository.save(customer);

		// load customer
		customer = customerRepository.findCustomerByEmail(customerEmail);
		
		assertNotNull(customer);

		//ensure appointments are what is expected
		for (int i = 0; i < totalAppointments.size(); i++ ) {
			assertEquals(totalAppointments.get(i), customer.getAppointments().get(i));
		}	
		
	}
	
	
	@Test
	public void testFindAllCustomers() {
		
		String customerName1 = "TestCustomer1";
		String customerEmail1 = "CustomerEmail1";
		String customerPassword1 = "CustomerPassword";
		String customerAddress1 = "ABCD";
		String customerPhone1 = "63534525453";
		
		Customer customer1 = new Customer();
				
		customer1.setName(customerName1);
		customer1.setEmail(customerEmail1);
		customer1.setPassword(customerPassword1);
		customer1.setPhoneNumber(customerPhone1);
		customer1.setAddress(customerAddress1);
		customerRepository.save(customer1);
		
		
		String customerName2 = "TestCustomer2";
		String customerEmail2 = "CustomerEmail2";
		String customerPassword2 = "CustomerPassword";
		String customerAddress2 = "ABCD";
		String customerPhone2 = "63534525453";
		
		Customer customer2 = new Customer();
				
		customer2.setName(customerName2);
		customer2.setEmail(customerEmail2);
		customer2.setPassword(customerPassword2);
		customer2.setPhoneNumber(customerPhone2);
		customer2.setAddress(customerAddress2);
		customerRepository.save(customer2);
		
		
		List<Customer> customerList = customerRepository.findAll();
		assertEquals(2, customerList.size());
		assertEquals("CustomerEmail1", customerList.get(0).getEmail());
		assertEquals("CustomerEmail2", customerList.get(1).getEmail());
		
		
	}
	
	
	
}
