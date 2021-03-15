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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;

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

    //Test data
    private static final String BUSINESS_ADDRESS = "123 Business Street, Montreal";
    private static final String BUSINESS_EMAIL = "bestBusiness@gmail.com";
    private static final String BUSINESS_PHONE_NUMBER = "(123)-456-7890";
    private static final int BUSINESS_NUMBER_OF_REPAIR_SPOTS = 2;
    private static final Timestamp START_TIME = Timestamp.valueOf("2021-12-02 10:00:00");
    private static final Timestamp END_TIME = Timestamp.valueOf("2021-12-30 11:00:00");

    @BeforeEach
    public void setMockOutput() {
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

        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
            return invocation.getArgument(0);
        };
        lenient().when(businessRepository.save(any(Business.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(timeSlotRepository.save(any(TimeSlot.class))).thenAnswer(returnParameterAsAnswer);
    }

    @Test
    public void testCreateBusiness() {

        BusinessDto business = null;
        String businessName = "Best Business in the world";
        try {
            business = businessService.createBusiness(businessName, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, BUSINESS_EMAIL, BUSINESS_NUMBER_OF_REPAIR_SPOTS);

        } catch (Exception e) {
            fail();
        }
   
        assertEquals(businessName, business.getName());
        assertEquals(BUSINESS_ADDRESS, business.getAddress());
        assertEquals(BUSINESS_PHONE_NUMBER, business.getPhoneNumber());
        assertEquals(BUSINESS_EMAIL, business.getEmail());
        assertEquals(BUSINESS_NUMBER_OF_REPAIR_SPOTS, business.getNumberOfRepairSpots());

        assertNotNull(business);

    }

    @Test
    public void testNegativeNbRepairSpotsCreateBusiness() {
        int negativeNb = -10;
        BusinessDto business = null;
        String error = null;
        try {
            business = businessService.createBusiness(BUSINESS_NAME, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, BUSINESS_EMAIL, negativeNb);
        } catch (Exception e) {
            error = e.getMessage();
        }

        assertNull(business);
        assertEquals("The number of repair spots cannot be negative", error);
    }

    @Test
    public void testMissingNameCreateBusiness() {

        String name = "";
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.createBusiness(name, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, BUSINESS_EMAIL, BUSINESS_NUMBER_OF_REPAIR_SPOTS);
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
            business = businessService.createBusiness(BUSINESS_NAME, address, BUSINESS_PHONE_NUMBER, BUSINESS_EMAIL, BUSINESS_NUMBER_OF_REPAIR_SPOTS);
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
            business = businessService.createBusiness(BUSINESS_NAME, BUSINESS_ADDRESS, phoneNumber, BUSINESS_EMAIL, BUSINESS_NUMBER_OF_REPAIR_SPOTS);
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
            business = businessService.createBusiness(BUSINESS_NAME, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, email, BUSINESS_NUMBER_OF_REPAIR_SPOTS);
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
            business = businessService.createBusiness(BUSINESS_NAME, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, email, BUSINESS_NUMBER_OF_REPAIR_SPOTS);
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
            business = businessService.createBusiness(BUSINESS_NAME, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, email, BUSINESS_NUMBER_OF_REPAIR_SPOTS);
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
            business = businessService.createBusiness(BUSINESS_NAME, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, email, BUSINESS_NUMBER_OF_REPAIR_SPOTS);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);
        assertEquals("Invalid email", error);
    }

    
    /*
    @Test
    public void testNullIDCreateBusiness() {
        Long nullBusinessID = null;
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.createBusiness(nullBusinessID, BUSINESS_NAME, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, BUSINESS_EMAIL, BUSINESS_NUMBER_OF_REPAIR_SPOTS);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);
        assertEquals("Business ID cannot be empty!", error);
    }
	*/

    @Test
    public void testNullNameCreateBusiness() {
        String nullBusinessName = null;
        String error = null;
        BusinessDto business = null;
        try {
            business = businessService.createBusiness(nullBusinessName, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, BUSINESS_EMAIL, BUSINESS_NUMBER_OF_REPAIR_SPOTS);
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
            business = businessService.createBusiness(BUSINESS_NAME, nullAddress, BUSINESS_PHONE_NUMBER, BUSINESS_EMAIL, BUSINESS_NUMBER_OF_REPAIR_SPOTS);
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
            business = businessService.createBusiness(BUSINESS_NAME, BUSINESS_ADDRESS, nullPhoneNumber, BUSINESS_EMAIL, BUSINESS_NUMBER_OF_REPAIR_SPOTS);
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
            business = businessService.createBusiness(BUSINESS_NAME, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, nullEmail, BUSINESS_NUMBER_OF_REPAIR_SPOTS);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(business);
        assertEquals("Email cannot be empty!", error);
    }

    @Test
    public void testGetBusiness() {
        String error = null;
        BusinessDto businessDto;
        try {
            businessDto = businessService.getBusiness();
            assertEquals(BUSINESS_ID, businessDto.getBusinessID());
            assertEquals(BUSINESS_NAME, businessDto.getName());
            assertEquals(BUSINESS_ADDRESS, businessDto.getAddress());
            assertEquals(BUSINESS_PHONE_NUMBER, businessDto.getPhoneNumber());
            assertEquals(BUSINESS_EMAIL, businessDto.getEmail());
            assertEquals(BUSINESS_NUMBER_OF_REPAIR_SPOTS, businessDto.getNumberOfRepairSpots());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /*
    @Test
    public void testMissingGetBusinessByID() {
        String error = null;
        Long nullBusinessID = null;
        BusinessDto businessDto;
        try {
            businessDto = businessService.getBusinessByID(nullBusinessID);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertEquals("Enter BusinessID", error);
    }
    
    

    @Test
    public void testNotFoundGetBusinessByID() {
        String error = null;
        long businessID = 4567;
        BusinessDto businessDto;
        try {
            businessDto = businessService.getBusinessByID(businessID);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertEquals("BusinessID not found", error);
    }
	*/
    @Test
    public void testUpdateBusiness() {


        BusinessDto business = null;
        String newName = "Update name";
        String newAddress = "Update address";
        String newPhoneNumber = "Update phone number";
        String newEmail = "niceEmail@mcgill.ca";
        int newNbRepairSpots = 100;


        try {
            business = businessService.updateBusiness(newName, newAddress, newPhoneNumber, newEmail, newNbRepairSpots);
            assertEquals(BUSINESS_ID, business.getBusinessID());
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
    public void testNegativeNbRepairSpotsUpdateBusiness() {
        int negativeNb = -10;
        BusinessDto business = null;
        String error = null;
        try {
            business = businessService.updateBusiness(BUSINESS_NAME, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, BUSINESS_EMAIL, negativeNb);
        } catch (Exception e) {
            error = e.getMessage();
        }

        assertNull(business);
        assertEquals("The number of repair spots cannot be negative", error);
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

    /*
    @Test
    public void testGetAllBusinesses() {

        BusinessDto business = null;

        try {
            business = businessService.createBusiness(BUSINESS_NAME, BUSINESS_ADDRESS, BUSINESS_PHONE_NUMBER, BUSINESS_EMAIL, BUSINESS_NUMBER_OF_REPAIR_SPOTS);

            List<BusinessDto> businessDtoList = businessService.getAllBusinesses();

            assertEquals(business.getName(), businessDtoList.get(0).getName());
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }
	*/

    @Test
    public void testAddHoliday() {
        BusinessDto businessDto = null;
        Timestamp startTime = Timestamp.valueOf("2021-04-02 10:00:00");
        Timestamp endTime = Timestamp.valueOf("2021-05-14 09:00:00");
        TimeSlot newHoliday = new TimeSlot();
        newHoliday.setStartDateTime(startTime);
        newHoliday.setEndDateTime(endTime);

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
        BusinessDto businessDto = null;
        Timestamp startTime = Timestamp.valueOf("2020-12-23 21:00:00");
        Timestamp endTime = Timestamp.valueOf("2020-12-26 07:00:00");
        TimeSlot newHoliday = new TimeSlot();
        newHoliday.setStartDateTime(startTime);
        newHoliday.setEndDateTime(endTime);

        try {
        	
            businessDto = businessService.addHoliday(BUSINESS_ID, startTime, endTime);
            System.out.println("Holidays before deletion: " + businessDto.getHolidays().toString()); // 2 holidays
            
            businessDto = businessService.deleteHoliday(BUSINESS_ID, startTime, endTime); 
            System.out.println("Holidays after deletion: " + businessDto.getHolidays().toString()); // 1 holiday
            
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertEquals(1, businessDto.getHolidays().size());
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

}