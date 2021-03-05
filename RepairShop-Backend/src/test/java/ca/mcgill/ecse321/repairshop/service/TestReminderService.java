package ca.mcgill.ecse321.repairshop.service;

import ca.mcgill.ecse321.repairshop.dto.ReminderDto;
import ca.mcgill.ecse321.repairshop.model.Customer;
import ca.mcgill.ecse321.repairshop.model.Reminder;
import ca.mcgill.ecse321.repairshop.model.ReminderType;
import ca.mcgill.ecse321.repairshop.repository.ReminderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;

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
    private static final Customer REMINDER_CUSTOMER = new Customer();

    @BeforeEach
    public void setMockOutput() {
        lenient().when(reminderRepository.save(any(Reminder.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
    }

    @Test // valid reminder
    public void testCreateReminder() {

        ReminderDto reminderDto = null;

        try {
            reminderDto = reminderService.createReminder(REMINDER_TIMESTAMP, REMINDER_TYPE, REMINDER_CUSTOMER);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertNotNull(reminderDto);
        assertEquals(REMINDER_TIMESTAMP, reminderDto.getDateTime());
        assertEquals(REMINDER_TYPE, reminderDto.getReminderType());
        assertEquals(REMINDER_CUSTOMER, reminderDto.getCustomer());

    }

    @Test // invalid reminder (null Timeslot)
    public void testCreateReminderNull() {

        ReminderDto reminderDto = null;

        try {
            reminderDto = reminderService.createReminder(null, REMINDER_TYPE, REMINDER_CUSTOMER);
        } catch (Exception e) {
            assertEquals("The Timestamp is mandatory", e.getMessage());
        }

        assertNull(reminderDto);

    }

}
