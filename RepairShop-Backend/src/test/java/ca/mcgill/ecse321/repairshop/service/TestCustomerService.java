package ca.mcgill.ecse321.repairshop.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ca.mcgill.ecse321.repairshop.dto.AppointmentDto;
import ca.mcgill.ecse321.repairshop.dto.CustomerDto;
import ca.mcgill.ecse321.repairshop.model.Customer;
import ca.mcgill.ecse321.repairshop.model.Appointment;
import ca.mcgill.ecse321.repairshop.model.Reminder;
import ca.mcgill.ecse321.repairshop.model.Service;
import ca.mcgill.ecse321.repairshop.model.Technician;
import ca.mcgill.ecse321.repairshop.model.TimeSlot;
import ca.mcgill.ecse321.repairshop.repository.AppointmentRepository;
import ca.mcgill.ecse321.repairshop.repository.CustomerRepository;


@ExtendWith(MockitoExtension.class)

public class TestCustomerService {
	
	@Mock
	private CustomerRepository customerRepo;
	
	@Mock
	private AppointmentRepository appRepo;
	
	
	@InjectMocks
	private CustomerService service;
	
	
	
	
	private static final String CUSTOMER_NAME = "ABCD";
	private static final String CUSTOMER_EMAIL = "someone@gmail.com";
	private static final String CUSTOMER_PASSWORD = "fSHBlfsuesefd";
	private static final String CUSTOMER_ADDRESS = "Somewhere";
	private static final String CUSTOMER_PHONE = "5142253789";
	
	private static final String SERVICE_NAME = "Repair";
	private static final int SERVICE_DURATION = 1;
	private static final double SERVICE_PRICE = 10.0;
	private static final String TECH_EMAIL = "tech@gmail.com";
	private static final Timestamp START_TIME = Timestamp.valueOf("2021-03-02 10:00:00");
	private static final Timestamp END_TIME = Timestamp.valueOf("2021-03-02 10:30:00");	
	private static final Long APP_ID = (long) 312312;
	
	
	private Customer CUSTOMER = new Customer();
	private Appointment APP = new Appointment();
	private TimeSlot TIME = new TimeSlot();
	private Service SERVICE = new Service();
	private Technician TECHNICIAN = new Technician();
	
	
	
