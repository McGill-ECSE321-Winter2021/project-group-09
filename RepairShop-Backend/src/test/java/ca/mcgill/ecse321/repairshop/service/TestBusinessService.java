package ca.mcgill.ecse321.repairshop.service;

import ca.mcgill.ecse321.repairshop.dto.BusinessDto;
import ca.mcgill.ecse321.repairshop.dto.TimeSlotDto;
import ca.mcgill.ecse321.repairshop.model.Appointment;
import ca.mcgill.ecse321.repairshop.model.Business;
import ca.mcgill.ecse321.repairshop.model.Technician;
import ca.mcgill.ecse321.repairshop.model.TimeSlot;
import ca.mcgill.ecse321.repairshop.repository.BusinessRepository;
import ca.mcgill.ecse321.repairshop.repository.TechnicianRepository;
import ca.mcgill.ecse321.repairshop.repository.TimeSlotRepository;
import ca.mcgill.ecse321.repairshop.service.utilities.SystemTime;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;

import org.mockito.stubbing.Answer;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class TestBusinessService {

    @Mock
    private BusinessRepository businessRepository;

    @Mock
    private TimeSlotRepository timeSlotRepository;

    @Mock
    private TechnicianRepository technicianRepository;

    @InjectMocks
    private BusinessService businessService;

    // Test data
    private static final long BUSINESS_ID = 123;
    private static final String BUSINESS_NAME = "TestBusiness";

    private static final LocalDateTime INITIAL_TIME = LocalDateTime.parse("2021-03-01T00:00:00.0"); // Monday

    private static final String BUSINESS_ADDRESS = "123 Business Street, Montreal";
    private static final String BUSINESS_EMAIL = "bestBusiness@gmail.com";
    private static final String BUSINESS_PHONE_NUMBER = "(123)-456-7890";
    private static final int BUSINESS_NUMBER_OF_REPAIR_SPOTS = 2;
    private static final Timestamp START_TIME = Timestamp.valueOf(INITIAL_TIME.plusDays(15)); // Tuesday
    private static final Timestamp END_TIME = Timestamp.valueOf(INITIAL_TIME.plusDays(15).plusHours(23)); // Tuesday

    // Test data for getting available repair spots

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

    @BeforeEach
    public void setMockOutput() {

        SystemTime.setTest(true);
        SystemTime.setTestTime(Timestamp.valueOf(INITIAL_TIME.plusDays(15).plusHours(1)));

        lenient().when(businessRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {

            //TimeSlot
            TimeSlot timeSlot = new TimeSlot();
            timeSlot.setStartDateTime(START_TIME);
            timeSlot.setEndDateTime(END_TIME);
            List<TimeSlot> holidaysList = new ArrayList<>();
            holidaysList.add(timeSlot);

            //CREATE BUSINESS
            Business business = new Business();
            business.setBusinessID(BUSINESS_ID);
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

        lenient()
                .when(businessRepository.findBusinessByBusinessID(anyLong()))
                .thenAnswer(
                        (InvocationOnMock invocation) -> {
                            if (invocation.getArgument(0).equals(BUSINESS_ID)) {

                                //TimeSlot
                                TimeSlot timeSlot = new TimeSlot();
                                timeSlot.setStartDateTime(START_TIME);
                                timeSlot.setEndDateTime(END_TIME);
                                List<TimeSlot> holidaysList = new ArrayList<>();
                                holidaysList.add(timeSlot);

                                //CREATE BUSINESS
                                Business business = new Business();
                                business.setBusinessID(BUSINESS_ID);
                                business.setHolidays(holidaysList);
                                business.setName(BUSINESS_NAME);
                                business.setEmail(BUSINESS_EMAIL);
                                business.setAddress(BUSINESS_ADDRESS);
                                business.setPhoneNumber(BUSINESS_PHONE_NUMBER);
                                business.setNumberOfRepairSpots(BUSINESS_NUMBER_OF_REPAIR_SPOTS);
                                return business;
                            } else {
                                return null;
                            }
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

        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> invocation.getArgument(0);
        lenient().when(businessRepository.save(any(Business.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(timeSlotRepository.save(any(TimeSlot.class))).thenAnswer(returnParameterAsAnswer);
    }

    @Test
    public void testCreateBusiness() {

        BusinessDto business = null;
        String businessName = "Best Business in the world";
        try {
            business = businessService.createBusiness(businessName, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, BUSINESS_EMAIL);

        } catch (Exception e) {
            fail();
        }
   
        assertEquals(businessName, business.getName());
        assertEquals(BUSINESS_ADDRESS, business.getAddress());
        assertEquals(BUSINESS_PHONE_NUMBER, business.getPhoneNumber());
        assertEquals(BUSINESS_EMAIL, business.getEmail());
        assertEquals(0, business.getNumberOfRepairSpots()); // new business default is 0 (since there are not technicians)

        assertNotNull(business);

    }

    @Test
    public void testMissingNameCreateBusiness() {

        String name = "";
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.createBusiness(name, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, BUSINESS_EMAIL);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);
        assertEquals("Business name cannot be empty!", error);
    }

    @Test
    public void testMissingAddressCreateBusiness() {
        String address = "";
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.createBusiness(BUSINESS_NAME, address, BUSINESS_PHONE_NUMBER, BUSINESS_EMAIL);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);
        assertEquals("Address cannot be empty!", error);
    }

    @Test
    public void testMissingPhoneNumberCreateBusiness() {
        String phoneNumber = "";
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.createBusiness(BUSINESS_NAME, BUSINESS_ADDRESS, phoneNumber, BUSINESS_EMAIL);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);
        assertEquals("Phone number cannot be empty!", error);
    }

    @Test
    public void testMissingEmailCreateBusiness() {
        String email = "";
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.createBusiness(BUSINESS_NAME, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, email);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);
        assertEquals("Email cannot be empty!", error);
    }

    @Test

    public void testInvalidEmailNoAtSignCreateBusiness() {
        String email = "bestBusinessgmail.com";
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.createBusiness(BUSINESS_NAME, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, email);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);
        assertEquals("Invalid email", error);
    }

    @Test
    public void testInvalidEmailCreateBusiness() {
        String email = "Not an email";
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.createBusiness(BUSINESS_NAME, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, email);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);
        assertEquals("Invalid email", error);
    }

    @Test
    public void testInvalidEmailNoDotCreateBusiness() {
        String email = "bestBusiness@gmailcom";
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.createBusiness(BUSINESS_NAME, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, email);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);
        assertEquals("Invalid email", error);
    }

    @Test
    public void testNullNameCreateBusiness() {
        String nullBusinessName = null;
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.createBusiness(nullBusinessName, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, BUSINESS_EMAIL);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);
        assertEquals("Business name cannot be empty!", error);
    }

    @Test
    public void testNullAddressCreateBusiness() {
        String nullAddress = null;
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.createBusiness(BUSINESS_NAME, nullAddress, BUSINESS_PHONE_NUMBER, BUSINESS_EMAIL);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);
        assertEquals("Address cannot be empty!", error);
    }

    @Test
    public void testNullPhoneNumberCreateBusiness() {
        String nullPhoneNumber = null;
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.createBusiness(BUSINESS_NAME, BUSINESS_ADDRESS, nullPhoneNumber, BUSINESS_EMAIL);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);
        assertEquals("Phone number cannot be empty!", error);
    }

    @Test
    public void testNullEmailCreateBusiness() {
        String nullEmail = null;
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.createBusiness(BUSINESS_NAME, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, nullEmail);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);
        assertEquals("Email cannot be empty!", error);
    }

    @Test
    public void testGetBusiness() {
        BusinessDto businessDto;
        try {
            businessDto = businessService.getBusiness();
            assertEquals(BUSINESS_NAME, businessDto.getName());
            assertEquals(BUSINESS_ADDRESS, businessDto.getAddress());
            assertEquals(BUSINESS_PHONE_NUMBER, businessDto.getPhoneNumber());
            assertEquals(BUSINESS_EMAIL, businessDto.getEmail());
            assertEquals(BUSINESS_NUMBER_OF_REPAIR_SPOTS, businessDto.getNumberOfRepairSpots());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testUpdateBusiness() {


        BusinessDto business;
        String newName = "Update name";
        String newAddress = "Update address";
        String newPhoneNumber = "Update phone number";
        String newEmail = "niceEmail@mcgill.ca";
        int newNbRepairSpots = 100;


        try {
            business = businessService.updateBusiness(newName, newAddress, newPhoneNumber, newEmail, newNbRepairSpots);
            assertEquals(newName, business.getName());
            assertEquals(newAddress, business.getAddress());
            assertEquals(newPhoneNumber, business.getPhoneNumber());
            assertEquals(newEmail, business.getEmail());
            assertEquals(newNbRepairSpots, business.getNumberOfRepairSpots());
            assertNotNull(business);
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void testMissingNameUpdateBusiness() {
        String newName = "";
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.updateBusiness(newName, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, BUSINESS_EMAIL, BUSINESS_NUMBER_OF_REPAIR_SPOTS);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);
        assertEquals("Business name cannot be empty!", error);
    }

    @Test
    public void testMissingAddressUpdateBusiness() {
        String address = "";
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.updateBusiness(BUSINESS_NAME, address, BUSINESS_PHONE_NUMBER, BUSINESS_EMAIL, BUSINESS_NUMBER_OF_REPAIR_SPOTS);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);
        assertEquals("Address cannot be empty!", error);
    }

    @Test
    public void testMissingPhoneNumberUpdateBusiness() {
        String phoneNumber = "";
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.updateBusiness(BUSINESS_NAME, BUSINESS_ADDRESS, phoneNumber, BUSINESS_EMAIL, BUSINESS_NUMBER_OF_REPAIR_SPOTS);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);
        assertEquals("Phone number cannot be empty!", error);
    }


    @Test
    public void testMissingEmailUpdateBusiness() {
        String email = "";
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.updateBusiness(BUSINESS_NAME, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, email, BUSINESS_NUMBER_OF_REPAIR_SPOTS);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);

        assertEquals("Email cannot be empty!", error);
    }

    @Test

    public void testInvalidEmailNoAtSignUpdateBusiness() {
        String email = "bestBusinessgmail.com";
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.updateBusiness(BUSINESS_NAME, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, email, BUSINESS_NUMBER_OF_REPAIR_SPOTS);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);

        assertEquals("Invalid email", error);
    }

    @Test
    public void testInvalidEmailUpdateBusiness() {
        String email = "Not an email";
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.updateBusiness(BUSINESS_NAME, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, email, BUSINESS_NUMBER_OF_REPAIR_SPOTS);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);
        assertEquals("Invalid email", error);
    }

    @Test
    public void testInvalidEmailNoDotUpdateBusiness() {
        String email = "bestBusiness@gmailcom";
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.updateBusiness(BUSINESS_NAME, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, email, BUSINESS_NUMBER_OF_REPAIR_SPOTS);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);
        assertEquals("Invalid email", error);
    }

    /*
    @Test
    public void testNullIDUpdateBusiness() {
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.updateBusiness(BUSINESS_NAME, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, BUSINESS_EMAIL, BUSINESS_NUMBER_OF_REPAIR_SPOTS);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);
        assertEquals("Business ID cannot be empty!", error);
    }

	*/

    @Test
    public void testNullNameUpdateBusiness() {
        String nullBusinessName = null;
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.updateBusiness(nullBusinessName, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, BUSINESS_EMAIL, BUSINESS_NUMBER_OF_REPAIR_SPOTS);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);
        assertEquals("Business name cannot be empty!", error);
    }

    @Test
    public void testNullAddressUpdateBusiness() {
        String nullAddress = null;
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.updateBusiness(BUSINESS_NAME, nullAddress, BUSINESS_PHONE_NUMBER, BUSINESS_EMAIL, BUSINESS_NUMBER_OF_REPAIR_SPOTS);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);
        assertEquals("Address cannot be empty!", error);
    }

    @Test
    public void testNullPhoneNumberUpdateBusiness() {
        String nullPhoneNumber = null;
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.updateBusiness(BUSINESS_NAME, BUSINESS_ADDRESS, nullPhoneNumber, BUSINESS_EMAIL, BUSINESS_NUMBER_OF_REPAIR_SPOTS);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);
        assertEquals("Phone number cannot be empty!", error);
    }

    @Test
    public void testNullEmailUpdateBusiness() {
        String nullEmail = null;
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.updateBusiness(BUSINESS_NAME, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, nullEmail, BUSINESS_NUMBER_OF_REPAIR_SPOTS);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);
        assertEquals("Email cannot be empty!", error);
    }

    @Test
    public void testUpdateNbRepairSpots() {

        BusinessDto business = null;
        try {
            business = businessService.updateNbRepairSpots(BUSINESS_NUMBER_OF_REPAIR_SPOTS);

        } catch (Exception e) {
            fail();
        }

        assertNotNull(business);
        assertEquals(BUSINESS_NUMBER_OF_REPAIR_SPOTS, business.getNumberOfRepairSpots());
    }

    @Test
    public void testInvalidUpdateNbRepairSpots() {
        int negativeNb = -20;
        BusinessDto business = null;
        String error = null;
        try {
            business = businessService.updateNbRepairSpots(negativeNb);

        } catch (Exception e) {
            error = e.getMessage();
        }

        assertNull(business);
        assertEquals("The number of repair spots cannot be negative", error);
    }
    

    @Test
    public void testAddHoliday() {
        BusinessDto businessDto = null;
        Timestamp startTime = Timestamp.valueOf("2021-04-02 10:00:00");
        Timestamp endTime = Timestamp.valueOf("2021-05-14 09:00:00");

        try {
            businessDto = businessService.addHoliday(startTime, endTime);
            System.out.println("BusinessDto: " + businessDto);
            System.out.println("Holidays: " + businessDto.getHolidays());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertEquals(startTime, businessDto.getHolidays().get(1).getStartDateTime());
        assertEquals(endTime, businessDto.getHolidays().get(1).getEndDateTime());
        assertEquals(2, businessDto.getHolidays().size());
    }

    @Test
    public void testDeleteHoliday() {
    	// first creating a holiday before proceeding to delete it
        Timestamp startTime = Timestamp.valueOf("2020-12-23 21:00:00");
        Timestamp endTime = Timestamp.valueOf("2020-12-26 07:00:00");
        TimeSlot newHoliday = new TimeSlot();
        newHoliday.setStartDateTime(startTime);
        newHoliday.setEndDateTime(endTime);
        String message ="";
        try {
        	
           businessService.addHoliday(startTime, endTime);
            message = businessService.deleteHoliday( startTime, endTime);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertEquals("The holiday was successfully deleted", message);
    }
    
    @Test
    public void testGetHolidays() {

        List<TimeSlotDto> holidaysDtoList = null;
        try {
            holidaysDtoList = businessService.getAllHolidays();
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertEquals(1, holidaysDtoList.size());
        assertEquals(START_TIME, holidaysDtoList.get(0).getStartDateTime());
        assertEquals(END_TIME, holidaysDtoList.get(0).getEndDateTime());
    }

    @Test
    public void testGetAvailableSpots() {

        // Checking when one technician has work hours, but has an appointment booked (is unavailable) --> should get 0

        int numSpots = -1;

        try {
            numSpots = businessService.getAvailableRepairSpots();
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertEquals(0, numSpots);

    }

    @Test
    public void testGetAvailableSpots2() {

        SystemTime.setTestTime(Timestamp.valueOf(INITIAL_TIME.plusDays(14).plusHours(9)));

        // Checking when one technician has work hours --> should get 1

        int numSpots = -1;

        try {
            numSpots = businessService.getAvailableRepairSpots();
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertEquals(1, numSpots);

    }

}