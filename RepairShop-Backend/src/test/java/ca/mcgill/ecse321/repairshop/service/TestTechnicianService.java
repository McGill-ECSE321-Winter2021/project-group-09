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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ca.mcgill.ecse321.repairshop.model.*;
import ca.mcgill.ecse321.repairshop.repository.*;
import ca.mcgill.ecse321.repairshop.service.utilities.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.mcgill.ecse321.repairshop.dto.AppointmentDto;
import ca.mcgill.ecse321.repairshop.dto.TechnicianDto;
import ca.mcgill.ecse321.repairshop.dto.TimeSlotDto;


@ExtendWith(MockitoExtension.class)

public class TestTechnicianService {
	
	@Mock
	private TechnicianRepository techRepo;

	@Mock
	private BusinessRepository businessRepository;

	@Mock
	private AppointmentRepository appointmentRepository;

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private TimeSlotRepository timeSlotRepository;

	@Mock
	private AppointmentService appointmentService;

	@Mock
	private EmailService emailService;
	
	@InjectMocks
	private TechnicianService service;

	
	private static final String TECHNICIAN_NAME = "ABCD";
	private static final String TECHNICIAN_EMAIL = "someone@gmail.com";
	private static final String TECHNICIAN_PASSWORD = "fSHBlfsuesefd";
	private static final String TECHNICIAN_ADDRESS = "Somewhere";
	private static final String TECHNICIAN_PHONE = "5142253789";
	private static final Timestamp START_TIME = Timestamp.valueOf("2021-03-02 10:00:00");
	private static final Timestamp END_TIME = Timestamp.valueOf("2021-03-02 11:00:00");
	private static final Timestamp START_TIME2 = Timestamp.valueOf("2021-03-06 09:00:00");
	private static final Timestamp END_TIME2 = Timestamp.valueOf("2021-03-06 17:00:00");

	private static final String CUSTOMER_EMAIL = "someone@gmail.com";
	private static final String SERVICE_NAME = "Repair";
	private static final int SERVICE_DURATION = 1;
	private static final double SERVICE_PRICE = 10.0;
	private static final Timestamp S_TIME = Timestamp.valueOf("2021-03-02 10:00:00");
	private static final Timestamp E_TIME = Timestamp.valueOf("2021-03-02 10:30:00");

	// Business for mock
	private static final String BUSINESS_NAME = "TestBusiness";
	private static final String BUSINESS_ADDRESS = "123 Business Street, Montreal";
	private static final String BUSINESS_EMAIL = "bestBusiness@gmail.com";
	private static final String BUSINESS_PHONE_NUMBER = "(123)-456-7890";
	private static final int BUSINESS_NUMBER_OF_REPAIR_SPOTS = 2;
	private static final Timestamp B_START_TIME = Timestamp.valueOf("2021-12-02 10:00:00");
	private static final Timestamp B_END_TIME = Timestamp.valueOf("2021-12-30 11:00:00");
	
	
	private final Customer CUSTOMER = new Customer();
	private final Appointment APP = new Appointment();
	private final TimeSlot TIME = new TimeSlot();
	private final Service SERVICE = new Service();
	private final Technician TECHNICIAN = new Technician();

	
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
			TECHNICIAN.setAppointments(Collections.emptyList());
			
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
				CUSTOMER.setReminders(Collections.emptyList());
				SERVICE.setName(SERVICE_NAME);
				SERVICE.setPrice(SERVICE_PRICE);
				SERVICE.setDuration(SERVICE_DURATION);
				TIME.setStartDateTime(S_TIME);
				TIME.setEndDateTime(E_TIME);

				APP.setAppointmentID(1L);
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

		lenient().when(techRepo.findById(anyString())).thenAnswer((InvocationOnMock invocation) -> techRepo.findTechnicianByEmail(invocation.getArgument(0)));

		lenient().when(businessRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {

			//TimeSlot
			TimeSlot timeSlot = new TimeSlot();
			timeSlot.setStartDateTime(B_START_TIME);
			timeSlot.setEndDateTime(B_END_TIME);
			List<TimeSlot> holidaysList = new ArrayList<>();
			holidaysList.add(timeSlot);

			//CREATE BUSINESS
			Business business = new Business();
			business.setHolidays(holidaysList);
			business.setName(BUSINESS_NAME);
			business.setEmail(BUSINESS_EMAIL);
			business.setAddress(BUSINESS_ADDRESS);
			business.setPhoneNumber(BUSINESS_PHONE_NUMBER);
			business.setNumberOfRepairSpots(BUSINESS_NUMBER_OF_REPAIR_SPOTS);

			List<Business> businesses = new ArrayList<>();
			businesses.add(business);
			return businesses;

		});

