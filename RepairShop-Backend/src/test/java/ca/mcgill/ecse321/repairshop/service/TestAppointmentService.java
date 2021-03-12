package ca.mcgill.ecse321.repairshop.service;

import ca.mcgill.ecse321.repairshop.dto.AppointmentDto;
import ca.mcgill.ecse321.repairshop.model.*;
import ca.mcgill.ecse321.repairshop.repository.*;
import ca.mcgill.ecse321.repairshop.service.exceptions.TimeConstraintException;
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
    private static final LocalDateTime INITIAL_TIME = LocalDateTime.parse("2021-03-01T00:00:00.0");
    // Target appointment start time
    private static final String APP_START_TIME = "2021-02-02 09:00:00.0";
    // Service
    private static final String SERVICE_NAME = "Service";
    private static final int SERVICE_DURATION = 2;
    private static final double SERVICE_PRICE = 49.99;
    // Technicians
    private static final String TECHNICIAN_EMAIL = "technician@mail.com";
    private static final String TECHNICIAN_EMAIL2 = "technician2@mail.com";
    private static final Timestamp HOURS_START = Timestamp.valueOf("2021-03-15 10:00:00.0");
    private static final Timestamp HOURS_END = Timestamp.valueOf("2021-03-15 14:00:00.0");
    private static final Timestamp HOURS_START2 = Timestamp.valueOf("2021-02-02 08:00:00.0");
    private static final Timestamp HOURS_END2 = Timestamp.valueOf("2021-02-02 16:00:00.0");
    private static final Timestamp APP_START = Timestamp.valueOf("2021-02-02 13:00:00.0");
    private static final Timestamp APP_END = Timestamp.valueOf("2021-02-02 15:00:00.0");
    // Customer
    private static final String CUSTOMER_EMAIL = "customer@mail.com";
    // Business
    private static final String BUSINESS_NAME = "Business";
    private static final Timestamp HOLIDAY_START = Timestamp.valueOf("2021-04-15 10:00:00.0");
    private static final Timestamp HOLIDAY_END = Timestamp.valueOf("2021-04-15 14:00:00.0");
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

                return customer;

            } else throw new Exception("The provided customer email is invalid");
        });

        lenient().when(businessRepository.findBusinessByName(any(String.class))).thenAnswer((InvocationOnMock invocation) -> {

            if (invocation.getArgument(0).equals(BUSINESS_NAME)) {

                Business business = new Business();
                business.setName(BUSINESS_NAME);
                TimeSlot holiday = new TimeSlot();
                holiday.setStartDateTime(HOLIDAY_START);
                holiday.setEndDateTime(HOLIDAY_END);
                List<TimeSlot> holidays = new ArrayList<>();
                holidays.add(holiday);
                business.setHolidays(holidays);

                return business;

            } else throw new Exception("The provided business name is invalid");
        });

        lenient().when(technicianRepository.findTechnicianByEmail(any(String.class))).thenAnswer((InvocationOnMock invocation) -> {

            if (invocation.getArgument(0).equals(TECHNICIAN_EMAIL)) {

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
                workHours.add(hours);
                workHours.add(hours2);
                technician.setTimeslots(workHours);

                return technician;

            } else if (invocation.getArgument(0).equals(TECHNICIAN_EMAIL2)) {

                Technician technician2 = new Technician();
                technician2.setEmail(TECHNICIAN_EMAIL);
                TimeSlot appSlot = new TimeSlot();
                appSlot.setStartDateTime(APP_START);
                appSlot.setEndDateTime(APP_END);
                Appointment app = new Appointment();
                app.setTimeSlot(appSlot);
                List<Appointment> apps = new ArrayList<>();
                apps.add(app);
                technician2.setAppointments(apps);
                TimeSlot hours = new TimeSlot();
                hours.setStartDateTime(HOURS_START);
                hours.setEndDateTime(HOURS_END);
                List<TimeSlot> workHours = new ArrayList<>();
                workHours.add(hours);
                technician2.setTimeslots(workHours);

                return technician2;

            } else throw new Exception("A technician's email is invalid");
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
            if (invocation.getArgument(0).equals(CUSTOMER_EMAIL)) {
                return Optional.of(customer);
            } else {
                return Optional.empty();
            }
        });

        lenient().when(appointmentRepository.findById(any(Long.class))).thenAnswer((InvocationOnMock invocation) -> {

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
            if (invocation.getArgument(0).equals(1L)) {
                return Optional.of(app);
            } else {
                return Optional.empty();
            }
        });


        lenient().when(appointmentRepository.save(any(Appointment.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

    }

    @Test // valid appointment
    public void testCreateAppointment() {

        AppointmentDto appointmentDto = null;

        try {
            appointmentDto = appointmentService.createAppointment(APP_START_TIME, SERVICE_NAME, TECHNICIAN_EMAIL + ", " + TECHNICIAN_EMAIL2, CUSTOMER_EMAIL, BUSINESS_NAME);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertNotNull(appointmentDto);
        assertEquals(APP_START_TIME, appointmentDto.getTimeSlotDto().getStartDateTime().toString());
        assertEquals(SERVICE_NAME, appointmentDto.getServiceDto().getName());
        assertEquals(TECHNICIAN_EMAIL, appointmentDto.getTechnicianDto().getEmail());
        assertEquals(CUSTOMER_EMAIL, appointmentDto.getCustomerDto().getEmail());

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
