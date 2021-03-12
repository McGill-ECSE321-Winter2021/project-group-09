package ca.mcgill.ecse321.repairshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import ca.mcgill.ecse321.repairshop.dto.AppointmentDto;
import ca.mcgill.ecse321.repairshop.dto.TechnicianDto;
import ca.mcgill.ecse321.repairshop.dto.TimeSlotDto;
import ca.mcgill.ecse321.repairshop.model.Appointment;
import ca.mcgill.ecse321.repairshop.model.Customer;
import ca.mcgill.ecse321.repairshop.model.Service;
import ca.mcgill.ecse321.repairshop.model.Technician;
import ca.mcgill.ecse321.repairshop.model.TimeSlot;
import ca.mcgill.ecse321.repairshop.repository.TechnicianRepository;



@ExtendWith(MockitoExtension.class)

public class TestTechnicianService {
	
	@Mock
	private TechnicianRepository techRepo;
	
	@InjectMocks
	private TechnicianService service;
	
	
	
	private static final String TECHNICIAN_NAME = "ABCD";
	private static final String TECHNICIAN_EMAIL = "someone@gmail.com";
	private static final String TECHNICIAN_PASSWORD = "fSHBlfsuesefd";
	private static final String TECHNICIAN_ADDRESS = "Somewhere";
	private static final String TECHNICIAN_PHONE = "5142253789";
	private static final Timestamp START_TIME = Timestamp.valueOf("2021-03-02 10:00:00");
	private static final Timestamp END_TIME = Timestamp.valueOf("2021-03-02 11:00:00");	
	
	private static final String CUSTOMER_EMAIL = "someone@gmail.com";
	private static final String SERVICE_NAME = "Repair";
	private static final int SERVICE_DURATION = 1;
	private static final double SERVICE_PRICE = 10.0;
	private static final Timestamp S_TIME = Timestamp.valueOf("2021-03-02 10:00:00");
	private static final Timestamp E_TIME = Timestamp.valueOf("2021-03-02 10:30:00");	
	
	
	private Customer CUSTOMER = new Customer();
	private Appointment APP = new Appointment();
	private TimeSlot TIME = new TimeSlot();
	private Service SERVICE = new Service();


	
	private Technician TECHNICIAN = new Technician();

	
	