		lenient().when(appointmentRepository.findById(any(Long.class))).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals(1L)) {
				APP.setCustomer(CUSTOMER);
				APP.setService(SERVICE);
				APP.setTechnician(TECHNICIAN);
				APP.setTimeSlot(TIME);
				return Optional.of(APP);
			} else return null;
		});

		lenient().when(customerRepository.findById(anyString())).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals(CUSTOMER_EMAIL)) {
				CUSTOMER.setEmail(CUSTOMER_EMAIL);
				CUSTOMER.setReminders(Collections.emptyList());
				APP.setCustomer(CUSTOMER);
				APP.setService(SERVICE);
				APP.setTechnician(TECHNICIAN);
				APP.setTimeSlot(TIME);
				List<Appointment> apps = new ArrayList<>();
				apps.add(APP);
				CUSTOMER.setAppointments(apps);
				return Optional.of(CUSTOMER);
			} else return null;
		});

		lenient().when(customerRepository.save(any(Customer.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
		
		lenient().when(techRepo.save(any(Technician.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
		
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
		
		
		try {
			service.createTechnician(techEmail1, techPassword1, techPhone1, techName1, techAddress1);
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
			assertEquals(CUSTOMER_EMAIL, app.getCustomerDto().getEmail());
			assertEquals(TECHNICIAN_EMAIL, app.getTechnicianDto().getEmail());
			assertEquals(SERVICE_NAME, app.getServiceDto().getName());
			assertEquals(S_TIME, app.getTimeSlotDto().getStartDateTime());
			assertEquals(E_TIME, app.getTimeSlotDto().getEndDateTime());
			
		} catch(Exception e) {
			fail(e.getMessage());
		}
		
	}
	
	
	@Test
	public void testViewSchedule() {
		
		try {
			
			List<TimeSlotDto> timeSlots = service.viewTechnicianSchedule(TECHNICIAN_EMAIL, "2021-03-01");
			TimeSlotDto timeSlot = timeSlots.get(0);
			assertEquals(1, timeSlots.size());
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


	@Test
	public void testAddTechnicianWorkHoursNull() {
		Timestamp start = Timestamp.valueOf("2021-03-02 9:00:00");
		Timestamp end = Timestamp.valueOf("2021-03-02 17:00:00");
		TimeSlotDto dto = new TimeSlotDto();
		dto.setStartDateTime(start);
		dto.setEndDateTime(end);
		List<TimeSlotDto> dtos = new ArrayList<>();
		dtos.add(dto);

		try {
			service.addTechnicianWorkHours(null, dtos);
			fail();
		} catch(Exception e) {
			assertEquals("Email cannot be empty.", e.getMessage());
		}

	}

	// private static final Timestamp START_TIME = Timestamp.valueOf("2021-03-02 10:00:00");
	//	private static final Timestamp END_TIME = Timestamp.valueOf("2021-03-02 11:00:00");
	//	private static final Timestamp START_TIME2 = Timestamp.valueOf("2021-03-06 09:00:00");
	//	private static final Timestamp END_TIME2 = Timestamp.valueOf("2021-03-06 17:00:00");

	@Test // Valid
	public void testAddSpecificWorkHours() {

		Timestamp start = Timestamp.valueOf("2021-03-04 10:00:00");
		Timestamp end = Timestamp.valueOf("2021-03-04 14:00:00");

		TimeSlotDto newHours = new TimeSlotDto();
		newHours.setStartDateTime(start);
		newHours.setEndDateTime(end);

		try {
			String message = service.addSpecificWorkHours(TECHNICIAN_EMAIL, newHours);
			assertEquals("Work hours for technician " + TECHNICIAN_EMAIL + " successfully added.", message);
			assertEquals(2, TECHNICIAN.getTimeslots().size());
			assertEquals(start, TECHNICIAN.getTimeslots().get(1).getStartDateTime());
			assertEquals(end, TECHNICIAN.getTimeslots().get(1).getEndDateTime());
		} catch(Exception e) {
			fail(e.getMessage());
		}

	}


	@Test // Invalid - null email
	public void testAddSpecificWorkHoursNull() {

		Timestamp start = Timestamp.valueOf("2021-03-04 10:00:00");
		Timestamp end = Timestamp.valueOf("2021-03-04 14:00:00");

		TimeSlotDto newHours = new TimeSlotDto();
		newHours.setStartDateTime(start);
		newHours.setEndDateTime(end);

		try {
			service.addSpecificWorkHours(null, newHours);
			fail();
		} catch(Exception e) {
			assertEquals("Email cannot be empty.", e.getMessage());
		}

	}

	@Test // Invalid - start after end
	public void testAddSpecificWorkHoursInvalidHours() {

		Timestamp start = Timestamp.valueOf("2021-03-04 14:00:00");
		Timestamp end = Timestamp.valueOf("2021-03-04 10:00:00");

		TimeSlotDto newHours = new TimeSlotDto();
		newHours.setStartDateTime(start);
		newHours.setEndDateTime(end);

		try {
			service.addSpecificWorkHours(TECHNICIAN_EMAIL, newHours);
			fail();
		} catch(Exception e) {
			assertEquals("The end date and time must be after the start date and time.", e.getMessage());
		}

	}

	@Test // Invalid - hours overlap
	public void testAddSpecificWorkHoursOverlap() {

		Timestamp start = Timestamp.valueOf("2021-03-02 10:30:00");
		Timestamp end = Timestamp.valueOf("2021-03-02 14:00:00");

		TimeSlotDto newHours = new TimeSlotDto();
		newHours.setStartDateTime(start);
		newHours.setEndDateTime(end);

		try {
			service.addSpecificWorkHours(TECHNICIAN_EMAIL, newHours);
			fail();
		} catch(Exception e) {
			assertEquals("The specified hours cannot overlap with existing hours.", e.getMessage());
		}

	}


	@Test // Valid
	public void testDeleteSchedule() {
		try {
			String message = service.deleteSchedule(TECHNICIAN_EMAIL);
			assertEquals(TECHNICIAN_EMAIL + "'s schedule has been removed.", message);
		} catch(Exception e) {
			fail(e.getMessage());
		}
	}

	@Test // Invalid
	public void testDeleteScheduleNull() {
		try {
			service.deleteSchedule(null);
			fail();
		} catch(Exception e) {
			assertEquals("Email cannot be empty.", e.getMessage());
		}
	}

	@Test // Invalid
	public void testDeleteScheduleEmpty() {
		try {
			service.deleteSchedule("");
			fail();
		} catch(Exception e) {
			assertEquals("Email cannot be empty.", e.getMessage());
		}
	}

	@Test // Invalid
	public void testDeleteScheduleNonExistentTechnician() {
		try {
			service.deleteSchedule("notATechnician");
			fail();
		} catch(Exception e) {
			assertEquals("Technician not found.", e.getMessage());
		}
	}

	@Test // Valid timeslot
	public void testDeleteSpecificWorkHours() {

		try {
			String message = service.deleteSpecificWorkHours(TECHNICIAN_EMAIL, START_TIME, END_TIME);
			assertEquals("Requested work hours were removed.", message);
		} catch(Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test // Invalid timeslot
	public void testDeleteSpecificWorkHoursInvalidHours() {

		try {
			String message = service.deleteSpecificWorkHours(TECHNICIAN_EMAIL, START_TIME2, END_TIME2);
			assertEquals("Could not find provided work hours", message);
		} catch(Exception e) {
			fail(e.getMessage());
		}
	}

	@Test // Invalid technician
	public void testDeleteSpecificWorkHoursNullTechnician() {

		try {
			service.deleteSpecificWorkHours(null, START_TIME, END_TIME);
			fail();
		} catch(Exception e) {
			assertEquals("Email cannot be empty.", e.getMessage());
		}
	}

	@Test // Invalid technician
	public void testDeleteSpecificWorkHoursEmptyTechnician() {

		try {
			service.deleteSpecificWorkHours("", START_TIME, END_TIME);
			fail();
		} catch(Exception e) {
			assertEquals("Email cannot be empty.", e.getMessage());
		}
	}

	@Test // Invalid timeslot
	public void testDeleteSpecificWorkHoursNonExistentTechnician() {

		try {
			service.deleteSpecificWorkHours("notATechnician", START_TIME, END_TIME);
		} catch(Exception e) {
			assertEquals("Technician not found.", e.getMessage());
		}
	}
	
}
