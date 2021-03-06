package ca.mcgill.ecse321.repairshop.service;

import ca.mcgill.ecse321.repairshop.dto.ReminderDto;
import ca.mcgill.ecse321.repairshop.model.Appointment;
import ca.mcgill.ecse321.repairshop.model.Customer;
import ca.mcgill.ecse321.repairshop.model.Reminder;
import ca.mcgill.ecse321.repairshop.model.ReminderType;
import ca.mcgill.ecse321.repairshop.repository.ReminderRepository;
import static  ca.mcgill.ecse321.repairshop.service.CustomerService.customerToDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class TestReminderService {

    @Mock
    private ReminderRepository reminderRepository;

    @InjectMocks
    private ReminderService reminderService;

    // test data
    private static final Timestamp REMINDER_TIMESTAMP = Timestamp.valueOf("2021-03-01 12:30:00");
    private static final ReminderType REMINDER_TYPE = ReminderType.Maintenance;
    private static final String CUSTOMER_EMAIL = "someone@mail.com";
    private static final String CUSTOMER_NAME = "Someone";
    private static final String CUSTOMER_PASSWORD = "notMyPassword";
    private static final String CUSTOMER_ADDRESS = "1 Way Street";
    private static final String CUSTOMER_PHONE_NUMBER = "111-111-1111";
    private static final List<Reminder> CUSTOMER_REMINDERS = Collections.emptyList();
    private static final List<Appointment> CUSTOMER_APPOINTMENTS = Collections.emptyList();
    private final Customer customer = new Customer();

    @BeforeEach
    public void setMockOutput() {

        lenient().when(reminderRepository.findByCustomer(any(Customer.class))).thenAnswer((InvocationOnMock invocation) -> {

            if (invocation.getArgument(0).equals(customer)) {

                Reminder reminder = new Reminder();
                reminder.setDateTime(REMINDER_TIMESTAMP);
                reminder.setReminderType(REMINDER_TYPE);
                reminder.setCustomer(customer);

                List<Reminder> reminders = new ArrayList<>();
                reminders.add(reminder);
                return reminders;

            } else return null;

        });

        lenient().when(reminderRepository.save(any(Reminder.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

    }

    @Test // valid reminder
    public void testCreateReminder() {

        customer.setEmail(CUSTOMER_EMAIL);
        customer.setName(CUSTOMER_NAME);
        customer.setPassword(CUSTOMER_PASSWORD);
        customer.setAddress(CUSTOMER_ADDRESS);
        customer.setPhoneNumber(CUSTOMER_PHONE_NUMBER);
        customer.setReminders(CUSTOMER_REMINDERS);
        customer.setAppointments(CUSTOMER_APPOINTMENTS);

        ReminderDto reminderDto = null;

        try {
            reminderDto = reminderService.createReminder(REMINDER_TIMESTAMP, REMINDER_TYPE, customer);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertNotNull(reminderDto);
        assertEquals(REMINDER_TIMESTAMP, reminderDto.getDateTime());
        assertEquals(REMINDER_TYPE, reminderDto.getReminderType());
        assertEquals(customerToDTO(customer).getEmail(), reminderDto.getCustomerDto().getEmail());

    }

    @Test // invalid reminder (null Timeslot)
    public void testCreateReminderNullTimeSlot() {

        customer.setEmail(CUSTOMER_EMAIL);
        customer.setName(CUSTOMER_NAME);
        customer.setPassword(CUSTOMER_PASSWORD);
        customer.setAddress(CUSTOMER_ADDRESS);
        customer.setPhoneNumber(CUSTOMER_PHONE_NUMBER);
        customer.setReminders(CUSTOMER_REMINDERS);
        customer.setAppointments(CUSTOMER_APPOINTMENTS);

        ReminderDto reminderDto = null;

        try {
            reminderDto = reminderService.createReminder(null, REMINDER_TYPE, customer);
        } catch (Exception e) {
            assertEquals("The Timestamp is mandatory", e.getMessage());
        }

        assertNull(reminderDto);

    }

    @Test // invalid reminder (null ReminderType)
    public void testCreateReminderNullReminderType() {

        customer.setEmail(CUSTOMER_EMAIL);
        customer.setName(CUSTOMER_NAME);
        customer.setPassword(CUSTOMER_PASSWORD);
        customer.setAddress(CUSTOMER_ADDRESS);
        customer.setPhoneNumber(CUSTOMER_PHONE_NUMBER);
        customer.setReminders(CUSTOMER_REMINDERS);
        customer.setAppointments(CUSTOMER_APPOINTMENTS);

        ReminderDto reminderDto = null;

        try {
            reminderDto = reminderService.createReminder(REMINDER_TIMESTAMP, null, customer);
        } catch (Exception e) {
            assertEquals("The ReminderType is mandatory", e.getMessage());
        }

        assertNull(reminderDto);

    }

    @Test // invalid reminder (null Customer)
    public void testCreateReminderNullCustomer() {

        ReminderDto reminderDto = null;

        try {
            reminderDto = reminderService.createReminder(REMINDER_TIMESTAMP, REMINDER_TYPE, null);
        } catch (Exception e) {
            assertEquals("The Customer is mandatory", e.getMessage());
        }

        assertNull(reminderDto);

    }

    @Test // valid customer
    public void testGetRemindersByCustomer() {

        customer.setEmail(CUSTOMER_EMAIL);
        customer.setName(CUSTOMER_NAME);
        customer.setPassword(CUSTOMER_PASSWORD);
        customer.setAddress(CUSTOMER_ADDRESS);
        customer.setPhoneNumber(CUSTOMER_PHONE_NUMBER);
        customer.setReminders(CUSTOMER_REMINDERS);
        customer.setAppointments(CUSTOMER_APPOINTMENTS);

        List<ReminderDto> reminderDtos = null;

        try {
            reminderDtos = reminderService.getRemindersByCustomer(customer);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertEquals(1, reminderDtos.size());

        // verify the only entry
        ReminderDto reminderDto = (ReminderDto) reminderDtos.toArray()[0];

        assertEquals(REMINDER_TIMESTAMP, reminderDto.getDateTime());
        assertEquals(REMINDER_TYPE, reminderDto.getReminderType());
        assertEquals(customerToDTO(customer).getEmail(), reminderDto.getCustomerDto().getEmail()); // check same customer

    }

    @Test // invalid customer (null)
    public void testGetRemindersByCustomerNull() {

        List<ReminderDto> reminderDtos = null;

        try {
            reminderDtos = reminderService.getRemindersByCustomer(null);
        } catch (Exception e) {
            assertEquals("A valid customer is required", e.getMessage());
        }

        assertNull(reminderDtos);

    }

}