	@BeforeEach
	public void setMockOutput() {
		
		

		lenient().when(customerRepo.findAll()).thenAnswer((InvocationOnMock invocation) -> {
			CUSTOMER.setName(CUSTOMER_NAME);
			CUSTOMER.setEmail(CUSTOMER_EMAIL);
			CUSTOMER.setPassword(CUSTOMER_PASSWORD);
			CUSTOMER.setPhoneNumber(CUSTOMER_PHONE);
			CUSTOMER.setAddress(CUSTOMER_ADDRESS);
			
			List<Customer> customerList = new ArrayList<>();
			customerList.add(CUSTOMER);
			return customerList;
			
		});
		
		lenient().when(customerRepo.findCustomerByEmail(anyString())).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals(CUSTOMER_EMAIL)) {
				//Customer customer = new Customer();
				List<Appointment> apps = new ArrayList<>();
				List<Reminder> reminders = new ArrayList<>();
				CUSTOMER.setName(CUSTOMER_NAME);
				CUSTOMER.setEmail(CUSTOMER_EMAIL);
				CUSTOMER.setPassword(CUSTOMER_PASSWORD);
				CUSTOMER.setPhoneNumber(CUSTOMER_PHONE);
				CUSTOMER.setAddress(CUSTOMER_ADDRESS);
				
				//customer appointment
				TECHNICIAN.setEmail(TECH_EMAIL);
				SERVICE.setName(SERVICE_NAME);
				SERVICE.setPrice(SERVICE_PRICE);
				SERVICE.setDuration(SERVICE_DURATION);
				TIME.setStartDateTime(START_TIME);
				TIME.setEndDateTime(END_TIME);
				APP.setAppointmentID(APP_ID);
				APP.setCustomer(CUSTOMER);
				APP.setService(SERVICE);
				APP.setTechnician(TECHNICIAN);
				APP.setTimeSlot(TIME);
				apps.add(APP);
				
				CUSTOMER.setAppointments(apps);
				CUSTOMER.setReminders(reminders);
				return CUSTOMER;

			} else {
				return null;
			}
			
		});
		
		

		
		
		lenient().when(appRepo.findAppointmentByAppointmentID(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals(APP_ID)) {
				return APP;
			} else {
				return null;
			}
			
		});
		
	
		
		
		Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
			return invocation.getArgument(0);
		};
		
		lenient().when(customerRepo.save(any(Customer.class))).thenAnswer(returnParameterAsAnswer);
		
	}	
	
	
	
	
	
	@Test
	public void testCreateCustomer() {

		String customerName = "ABCD";
		String customerEmail = "somebody@gmail.com";
		String customerPassword = "fSHBlfsuesefd";
		String customerAddress = "Somewhere";
		String customerPhone = "5142253789";
		
		CustomerDto customer = null;
		
		try {
			customer = service.createCustomer(customerEmail, customerPassword, customerPhone, customerName, customerAddress);
		} catch (Exception e) {
			// Check that no error occurred
			fail(e.getMessage());
		}
		
		assertNotNull(customer);
		assertEquals(customerName, customer.getName());
		assertEquals(customerEmail, customer.getEmail());
		
		
	}

	
	
	@Test
	public void testCreateCustomerNull() {

		String customerName = "ABCD";
		String customerEmail = null;
		String customerPassword = null;
		String customerAddress = "Somewhere";
		String customerPhone = "5142253789";
		
		@SuppressWarnings("unused")
		CustomerDto customer = null;
		
		try {
			customer = service.createCustomer(customerEmail, customerPassword, customerPhone, customerName, customerAddress);
			fail();
		} catch (Exception e) {
			//an error should occur
			assertEquals("Email or password cannot be empty.", e.getMessage());
		}	
		
		
	}
	
	
	@Test
	public void testCreateCustomerDuplicate() {

		String customerName1 = "ABCD";
		String customerEmail1 = "someone@gmail.com";
		String customerPassword1 = "fSHBlfsuesefd";
		String customerAddress1 = "Somewhere";
		String customerPhone1 = "5142253789";
		
		try {
			
			service.createCustomer(customerEmail1, customerPassword1, customerPhone1, customerName1, customerAddress1);
			fail();
			
		} catch (Exception e) {
			//an error should occur
			assertEquals("Email is already taken.", e.getMessage());
		}
		
	
		
	}
	
	
	
	@Test
	public void testChangePassword() {

		String newPassword = "gsdfjsoifkl";
		
		CustomerDto customer = null;
		
		try {
			customer = service.changePassword(CUSTOMER_EMAIL, newPassword);
		} catch (Exception e) {
			// Check that no error occurred
			fail(e.getMessage());
		}
		
		assertNotNull(customer);
		assertEquals(newPassword, customer.getPassword());
		assertEquals(CUSTOMER_EMAIL, customer.getEmail());
		
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
	public void testChangePasswordNonExistentCustomer() {
				
		try {
			service.changePassword("someMail@gmail.com", "fdjhyffaskd");
			fail();
		} catch (Exception e) {
			//error should occur
			assertEquals("Customer not found.", e.getMessage());
		}
	}
	
	
	
	@Test
	public void testGetCustomer() {

		CustomerDto customer = null;
		
		try {
			customer = service.getCustomer(CUSTOMER_EMAIL);
		} catch (Exception e) {
			// Check that no error occurred
			fail();
		}
		
		assertNotNull(customer);
		assertEquals(CUSTOMER_NAME, customer.getName());
		assertEquals(CUSTOMER_EMAIL, customer.getEmail());
		
		
	}
	
	
	
	
	@Test
	public void testGetCustomerNull() {
		
		try {
			service.getCustomer(null);
			fail();
		} catch (Exception e) {
			// error should occur
			assertEquals("Email cannot be empty.", e.getMessage());
		}
		
	}
	
	
	
	@Test
	public void testGetNonExistentCustomer() {
		
		String customerEmail = "xyz@gmail.com";
		
		try {
			service.getCustomer(customerEmail);
			fail();
		} catch (Exception e) {
			// error should occur
			assertEquals("Customer not found.", e.getMessage());
		}
		
		
		
	}
	
	
	
	@Test              //not sure if this test is correct
	public void testDeleteCustomer() {
		
		try {
		
			//delete
			String message = service.deleteCustomer(CUSTOMER_EMAIL);
			assertEquals("Customer account with email " + CUSTOMER_EMAIL + " deleted.", message);
			
		} catch (Exception e) {
			fail(e.getMessage());
		}

	}
	
	
	
	
	@Test
	public void testDeleteCustomerNull() {

		try {
			//create
			service.deleteCustomer(null);
			
		} catch (Exception e) {
			// error should occur
			assertEquals("Email cannot be empty.", e.getMessage());
		}
		
	}
	
	
	
	
	@Test
	public void testDeleteNonExistentCustomer() {
		String email = "abc@gmail.com";
		
		try {
			//create
			service.deleteCustomer(email);
			
		} catch (Exception e) {
			// error should occur
			assertEquals("Customer not found.", e.getMessage());
		}
		
	}
	

	
	
	@Test
	public void testGetAllCustomers() {
		
		try {
			List<CustomerDto> customers = service.getAllCustomers();
			assertTrue(customers.stream().map(CustomerDto::getEmail).collect(Collectors.toList()).contains(CUSTOMER_EMAIL));
		} catch (Exception e) {
			fail(e.getMessage());
		}
		

	}
	
	
	
	
	@Test
	public void testViewAppointment() {
		
		try {
			
			AppointmentDto app = service.viewAppointments(CUSTOMER_EMAIL).get(0);
			assertEquals(CUSTOMER_EMAIL, app.getCustomerDto().getEmail());
			assertEquals(TECH_EMAIL, app.getTechnicianDto().getEmail());
			assertEquals(SERVICE_NAME, app.getServiceDto().getName());
			assertEquals(START_TIME, app.getTimeSlotDto().getStartDateTime());
			assertEquals(END_TIME, app.getTimeSlotDto().getEndDateTime());
			
		} catch(Exception e) {
			fail(e.getMessage());
		}
		
	}
	
	
	
	
	
	
	
}
