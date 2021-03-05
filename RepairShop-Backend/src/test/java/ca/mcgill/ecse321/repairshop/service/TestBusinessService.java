package ca.mcgill.ecse321.repairshop.service;

import ca.mcgill.ecse321.repairshop.dto.BusinessDto;
import ca.mcgill.ecse321.repairshop.dto.TimeSlotDto;
import ca.mcgill.ecse321.repairshop.model.Business;
import ca.mcgill.ecse321.repairshop.model.TimeSlot;
import ca.mcgill.ecse321.repairshop.repository.BusinessRepository;
import ca.mcgill.ecse321.repairshop.repository.TimeSlotRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.mockito.stubbing.Answer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class TestBusinessService {
    @Mock
    private BusinessRepository businessRepository;

    @Mock
    private TimeSlotRepository timeSlotRepository;

    @InjectMocks
    private BusinessService businessService;

    private static final long BUSINESS_ID = 123;
    private static final String BUSINESS_NAME = "TestBusiness";
    private static final String NONEXISTING_KEY = "NotABusiness";

    //Test data
    private static final String BUSINESS_ADDRESS = "123 Business Street, Montreal";
    private static final String BUSINESS_EMAIL = "bestBusiness@gmail.com";
    private static final String BUSINESS_PHONE_NUMBER = "(123)-456-7890";
    private static final int BUSINESS_NUMBER_OF_REPAIR_SPOTS = 2;
    private static final Timestamp START_TIME = Timestamp.valueOf("2021-12-02 10:00:00");
    private static final Timestamp END_TIME = Timestamp.valueOf("2021-12-30 11:00:00");

    private static final List<TimeSlotDto> BUSINESS_HOLIDAYS = new ArrayList<>();

    @BeforeEach
    public void setMockOutput() {
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

        // Whenever anything is saved, just return the parameter object
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
            return invocation.getArgument(0);
        };
        lenient().when(businessRepository.save(any(Business.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(timeSlotRepository.save(any(TimeSlot.class))).thenAnswer(returnParameterAsAnswer);
    }

    @Test
    public void testCreateBusiness() {

        assertEquals(0, businessService.getAllBusinesses().size());

        BusinessDto business = null;
        String businessName = "Best Business in the world";
        try {
            business = businessService.createBusiness(BUSINESS_ID, businessName, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, BUSINESS_EMAIL, BUSINESS_NUMBER_OF_REPAIR_SPOTS);

        } catch (Exception e) {
            fail();
        }
        assertEquals(BUSINESS_ID, business.getBusinessID());
        assertEquals(businessName, business.getName());
        assertEquals(BUSINESS_ADDRESS, business.getAddress());
        assertEquals(BUSINESS_PHONE_NUMBER, business.getPhoneNumber());
        assertEquals(BUSINESS_EMAIL, business.getEmail());
        assertEquals(BUSINESS_NUMBER_OF_REPAIR_SPOTS, business.getNumberOfRepairSpots());
        assertNotNull(business);

    }


    @Test
    public void testMissingNameCreateBusiness() {
        String name = "";
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.createBusiness(BUSINESS_ID,name, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, BUSINESS_EMAIL, BUSINESS_NUMBER_OF_REPAIR_SPOTS);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);
        // check error
        assertEquals("Business name cannot be empty!", error);
    }

    @Test
    public void testMissingAddressCreateBusiness() {
        String address = "";
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.createBusiness(BUSINESS_ID, BUSINESS_NAME, address, BUSINESS_PHONE_NUMBER, BUSINESS_EMAIL, BUSINESS_NUMBER_OF_REPAIR_SPOTS);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);
        // check error
        assertEquals("Address cannot be empty!", error);
    }

    @Test
    public void testMissingPhoneNumberCreateBusiness() {
        String phoneNumber = "";
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.createBusiness(BUSINESS_ID, BUSINESS_NAME, BUSINESS_ADDRESS, phoneNumber, BUSINESS_EMAIL, BUSINESS_NUMBER_OF_REPAIR_SPOTS);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);
        // check error
        assertEquals("Phone number cannot be empty!", error);
    }

    @Test
    public void testMissingEmailCreateBusiness() {
        String email = "";
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.createBusiness(BUSINESS_ID, BUSINESS_NAME, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, email, BUSINESS_NUMBER_OF_REPAIR_SPOTS);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);
        // check error
        assertEquals("Email cannot be empty!", error);
    }

    @Test

    public void testInvalidEmailNoAtSignCreateBusiness() {
        String email = "bestBusinessgmail.com";
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.createBusiness(BUSINESS_ID, BUSINESS_NAME, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, email, BUSINESS_NUMBER_OF_REPAIR_SPOTS);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);
        // check error
        assertEquals("Invalid email", error);
    }

    @Test
    public void testInvalidEmailCreateBusiness() {
        String email = "Not an email";
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.createBusiness(BUSINESS_ID, BUSINESS_NAME, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, email, BUSINESS_NUMBER_OF_REPAIR_SPOTS);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);
        // check error
        assertEquals("Invalid email", error);
    }

    @Test
    public void testInvalidEmailNoDotCreateBusiness() {
        String email = "bestBusiness@gmailcom";
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.createBusiness(BUSINESS_ID, BUSINESS_NAME, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, email, BUSINESS_NUMBER_OF_REPAIR_SPOTS);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);
        // check error
        assertEquals("Invalid email", error);
    }
}
