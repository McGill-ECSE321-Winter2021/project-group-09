package ca.mcgill.ecse321.repairshop.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.mcgill.ecse321.repairshop.model.Appointment;
import ca.mcgill.ecse321.repairshop.model.Customer;
import ca.mcgill.ecse321.repairshop.model.Service;
import ca.mcgill.ecse321.repairshop.model.TimeSlot;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AppointmentTest {

	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private ServiceRepository serviceRepository;
	@Autowired
	private AppointmentRepository appointmentRepository;
	@Autowired
	private TimeSlotRepository timeSlotRepository;
	@Autowired
	private TechnicianRepository technicianRepository;
	
	

	@BeforeEach
	@AfterEach
	public void clearDatabase() {
		// First, we clear registrations to avoid exceptions due to inconsistencies
		appointmentRepository.deleteAll();
		// Then we can clear the other tables
		customerRepository.deleteAll();
		serviceRepository.deleteAll();
		technicianRepository.deleteAll();
		timeSlotRepository.deleteAll();;
	}
	
	

	public Customer createCustomer() {
		String customerName = "TestCustomer";
		String customerEmail = "CustomerEmail";
		String customerPassword = "CustomerPassword";
		String customerAddress = "ABCD";
		String customerPhone = "63534525453";
		Customer customer = new Customer();
		customer.setName(customerName);
		customer.setAddress(customerAddress);
		customer.setEmail(customerEmail);
		customer.setPassword(customerPassword);
		customer.setPhoneNumber(customerPhone);
		ArrayList<Appointment> apps = new ArrayList<>();
		customer.setAppointments(apps);
		return customerRepository.save(customer);
	}

	
	
	public Service createService() {
		int duration = 1;
		String serviceName = "repair";
		double price = 20.00;
		Service service = new Service();
		service.setName(serviceName);
		service.setDuration(duration);
		service.setPrice(price);
		return serviceRepository.save(service);
	}
	
	

	
	
	@Test
	public void testPersistAndLoadAppointment() {
		//create time slot
		Timestamp start = Timestamp.valueOf("2021-03-01 10:00:00");
		Timestamp end = Timestamp.valueOf("2021-03-01 10:30:00");
		TimeSlot timeSlot = new TimeSlot();
		timeSlot.setStartDateTime(start);
		timeSlot.setEndDateTime(end);
		timeSlotRepository.save(timeSlot);

		//create appointment
		ArrayList<TimeSlot> timeslots = new ArrayList<>();
		timeslots.add(timeSlot);
		Appointment appointment = new Appointment();
		Customer createdCustomer = createCustomer();
		appointment.setCustomer(createdCustomer);
		Service createdService = createService();
		appointment.setService(createdService);
		appointment.setTimeSlots(timeslots);
		Long appID = appointmentRepository.save(appointment).getAppointmentID();
		
		appointment = null;
		
		//read from database
		appointment = appointmentRepository.findAppointmentByAppointmentID(appID);
		assertNotNull(appointment);
		assertEquals(createdCustomer.getName(), appointment.getCustomer().getName());
		assertEquals(createdService.getName(), appointment.getService().getName());

		
	}
	
	
	@Test
	public void testDeleteAppointment() {
		//create time slot
		Timestamp start = Timestamp.valueOf("2021-03-01 10:00:00");
		Timestamp end = Timestamp.valueOf("2021-03-01 10:30:00");
		TimeSlot timeSlot = new TimeSlot();
		timeSlot.setStartDateTime(start);
		timeSlot.setEndDateTime(end);
		timeSlotRepository.save(timeSlot);

		//create appointment
		ArrayList<TimeSlot> timeslots = new ArrayList<>();
		timeslots.add(timeSlot);
		Appointment appointment = new Appointment();
		Customer createdCustomer = createCustomer();
		appointment.setCustomer(createdCustomer);
		Service createdService = createService();
		appointment.setService(createdService);
		appointment.setTimeSlots(timeslots);
		Long appID = appointmentRepository.save(appointment).getAppointmentID();
		
		appointment = null;
		
		//read from database
		appointment = appointmentRepository.findAppointmentByAppointmentID(appID);
		assertNotNull(appointment);
		assertEquals(createdCustomer.getName(), appointment.getCustomer().getName());
		assertEquals(createdService.getName(), appointment.getService().getName());
		
		
		//delete and try to read from database
		appointmentRepository.deleteById(appID);
		appointment = appointmentRepository.findAppointmentByAppointmentID(appID);
		assertNull(appointment);
		
	}
	
	
	@Test
	public void testGetAppointmentByCustomer() {
		
		//create time slot
		Timestamp start = Timestamp.valueOf("2021-03-01 10:00:00");
		Timestamp end = Timestamp.valueOf("2021-03-01 10:30:00");
		TimeSlot timeSlot = new TimeSlot();
		timeSlot.setStartDateTime(start);
		timeSlot.setEndDateTime(end);
		timeSlotRepository.save(timeSlot);

		//create appointment
		ArrayList<TimeSlot> timeslots = new ArrayList<>();
		timeslots.add(timeSlot);
		Appointment appointment = new Appointment();
		Customer createdCustomer = createCustomer();
		timeSlot.setAppointment(appointment);
		createdCustomer.getAppointments().add(appointment);
		appointment.setCustomer(createdCustomer);
		Service createdService = createService();
		appointment.setService(createdService);
		appointment.setTimeSlots(timeslots);
		Long appID = appointmentRepository.save(appointment).getAppointmentID();
				
		appointment = null;
				
		//read from database
		appointment = appointmentRepository.findByCustomer(createdCustomer).get(0);
		assertNotNull(appointment);
		assertEquals(createdCustomer.getName(), appointment.getCustomer().getName());
		assertEquals(createdService.getName(), appointment.getService().getName());

		
	}
	
	
	
	
	@Test
	public void testGetAppointmentByService() {
		
		//create time slot
		Timestamp start = Timestamp.valueOf("2021-03-01 10:00:00");
		Timestamp end = Timestamp.valueOf("2021-03-01 10:30:00");
		TimeSlot timeSlot = new TimeSlot();
		timeSlot.setStartDateTime(start);
		timeSlot.setEndDateTime(end);
		timeSlotRepository.save(timeSlot);

		//create appointment
		ArrayList<TimeSlot> timeslots = new ArrayList<>();
		timeslots.add(timeSlot);
		Appointment appointment = new Appointment();
		Customer createdCustomer = createCustomer();
		timeSlot.setAppointment(appointment);
		createdCustomer.getAppointments().add(appointment);
		appointment.setCustomer(createdCustomer);
		Service createdService = createService();
		appointment.setService(createdService);
		appointment.setTimeSlots(timeslots);
		Long appID = appointmentRepository.save(appointment).getAppointmentID();
				
		appointment = null;
				
		//read from database
		appointment = appointmentRepository.findByService(createdService).get(0);
		assertNotNull(appointment);
		assertEquals(createdCustomer.getName(), appointment.getCustomer().getName());
		assertEquals(createdService.getName(), appointment.getService().getName());

		
	}
	
	
	
	
	


}