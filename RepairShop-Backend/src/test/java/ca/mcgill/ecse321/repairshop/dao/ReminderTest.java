package ca.mcgill.ecse321.repairshop.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Timestamp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.mcgill.ecse321.repairshop.model.Customer;
import ca.mcgill.ecse321.repairshop.model.Reminder;
import ca.mcgill.ecse321.repairshop.model.ReminderType;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ReminderTest {

	@Autowired
	private ReminderRepository reminderRepository;

	@Autowired
	private CustomerRepository customerRepository;
		
	@BeforeEach
	@AfterEach
	public void clearDatabase() {
		reminderRepository.deleteAll();
		customerRepository.deleteAll();
	}
	
	@Test
	public void testPersistAndLoadReminder() {
		
		// create customer
		String customerName = "Solomina XXukld";
		String customerEmail = "Solomin@gmail.com";
		String customerPassword = "scrum?meeting?101";
		String customerAddress = "2 UpInTheAir Street";
		String customerPhone = "333-445-3567";
		Customer customer = new Customer();
		customer.setName(customerName);
		customer.setAddress(customerAddress);
		customer.setEmail(customerEmail);
		customer.setPassword(customerPassword);
		customer.setPhoneNumber(customerPhone);
		customerRepository.save(customer);

		//create reminder
		Timestamp reminderDay = Timestamp.valueOf("2021-04-20 10:10:10.0");
		ReminderType reminderType = ReminderType.OilChange;
		Reminder reminder = new Reminder();
		reminder.setCustomer(customer);
		reminder.setDateTime(reminderDay);
		reminder.setReminderType(reminderType);
		reminder = reminderRepository.save(reminder);

		assertNotNull(reminder);
		assertEquals(customerName, reminder.getCustomer().getName());
		assertEquals(reminderType, reminder.getReminderType());
		
	}
}
