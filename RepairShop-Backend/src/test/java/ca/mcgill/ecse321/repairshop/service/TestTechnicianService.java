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

import ca.mcgill.ecse321.repairshop.dto.TechnicianDto;
import ca.mcgill.ecse321.repairshop.dto.TimeSlotDto;
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
	
	private static final List<TimeSlotDto> TECHNICIAN_WORKHOURS = new ArrayList<>();
	
	
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
				Technician tech = new Technician();
				TimeSlot t = new TimeSlot();
				t.setStartDateTime(START_TIME);
				t.setEndDateTime(END_TIME);
				List<TimeSlot> slots = new ArrayList<>();
				slots.add(t);
				tech.setName(TECHNICIAN_NAME);
				tech.setEmail(TECHNICIAN_EMAIL);
				tech.setPassword(TECHNICIAN_PASSWORD);
				tech.setPhoneNumber(TECHNICIAN_PHONE);
				tech.setAddress(TECHNICIAN_ADDRESS);
				tech.setTimeslots(slots);
				return tech;
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
		
		TimeSlotDto t = new TimeSlotDto();
		t.setStartDateTime(START_TIME);
		t.setEndDateTime(END_TIME);
		List<TimeSlotDto> workHours = new ArrayList<>();
		workHours.add(t);
		
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
		assertEquals(START_TIME, tech.getTimeSlots().get(0).getStartDateTime());
		assertEquals(END_TIME, tech.getTimeSlots().get(0).getEndDateTime());
		
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
		
		String techName = "ABCD";
		String techEmail = "some@gmail.com";
		String techPassword = "fSHBlfsuesefd";
		String techAddress = "Somewhere";
		String techPhone = "5142253789";
		
		TechnicianDto tech = null;
		
		try {
			//create
			tech = service.createTechnician(techEmail, techPassword, techPhone, techName, techAddress);
			tech = service.getTechnician(techEmail);
			assertEquals(techName, tech.getName());
			
			//delete
			service.deleteTechnician(techEmail);
			tech = service.getTechnician(techEmail);
			fail();
			
		} catch (Exception e) {
			assertEquals("Technician not found.", e.getMessage());
			
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
	public void testGetAllTechnicians() {
		
		try {
			List<TechnicianDto> techs = service.getAllTechnicians();
			assertTrue(techs.stream().map(TechnicianDto::getEmail).collect(Collectors.toList()).contains(TECHNICIAN_EMAIL));
		} catch (Exception e) {
			fail(e.getMessage());
		}
		

	}
	
	
}
