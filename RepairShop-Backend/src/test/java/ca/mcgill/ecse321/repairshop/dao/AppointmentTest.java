

package ca.mcgill.ecse321.repairshop.dao;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import ca.mcgill.ecse321.repairshop.model.Technician;
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
	
	
	@AfterEach
	@BeforeEach
	public void clearDatabase() {
		// Fisrt, we clear registrations to avoid exceptions due to inconsistencies
		appointmentRepository.deleteAll();
		// Then we can clear the other tables
		customerRepository.deleteAll();
		serviceRepository.deleteAll();
		technicianRepository.deleteAll();
		timeSlotRepository.deleteAll();;
		
	}

	

	@Test
	public void testPersistAndLoadAppointment() {
		Long appID = null;
		
		//create customer
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
		customerRepository.save(customer);
		
		
		//create service
		int duration = 1;
		String serviceName = "repair";
		double price = 20.00;
		Service service = new Service();
		service.setName(serviceName);
		service.setDuration(duration);
		service.setPrice(price);
		serviceRepository.save(service);
		
		
		//create technician
		String techName = "TestCustomer";
		String techEmail = "CustomerEmail";
		String techPassword = "CustomerPassword";
		String techAddress = "ABCD";
		String techPhone = "63534525453";
		Technician technician = new Technician();
		technician.setName(techName);
		technician.setAddress(techAddress);
		technician.setEmail(techEmail);
		technician.setPassword(techPassword);
		technician.setPhoneNumber(techPhone);
		technicianRepository.save(technician);
		
		
		//create time slot
		Timestamp start = Timestamp.valueOf("2021-03-01 10:00:00");
		Timestamp end = Timestamp.valueOf("2021-03-01 10:30:00");
		TimeSlot timeSlot = new TimeSlot();
		timeSlot.setStartDateTime(start);
		timeSlot.setEndDateTime(end);
		timeSlot.setTechnician(technician);
		timeSlotRepository.save(timeSlot);
		
		
		//create appointment
		ArrayList<TimeSlot> timeslots = new ArrayList<>();
		timeslots.add(timeSlot);
		Appointment appointment = new Appointment();
		timeSlot.setAppointment(appointment);
		
		
		appointment.setCustomer(customer);
		apps.add(appointment);
		customer.setAppointments(apps);
		
		appointment.setRepairSpotNumber(2);
		appointment.setService(service);
		appointment.setTimeSlots(timeslots);
		appID = appointment.getAppointmentID();
		appointmentRepository.save(appointment);
		
		appointment = null;
				
		
		
		//read from database
		appointment = appointmentRepository.findAppointmentByAppointmentID(appID);
		assertNotNull(appointment);
		
		assertEquals(appID, appointment.getAppointmentID());
		assertEquals(customer.getName(), appointment.getCustomer().getName());
		assertEquals(service.getName(), appointment.getService().getName());
	}


}