	@BeforeEach
	public void setMockOutput() {

		lenient().when(techRepo.findAll()).thenAnswer((InvocationOnMock invocation) -> {
			List<TimeSlot> slots = new ArrayList<>();
			
			TECHNICIAN.setName(TECHNICIAN_NAME);
			TECHNICIAN.setEmail(TECHNICIAN_EMAIL);
			TECHNICIAN.setPassword(TECHNICIAN_PASSWORD);
			TECHNICIAN.setPhoneNumber(TECHNICIAN_PHONE);
			TECHNICIAN.setAddress(TECHNICIAN_ADDRESS);
			TECHNICIAN.setTimeslots(slots);
			
			List<Technician> techList = new ArrayList<>();
			techList.add(TECHNICIAN);
			return techList;
			
		});
		
		lenient().when(techRepo.findTechnicianByEmail(anyString())).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals(TECHNICIAN_EMAIL)) {
				
				TimeSlot t = new TimeSlot();
				t.setStartDateTime(START_TIME);
				t.setEndDateTime(END_TIME);
				List<TimeSlot> slots = new ArrayList<>();
				List<Appointment> apps = new ArrayList<>();
				slots.add(t);
				TECHNICIAN.setName(TECHNICIAN_NAME);
				TECHNICIAN.setEmail(TECHNICIAN_EMAIL);
				TECHNICIAN.setPassword(TECHNICIAN_PASSWORD);
				TECHNICIAN.setPhoneNumber(TECHNICIAN_PHONE);
				TECHNICIAN.setAddress(TECHNICIAN_ADDRESS);
				TECHNICIAN.setTimeslots(slots);
				
				//technician appointments
				CUSTOMER.setEmail(CUSTOMER_EMAIL);
				SERVICE.setName(SERVICE_NAME);
				SERVICE.setPrice(SERVICE_PRICE);
				SERVICE.setDuration(SERVICE_DURATION);
				TIME.setStartDateTime(S_TIME);
				TIME.setEndDateTime(E_TIME);
				
				APP.setCustomer(CUSTOMER);
				APP.setService(SERVICE);
				APP.setTechnician(TECHNICIAN);
				APP.setTimeSlot(TIME);
				apps.add(APP);
				TECHNICIAN.setAppointments(apps);
				
				return TECHNICIAN;
			} else {
				return null;
			}
			
		});
		
		
		Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
			return invocation.getArgument(0);
		};
		
		lenient().when(techRepo.save(any(Technician.class))).thenAnswer(returnParameterAsAnswer);
		
	}	
	
	
	
	
	
	@Test
	public void testCreateTechnician() {

		String techName = "ABCD";
		String techEmail = "somebody@gmail.com";
		String techPassword = "fSHBlfsuesefd";
		String techAddress = "Somewhere";
		String techPhone = "5142253789";
		
		
		TechnicianDto tech = null;
		
		try {
			tech = service.createTechnician(techEmail, techPassword, techPhone, techName, techAddress);
		} catch (Exception e) {
			// Check that no error occurred
			fail(e.getMessage());
		}
		
		assertNotNull(tech);
		assertEquals(techName, tech.getName());
		assertEquals(techEmail, tech.getEmail());
		
		
	}

	
	
	@Test
	public void testCreateTechnicianNull() {

		String techName = "ABCD";
		String techEmail = null;
		String techPassword = null;
		String techAddress = "Somewhere";
		String techPhone = "5142253789";
		
		try {
			service.createTechnician(techEmail, techPassword, techPhone, techName, techAddress);
			fail();
		} catch (Exception e) {
			//an error should occur
			assertEquals("Email or password cannot be empty.", e.getMessage());
		}	
		
		
	}
	
	
	@Test
	public void testCreateTechnicianDuplicate() {

		String techName1 = "ABCD";
		String techEmail1 = "someone@gmail.com";
		String techPassword1 = "fSHBlfsuesefd";
		String techAddress1 = "Somewhere";
		String techPhone1 = "5142253789";
		
		
		String techName2 = "ABCD";
		String techEmail2 = "someone@gmail.com";
		String techPassword2 = "fSHBlfsuesefd";
		String techAddress2 = "Somewhere";
		String techPhone2 = "5142253789";
		
		try {
			service.createTechnician(techEmail1, techPassword1, techPhone1, techName1, techAddress1);
			service.createTechnician(techEmail2, techPassword2, techPhone2, techName2, techAddress2);
			fail();
		} catch (Exception e) {
			//an error should occur
			assertEquals("Email is already taken.", e.getMessage());
		}
		
	
		
	}
	
	
	
	
	
	@Test
	public void testChangePassword() {

		String newPassword = "gsdfjsoifkl";
		
		TechnicianDto tech = null;
		
		try {
			tech = service.changePassword(TECHNICIAN_EMAIL, newPassword);
		} catch (Exception e) {
			// Check that no error occurred
			fail(e.getMessage());
		}
		
		assertNotNull(tech);
		assertEquals(newPassword, tech.getPassword());
		assertEquals(TECHNICIAN_EMAIL, tech.getEmail());
		
	}
	
	
	
	@Test
	public void testChangePasswordNull() {
				
		try {
			service.changePassword(null, null);
			fail();
		} catch (Exception e) {
			//error should occur
			assertEquals("Email or new password cannot be empty.", e.getMessage());
		}
		
		
	}
	
	
	@Test
	public void testChangePasswordNonExistentTechnician() {
				
		try {
			service.changePassword("someMail@gmail.com", "fdjhyffaskd");
			fail();
		} catch (Exception e) {
			//error should occur
			assertEquals("Technician not found.", e.getMessage());
		}
	}
	
	
	@Test
	public void testGetTechnician() {

		TechnicianDto tech = null;
		
		try {
			tech = service.getTechnician(TECHNICIAN_EMAIL);
		} catch (Exception e) {
			// Check that no error occurred
			fail();
		}
		
		assertNotNull(tech);
		assertEquals(TECHNICIAN_NAME, tech.getName());
		assertEquals(TECHNICIAN_EMAIL, tech.getEmail());
		
	}
	
	
	
	
	@Test
	public void testGetTechnicianNull() {
		
		try {
			service.getTechnician(null);
			fail();
		} catch (Exception e) {
			// error should occur
			assertEquals("Email cannot be empty.", e.getMessage());
		}
		
		
	}
	
	
	
	@Test
	public void testGetNonExistentTechnician() {
		
		String techEmail = "xyz@gmail.com";
		
		try {
			service.getTechnician(techEmail);
			fail();
		} catch (Exception e) {
			// error should occur
			assertEquals("Technician not found.", e.getMessage());
		}
		
		
		
	}
	

	
	@Test              //not sure if this test is correct
	public void testDeleteTechnician() {
		
		try {
			
			//delete
			String message = service.deleteTechnician(TECHNICIAN_EMAIL);
			assertEquals("Technician account with email " + TECHNICIAN_EMAIL + " deleted.", message);
			
		} catch (Exception e) {
			fail(e.getMessage());
		}
	

	}
	
	
	
	
	@Test
	public void testDeleteTechnicianNull() {

		try {
			//create
			service.deleteTechnician(null);
			
		} catch (Exception e) {
			// error should occur
			assertEquals("Email cannot be empty.", e.getMessage());
		}
		
	}
	
	
	
	
	@Test
	public void testDeleteNonExistentTechnician() {
		String email = "abc@gmail.com";
		
		try {
			//create
			service.deleteTechnician(email);
			
		} catch (Exception e) {
			// error should occur
			assertEquals("Technician not found.", e.getMessage());
		}
		
	}
	
	
	@Test
	public void testGetTechnicianTimeSlots() {
		
		List<TimeSlotDto> slots = null;
		try {
			slots = service.getWorkHours(TECHNICIAN_EMAIL);
		} catch(Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(1, slots.size());
		assertEquals(START_TIME, slots.get(0).getStartDateTime());
		assertEquals(END_TIME, slots.get(0).getEndDateTime());
	}
	
	
	@Test
	public void testGetTechnicianWorkHoursNull() {
		
		try {
			service.getWorkHours(null);
			fail();
		} catch (Exception e) {
			// error should occur
			assertEquals("Email cannot be empty.", e.getMessage());
		}
		
		
	}
	
	
	
	@Test
	public void testGetNonExistentTechnicianWorkHours() {
		
		String techEmail = "xyz@gmail.com";
		
		try {
			service.getWorkHours(techEmail);
			fail();
		} catch (Exception e) {
			// error should occur
			assertEquals("Technician not found.", e.getMessage());
		}
		
		
		
	}
	

	
	
	@Test
	public void testGetAllTechnicians() {
		
		try {
			List<TechnicianDto> techs = service.getAllTechnicians();
			assertTrue(techs.stream().map(TechnicianDto::getEmail).collect(Collectors.toList()).contains(TECHNICIAN_EMAIL));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	
	}
	
	
	@Test
	public void testViewAppointment() {
		
		try {
			
			AppointmentDto app = service.viewAppointments(CUSTOMER_EMAIL).get(0);
			assertEquals(1, service.viewAppointments(CUSTOMER_EMAIL).size());
			assertEquals(CUSTOMER_EMAIL, app.getCustomer().getEmail());
			assertEquals(TECHNICIAN_EMAIL, app.getTechnician().getEmail());
			assertEquals(SERVICE_NAME, app.getService().getName());
			assertEquals(S_TIME, app.getTimeSlot().getStartDateTime());
			assertEquals(E_TIME, app.getTimeSlot().getEndDateTime());
			
		} catch(Exception e) {
			fail(e.getMessage());
		}
		
	}
	
	
	@Test
	public void testViewSchedule() {
		
		try {
			
			TimeSlotDto timeSlot = service.viewTechnicianSchedule(TECHNICIAN_EMAIL).get(0);
			assertEquals(1, service.viewTechnicianSchedule(TECHNICIAN_EMAIL).size());
			assertEquals(S_TIME, timeSlot.getStartDateTime());
			assertEquals(E_TIME, timeSlot.getEndDateTime());
			
		} catch(Exception e) {
			fail(e.getMessage());
		}
	}
	
	
	
	
	
	
	@Test 
	public void testAddTechnicianWorkHours() {
		Timestamp start = Timestamp.valueOf("2021-03-02 9:00:00");
		Timestamp end = Timestamp.valueOf("2021-03-02 17:00:00");
		TimeSlotDto dto = new TimeSlotDto();
		dto.setStartDateTime(start);
		dto.setEndDateTime(end);
		List<TimeSlotDto> dtos = new ArrayList<>();
		dtos.add(dto);
		
		try {
			String message = service.addTechnicianWorkHours(TECHNICIAN_EMAIL, dtos);
			assertEquals("Work hours for technician " + TECHNICIAN_EMAIL + " successfully added.", message);
			assertEquals(start, TECHNICIAN.getTimeslots().get(0).getStartDateTime());
			assertEquals(end, TECHNICIAN.getTimeslots().get(0).getEndDateTime());
			
		} catch(Exception e) {
			fail(e.getMessage());
			
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
