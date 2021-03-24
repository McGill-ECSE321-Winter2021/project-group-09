package ca.mcgill.ecse321.repairshop.service;

import ca.mcgill.ecse321.repairshop.dto.AppointmentDto;
import ca.mcgill.ecse321.repairshop.model.*;
import ca.mcgill.ecse321.repairshop.repository.*;

import ca.mcgill.ecse321.repairshop.service.exceptions.TimeConstraintException;
import ca.mcgill.ecse321.repairshop.service.utilities.EmailService;
import ca.mcgill.ecse321.repairshop.service.utilities.SystemTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestAppointmentService {

    // Test data - only using what is needed for the tests

    // Going to use times relative to 2021-03-01 00:00:00.0 to make them easier to understand
    private static final LocalDateTime INITIAL_TIME = LocalDateTime.parse("2021-04-01T00:00:00.0"); // Monday

    // Target appointment start time
    private static final String APP_START_TIME = Timestamp.valueOf(INITIAL_TIME.plusDays(14).plusHours(9)).toString(); // Monday
    private static final String APP_START_TIME2 = Timestamp.valueOf(INITIAL_TIME.minusDays(14)).toString(); // Past time
    private static final String APP_START_TIME3 = Timestamp.valueOf(INITIAL_TIME.plusDays(14).plusHours(13)).toString(); // Tuesday - end time out of hours
    private static final String APP_START_TIME4 = Timestamp.valueOf(INITIAL_TIME.plusDays(18).plusHours(7)).toString(); // Friday - outside hours
    private static final String APP_START_TIME5 = Timestamp.valueOf(INITIAL_TIME.plusDays(15).plusHours(7)).toString(); // Wednesday - during holiday
    private static final String APP_START_TIME6 = Timestamp.valueOf(INITIAL_TIME.plusDays(14).plusHours(11)).toString(); // Tuesday - overlaps another appointment
    private static final String APP_START_TIME7 = Timestamp.valueOf(INITIAL_TIME.plusDays(14).plusHours(13)).toString(); // Tuesday - during another appointment

    // Service
    private static final String SERVICE_NAME = "Service";
    private static final int SERVICE_DURATION = 4;
    private static final double SERVICE_PRICE = 49.99;

    // Technicians
    private static final String TECHNICIAN_EMAIL = "technician@mail.com";
    private static final String TECHNICIAN_EMAIL2 = "technician2@mail.com";
    private static final Timestamp HOURS_START = Timestamp.valueOf(INITIAL_TIME.plusHours(8)); // Monday
    private static final Timestamp HOURS_END = Timestamp.valueOf(INITIAL_TIME.plusHours(16)); // Monday
    private static final Timestamp HOURS_START2 = Timestamp.valueOf(INITIAL_TIME.plusDays(1).plusHours(10)); // Tuesday
    private static final Timestamp HOURS_END2 = Timestamp.valueOf(INITIAL_TIME.plusDays(1).plusHours(18)); // Tuesday
    private static final Timestamp HOURS_START3 = Timestamp.valueOf(INITIAL_TIME.plusDays(2).plusHours(7)); // Wednesday
    private static final Timestamp HOURS_END3 = Timestamp.valueOf(INITIAL_TIME.plusDays(2).plusHours(15)); // Wednesday
    private static final Timestamp APP_START = Timestamp.valueOf(INITIAL_TIME.plusDays(14).plusHours(12)); // Monday
    private static final Timestamp APP_END = Timestamp.valueOf(INITIAL_TIME.plusDays(14).plusHours(15)); // Monday

    // Customer
    private static final String CUSTOMER_EMAIL = "customer@mail.com";

    // Business
    private static final String BUSINESS_NAME = "Business";
    private static final Timestamp HOLIDAY_START = Timestamp.valueOf(INITIAL_TIME.plusDays(15)); // Tuesday
    private static final Timestamp HOLIDAY_END = Timestamp.valueOf(INITIAL_TIME.plusDays(15).plusHours(23)); // Tuesday

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private ServiceRepository serviceRepository;
    @Mock
    private TechnicianRepository technicianRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private BusinessRepository businessRepository;
    @Mock
    private TimeSlotRepository timeSlotRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private ReminderService reminderService;
    @InjectMocks
    private AppointmentService appointmentService;

    @BeforeEach
    public void setMockOutput() {

        SystemTime.setTest(true);
        SystemTime.setTestTime(Timestamp.valueOf(INITIAL_TIME.plusDays(11)));

        lenient().when(serviceRepository.findServiceByName(any(String.class))).thenAnswer((InvocationOnMock invocation) -> {

            if (invocation.getArgument(0).equals(SERVICE_NAME)) {

                Service service = new Service();
                service.setName(SERVICE_NAME);
                service.setPrice(SERVICE_PRICE);
                service.setDuration(SERVICE_DURATION);

                return service;

            } else throw new Exception("The provided service name is invalid");
        });

        lenient().when(customerRepository.findCustomerByEmail(any(String.class))).thenAnswer((InvocationOnMock invocation) -> {

            if (invocation.getArgument(0).equals(CUSTOMER_EMAIL)) {

                Customer customer = new Customer();
                customer.setEmail(CUSTOMER_EMAIL);
                List<Reminder> rems = new ArrayList<>();
                customer.setReminders(rems);

                return customer;

            } else throw new Exception("The provided customer email is invalid");
        });

        lenient().when(businessRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            List<Business> businesses = new ArrayList<>();
            Business business = new Business();
            business.setName(BUSINESS_NAME);
            TimeSlot holiday = new TimeSlot();
            holiday.setStartDateTime(HOLIDAY_START);
            holiday.setEndDateTime(HOLIDAY_END);
            List<TimeSlot> holidays = new ArrayList<>();
            holidays.add(holiday);
            business.setHolidays(holidays);
            businesses.add(business);

            return businesses;

        });


        lenient().when(technicianRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {

            List<Technician> technicians = new ArrayList<>();

            Technician technician = new Technician();
            technician.setEmail(TECHNICIAN_EMAIL);
            TimeSlot appSlot = new TimeSlot();
            appSlot.setStartDateTime(APP_START);
            appSlot.setEndDateTime(APP_END);
            Appointment app = new Appointment();
            app.setTimeSlot(appSlot);
            List<Appointment> apps = new ArrayList<>();
            apps.add(app);
            technician.setAppointments(apps);
            TimeSlot hours = new TimeSlot();
            hours.setStartDateTime(HOURS_START);
            hours.setEndDateTime(HOURS_END);
            TimeSlot hours2 = new TimeSlot();
            hours2.setStartDateTime(HOURS_START2);
            hours2.setEndDateTime(HOURS_END2);
            List<TimeSlot> workHours = new ArrayList<>();
            workHours.add(hours); // Monday
            workHours.add(hours2); // Tuesday
            technician.setTimeslots(workHours);

            Technician technician2 = new Technician();
            technician2.setEmail(TECHNICIAN_EMAIL2);
            TimeSlot appSlot2 = new TimeSlot();
            appSlot2.setStartDateTime(APP_START);
            appSlot2.setEndDateTime(APP_END);
            Appointment app2 = new Appointment();
            app2.setTimeSlot(appSlot2);
            List<Appointment> apps2 = new ArrayList<>();
            apps2.add(app2);
            technician2.setAppointments(apps2);
            TimeSlot hours3 = new TimeSlot();
            hours3.setStartDateTime(HOURS_START3);
            hours3.setEndDateTime(HOURS_END3);
            List<TimeSlot> workHours2 = new ArrayList<>();
            workHours2.add(hours2); // Tuesday
            workHours2.add(hours3); // Wednesday
            technician2.setTimeslots(workHours2);

            technicians.add(technician);
            technicians.add(technician2);

            return technicians;

        });

        lenient().when(technicianRepository.findById(any(String.class))).thenAnswer((InvocationOnMock invocation) -> {

            Technician tech = new Technician();
            tech.setEmail(TECHNICIAN_EMAIL);
            List<Appointment> apps = new ArrayList<>();
            apps.add(createAppointment(1L));
            tech.setAppointments(apps);
            if (invocation.getArgument(0).equals(TECHNICIAN_EMAIL)) {
                return Optional.of(tech);
            } else {
                return Optional.empty();
            }
        });

        lenient().when(customerRepository.findById(any(String.class))).thenAnswer((InvocationOnMock invocation) -> {

            Customer customer = new Customer();
            customer.setEmail(CUSTOMER_EMAIL);
            List<Appointment> apps = new ArrayList<>();
            apps.add(createAppointment(1L));
            customer.setAppointments(apps);
            List<Reminder> reminders = new ArrayList<>();
            customer.setReminders(reminders);
            customer.setAddress("1123");
            customer.setName("rick");
            customer.setPassword("123");
            if (invocation.getArgument(0).equals(CUSTOMER_EMAIL)) {
                return Optional.of(customer);
            } else {
                return Optional.empty();
            }
        });

        lenient().when(appointmentRepository.findById(any(Long.class))).thenAnswer((InvocationOnMock invocation) -> {
            //create service
            Service service = new Service();
            service.setName(SERVICE_NAME);
            service.setPrice(SERVICE_PRICE);
            service.setDuration(SERVICE_DURATION);
            //create tech
            Technician tech = new Technician();
            tech.setEmail(TECHNICIAN_EMAIL);
            List<Appointment> apps = new ArrayList<>();
            apps.add(createAppointment(1L));
            tech.setAppointments(apps);
            //create customer
            Customer customer = new Customer();
            customer.setEmail(CUSTOMER_EMAIL);
            customer.setAppointments(apps);
            Appointment app = createAppointment(1L);
            app.setTechnician(tech);
            app.setCustomer(customer);
            app.setService(service);
            if (invocation.getArgument(0).equals(1L)) {
                return Optional.of(app);
            } else {
                return Optional.empty();
            }
        });


        lenient().when(appointmentRepository.save(any(Appointment.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
        lenient().when(timeSlotRepository.save(any(TimeSlot.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));


    }




    // Test creating an appointment

    @Test // valid appointment
    public void testCreateAppointment() {

        AppointmentDto appointmentDto = null;

        try {
            appointmentDto = appointmentService.createAppointment(APP_START_TIME, SERVICE_NAME, CUSTOMER_EMAIL);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertNotNull(appointmentDto);
        assertEquals(APP_START_TIME, appointmentDto.getTimeSlotDto().getStartDateTime().toString());
        assertEquals(SERVICE_NAME, appointmentDto.getServiceDto().getName());
        assertEquals(TECHNICIAN_EMAIL, appointmentDto.getTechnicianDto().getEmail());
        assertEquals(CUSTOMER_EMAIL, appointmentDto.getCustomerDto().getEmail());

    }

    @Test // invalid appointment - invalid start time (null timestamp)
    public void testCreateAppointmentInvalidTimestampNull() {

        AppointmentDto appointmentDto = null;

        try {
            appointmentDto = appointmentService.createAppointment(null, SERVICE_NAME, CUSTOMER_EMAIL);
            fail();
        } catch (Exception e) {
            assertEquals("The Timestamp is mandatory", e.getMessage());
        }

        assertNull(appointmentDto);
    }

    @Test // invalid appointment - invalid start time (empty timestamp)
    public void testCreateAppointmentInvalidTimestampEmpty() {

        AppointmentDto appointmentDto = null;

        try {
            appointmentDto = appointmentService.createAppointment("", SERVICE_NAME, CUSTOMER_EMAIL);
            fail();
        } catch (Exception e) {
            assertEquals("The Timestamp is mandatory", e.getMessage());
        }

        assertNull(appointmentDto);
    }

    @Test // invalid appointment - invalid start time (wrong format for timestamp)
    public void testCreateAppointmentInvalidTimestamp() {

        AppointmentDto appointmentDto = null;

        try {
            appointmentDto = appointmentService.createAppointment("notATimestamp", SERVICE_NAME, CUSTOMER_EMAIL);
            fail();
        } catch (Exception e) {
            assertEquals("The provided Timestamp is invalid", e.getMessage());
        }

        assertNull(appointmentDto);
    }

    @Test // invalid appointment - invalid start time (time has passed)
    public void testCreateAppointmentInvalidTimestampInPast() {

        AppointmentDto appointmentDto = null;

        try {
            appointmentDto = appointmentService.createAppointment(APP_START_TIME2, SERVICE_NAME, CUSTOMER_EMAIL);
            fail();
        } catch (Exception e) {
            assertEquals("The provided Timestamp is invalid", e.getMessage());
        }

        assertNull(appointmentDto);
    }

    @Test // invalid appointment - invalid start time (overlaps end of technician's hours)
    public void testCreateAppointmentInvalidTimestampEndHours() {

        AppointmentDto appointmentDto = null;

        try {
            appointmentDto = appointmentService.createAppointment(APP_START_TIME3, SERVICE_NAME, CUSTOMER_EMAIL);
            fail();
        } catch (Exception e) {
            assertEquals("The appointment cannot be booked", e.getMessage());
        }

        assertNull(appointmentDto);
    }

    @Test // invalid appointment - invalid start time (outside of technician's hours)
    public void testCreateAppointmentInvalidTimestampOutsideHours() {

        AppointmentDto appointmentDto = null;

        try {
            appointmentDto = appointmentService.createAppointment(APP_START_TIME4, SERVICE_NAME, CUSTOMER_EMAIL);
            fail();
        } catch (Exception e) {
            assertEquals("The appointment cannot be booked", e.getMessage());
        }

        assertNull(appointmentDto);
    }

    @Test // invalid appointment - invalid start time (during holiday)
    public void testCreateAppointmentInvalidTimestampDuringHoliday() {

        AppointmentDto appointmentDto = null;

        try {
            appointmentDto = appointmentService.createAppointment(APP_START_TIME5, SERVICE_NAME, CUSTOMER_EMAIL);
            fail();
        } catch (Exception e) {
            assertEquals("The appointment cannot be booked", e.getMessage());
        }

        assertNull(appointmentDto);
    }

    @Test // invalid appointment - invalid start time (overlaps with another appointment)
    public void testCreateAppointmentInvalidTimestampOverlapAppointment() {

        AppointmentDto appointmentDto = null;

        try {
            appointmentDto = appointmentService.createAppointment(APP_START_TIME6, SERVICE_NAME, CUSTOMER_EMAIL);
            fail();
        } catch (Exception e) {
            assertEquals("The appointment cannot be booked", e.getMessage());
        }

        assertNull(appointmentDto);
    }

    @Test // invalid appointment - invalid start time (during another appointment)
    public void testCreateAppointmentInvalidTimestampDuringAppointment() {

        AppointmentDto appointmentDto = null;

        try {
            appointmentDto = appointmentService.createAppointment(APP_START_TIME7, SERVICE_NAME, CUSTOMER_EMAIL);
            fail();
        } catch (Exception e) {
            assertEquals("The appointment cannot be booked", e.getMessage());
        }

        assertNull(appointmentDto);
    }

    @Test // invalid appointment - invalid service (null service)
    public void testCreateAppointmentInvalidServiceNull() {

        AppointmentDto appointmentDto = null;

        try {
            appointmentDto = appointmentService.createAppointment(APP_START_TIME, null, CUSTOMER_EMAIL);
            fail();
        } catch (Exception e) {
            assertEquals("The service name is mandatory", e.getMessage());
        }

        assertNull(appointmentDto);
    }

    @Test // invalid appointment - invalid service (empty service)
    public void testCreateAppointmentInvalidServiceEmpty() {

        AppointmentDto appointmentDto = null;

        try {
            appointmentDto = appointmentService.createAppointment(APP_START_TIME, "", CUSTOMER_EMAIL);
            fail();
        } catch (Exception e) {
            assertEquals("The service name is mandatory", e.getMessage());
        }

        assertNull(appointmentDto);
    }

    @Test // invalid appointment - invalid service
    public void testCreateAppointmentInvalidService() {

        AppointmentDto appointmentDto = null;

        try {
            appointmentDto = appointmentService.createAppointment(APP_START_TIME, "notAService", CUSTOMER_EMAIL);
            fail();
        } catch (Exception e) {
            assertEquals("The provided service name is invalid", e.getMessage());
        }

        assertNull(appointmentDto);
    }

    @Test // invalid appointment - invalid customer (null customer)
    public void testCreateAppointmentInvalidCustomerNull() {

        AppointmentDto appointmentDto = null;

        try {
            appointmentDto = appointmentService.createAppointment(APP_START_TIME, SERVICE_NAME, null);
            fail();
        } catch (Exception e) {
            assertEquals("The customer is mandatory", e.getMessage());
        }

        assertNull(appointmentDto);
    }

    @Test // invalid appointment - invalid customer (empty customer)
    public void testCreateAppointmentInvalidCustomerEmpty() {

        AppointmentDto appointmentDto = null;

        try {
            appointmentDto = appointmentService.createAppointment(APP_START_TIME, SERVICE_NAME, "");
            fail();
        } catch (Exception e) {
            assertEquals("The customer is mandatory", e.getMessage());
        }

        assertNull(appointmentDto);
    }

    @Test // invalid appointment - invalid customer
    public void testCreateAppointmentInvalidCustomer() {

        AppointmentDto appointmentDto = null;

        try {
            appointmentDto = appointmentService.createAppointment(APP_START_TIME, SERVICE_NAME, "notACustomer");
            fail();
        } catch (Exception e) {
            assertEquals("The provided customer email is invalid", e.getMessage());
        }

        assertNull(appointmentDto);
    }


    //  Test getting all possible appointments

    @Test // valid set of appointments
    public void testGetPossibleAppointments() {

        List<TimeSlot> possibleAppointments = null;

        try {
            possibleAppointments = appointmentService.getPossibleAppointments(INITIAL_TIME.plusDays(14).toLocalDate().toString(), SERVICE_NAME);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertNotNull(possibleAppointments);
        // 18 timeslots are expected from the test data for the target week (see visualization and test output)
        assertEquals(18, possibleAppointments.size());
    }

    @Test // valid set of appointments (next week - changes holiday and app)
    public void testGetPossibleAppointments2() {

        // Modify time for this test (the following week)
        SystemTime.setTestTime(Timestamp.valueOf(INITIAL_TIME.plusDays(18)));

        List<TimeSlot> possibleAppointments = null;

        try {
            possibleAppointments = appointmentService.getPossibleAppointments(INITIAL_TIME.plusDays(21).toLocalDate().toString(), SERVICE_NAME);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertNotNull(possibleAppointments);
        // 39 timeslots are expected from the test data for the target week (see visualization and test output)
        assertEquals(39, possibleAppointments.size());

        // Reset the time
        SystemTime.setTestTime(Timestamp.valueOf(INITIAL_TIME.plusDays(11)));
    }

    @Test // valid start date (null date uses current date)
    public void testGetPossibleAppointmentsInvalidStartDateNull() {

        List<TimeSlot> possibleAppointments = null;

        try {
            possibleAppointments = appointmentService.getPossibleAppointments(null, SERVICE_NAME);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertNotNull(possibleAppointments);
    }

    @Test // valid start date (empty date uses current date)
    public void testGetPossibleAppointmentsInvalidStartDateEmpty() {

        List<TimeSlot> possibleAppointments = null;

        try {
            possibleAppointments = appointmentService.getPossibleAppointments("", SERVICE_NAME);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertNotNull(possibleAppointments);
    }

    @Test // invalid start date (wrong format)
    public void testGetPossibleAppointmentsInvalidStartDate() {

        List<TimeSlot> possibleAppointments = null;

        try {
            possibleAppointments = appointmentService.getPossibleAppointments("notADate", SERVICE_NAME);
            fail();
        } catch (Exception e) {
            assertEquals("The provided start date is invalid", e.getMessage());
        }

        assertNull(possibleAppointments);
    }

    @Test // invalid start date (past date)
    public void testGetPossibleAppointmentsInvalidStartDatePast() {

        List<TimeSlot> possibleAppointments = null;

        try {
            possibleAppointments = appointmentService.getPossibleAppointments(APP_START_TIME2, SERVICE_NAME);
            fail();
        } catch (Exception e) {
            assertEquals("The provided start date is invalid", e.getMessage());
        }

        assertNull(possibleAppointments);
    }

    @Test // invalid service name (null)
    public void testGetPossibleAppointmentsInvalidServiceNameNull() {

        List<TimeSlot> possibleAppointments = null;

        try {
            possibleAppointments = appointmentService.getPossibleAppointments(INITIAL_TIME.plusDays(14).toLocalDate().toString(), null);
            fail();
        } catch (Exception e) {
            assertEquals("The service name is mandatory", e.getMessage());
        }

        assertNull(possibleAppointments);
    }

    @Test // invalid service name (empty)
    public void testGetPossibleAppointmentsInvalidServiceNameEmpty() {

        List<TimeSlot> possibleAppointments = null;

        try {
            possibleAppointments = appointmentService.getPossibleAppointments(INITIAL_TIME.plusDays(14).toLocalDate().toString(), "");
            fail();
        } catch (Exception e) {
            assertEquals("The service name is mandatory", e.getMessage());
        }

        assertNull(possibleAppointments);
    }

    @Test // invalid service name (does not exist)
    public void testGetPossibleAppointmentsInvalidServiceName() {

        List<TimeSlot> possibleAppointments = null;

        try {
            possibleAppointments = appointmentService.getPossibleAppointments(INITIAL_TIME.plusDays(14).toLocalDate().toString(), "notAService");
            fail();
        } catch (Exception e) {
            assertEquals("The provided service name is invalid", e.getMessage());
        }

        assertNull(possibleAppointments);
    }

    @Test
    public void testCancelAppointment() {
        try {
            appointmentService.cancelAppointment(1L);
            verify(technicianRepository, times(1)).save(any());
            verify(customerRepository, times(1)).save(any());
            verify(appointmentRepository, times(1)).delete(any());
            verify(technicianRepository, times(1)).findById(TECHNICIAN_EMAIL);
            verify(customerRepository, times(1)).findById(CUSTOMER_EMAIL);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testCancelAppointmentWrongAppID() {
        try {
            appointmentService.cancelAppointment(2L);
            fail();
        } catch (Exception e) {
            assertEquals("Cannot find the appointment by ID.", e.getMessage());
        }
    }

    @Test
    public void testCancelAppointmentTooLate() {
        SystemTime.setTestTime(Timestamp.valueOf(INITIAL_TIME.plusDays(21)));
        assertThrows(TimeConstraintException.class,
                () -> appointmentService.cancelAppointment(1L)
        );
    }

    private Appointment createAppointment(Long id) {
        Appointment appointment = new Appointment();
        appointment.setAppointmentID(id);
        appointment.setTimeSlot(createTimeSlot());
        return appointment;
    }

    private TimeSlot createTimeSlot() {
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setStartDateTime(Timestamp.valueOf(INITIAL_TIME.plusDays(21)));
        timeSlot.setEndDateTime(Timestamp.valueOf(INITIAL_TIME.plusDays(22)));
        timeSlot.setTimeSlotID(1L);
        return timeSlot;
    }

}
