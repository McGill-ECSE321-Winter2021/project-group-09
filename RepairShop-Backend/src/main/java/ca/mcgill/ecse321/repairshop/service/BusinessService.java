package ca.mcgill.ecse321.repairshop.service;

import ca.mcgill.ecse321.repairshop.dto.BusinessDto;
import ca.mcgill.ecse321.repairshop.dto.TimeSlotDto;
import ca.mcgill.ecse321.repairshop.model.Business;
import ca.mcgill.ecse321.repairshop.model.TimeSlot;
import ca.mcgill.ecse321.repairshop.repository.BusinessRepository;
import ca.mcgill.ecse321.repairshop.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ca.mcgill.ecse321.repairshop.service.utilities.ValidationHelperMethods.validateEmail;
import static java.util.regex.Pattern.matches;

@Service
public class BusinessService {

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    TimeSlotRepository timeSlotRepository;

    /**
     * Creates a business with a name, an address, a phone number, an email and number of repair spot.
     *
     * @param businessID          ID of the business (Long)
     * @param name                name of the business (String)
     * @param address             address of the business (String)
     * @param phoneNumber         phone number of the business (String)
     * @param email               email of the business (String)
     * @param numberOfRepairSpots number of repair spots of the business (int)
     * @return businessDto BusinessDto (the business created by this method)
     * @throws Exception If at least one of the inputs is invalid
     */
    @Transactional
    public BusinessDto createBusiness(Long businessID, String name, String address, String phoneNumber, String email, int numberOfRepairSpots) throws Exception {
        if (businessID == null) {
            throw new Exception("Business ID cannot be empty!");
        }
        inputValidation(name, address, phoneNumber, email, numberOfRepairSpots);

        Business business = new Business();
        business.setBusinessID(businessID);
        business.setName(name);
        business.setAddress(address);
        business.setEmail(email);
        business.setPhoneNumber(phoneNumber);
        business.setNumberOfRepairSpots(numberOfRepairSpots);

        List<TimeSlot> holidays = new ArrayList<>();
        business.setHolidays(holidays);

        businessRepository.save(business);

        return businessToDto(business);
    }

    /**
     * Finds a business by businessID and returns a businessDTO
     *
     * @param businessID ID of the business to find (Long)
     * @return businessDto object for the corresponding business (BusinessDto)
     * @throws Exception If a business with the businessID was not found
     */
    @Transactional
    public BusinessDto getBusinessByID(Long businessID) throws Exception {

        if (businessID == null) {
            throw new Exception("Enter BusinessID");
        }

        Business businessFound = businessRepository.findBusinessByBusinessID(businessID);
        if (businessFound == null) throw new Exception("BusinessID not found");

        return businessToDto(businessFound);
    }

    /**
     * Updates business information (address, phone number, email, number of repair spots).
     * A businessID must be provided.
     *
     * @param businessID          ID of the business to update (Long)
     * @param name                name of the business (String)
     * @param address             address of the business (String)
     * @param phoneNumber         phone number of the business (String)
     * @param email               email of the business (String)
     * @param numberOfRepairSpots number of repair spots of the business (int)
     * @return businessDto object for the corresponding business (BusinessDto)
     * @throws Exception if there are invalid inputs or the business can't be found
     */
    @Transactional
    public BusinessDto updateBusiness(Long businessID, String name, String address, String phoneNumber, String email, int numberOfRepairSpots) throws Exception {

        if (businessID == null) {
            throw new Exception("Business ID cannot be empty!");
        }

        inputValidation(name, address, phoneNumber, email, numberOfRepairSpots);

        Business business = businessRepository.findBusinessByBusinessID(businessID);

        if (business == null) {
            throw new Exception("Could not find a business with ID: " + businessID);
        }

        business.setName(name);
        business.setAddress(address);
        business.setEmail(email);
        business.setPhoneNumber(phoneNumber);
        business.setNumberOfRepairSpots(numberOfRepairSpots);

        businessRepository.save(business);
        return businessToDto(business);
    }

    /**
     * Updates the number of repair spots.
     * A businessID must be provided.
     *
     * @param businessID          ID of the business to update (Long)
     * @param numberOfRepairSpots number of repair spots of the business (int)
     * @return businessDto object for the corresponding business (BusinessDto)
     * @throws Exception if there are invalid inputs or the business can't be found
     */
    @Transactional
    public BusinessDto updateNbRepairSpots(Long businessID, int numberOfRepairSpots) throws Exception {

        if (businessID == null) {
            throw new Exception("Business ID cannot be empty!");
        }

        Business business = businessRepository.findBusinessByBusinessID(businessID);

        if (business == null) {
            throw new Exception("Could not find a business with ID: " + businessID);
        }

        if (numberOfRepairSpots < 0) {
            throw new Exception("The number of repair spots cannot be negative");
        }

        business.setNumberOfRepairSpots(numberOfRepairSpots);
        businessRepository.save(business);
        return businessToDto(business);
    }

    /**
     * Gets all businesses from businessRepository
     *
     * @return List of businessesDto objects : List<BusinessDto>
     */
    @Transactional
    public List<BusinessDto> getAllBusinesses() {
        return businessRepository.findAll().stream().map(this::businessToDto).collect(Collectors.toList());
    }

    /**
     * Adds a new TimeSlot holiday to the business.
     *
     * @param businessID    ID of the business (Long)
     * @param startDateTime Start date and time of the new TimeSlot Holiday (TimeStamp)
     * @param endDateTime   End date and time of the new TimeSlot Holiday (TimeStamp)
     * @return businessDto BusinessDto
     * @throws Exception if the business wasn't found
     */
    @Transactional
    public BusinessDto addHoliday(Long businessID, Timestamp startDateTime, Timestamp endDateTime) throws Exception {

        Business business = businessRepository.findBusinessByBusinessID(businessID);

        if (business == null) {
            throw new Exception("Could not find a business with ID: " + businessID);
        }


        TimeSlot newHoliday = new TimeSlot();
        newHoliday.setStartDateTime(startDateTime);
        newHoliday.setEndDateTime(endDateTime);

        business.getHolidays().add(newHoliday);

        businessRepository.save(business);
        timeSlotRepository.save(newHoliday);
        return businessToDto(business);  //TODO: Should we return BusinessDto or TimeSlotDto or List<TimeSlotDto>?
    }

    /**
     * Gets all Holidays of the business.
     *
     * @return List of Holidays (List<TimeSlotDto>)
     * @throws Exception If the business with the input businessID can't be found
     */
    @Transactional
    public List<TimeSlotDto> getAllHolidays(Long businessID) throws Exception {

        if (businessID == null) {
            throw new Exception("BusinessID cannot be empty");
        }
        Business business = businessRepository.findBusinessByBusinessID(businessID);
        if (business == null) {
            throw new Exception("Could not find a business with ID: " + businessID);
        }
        List<TimeSlot> holidays = business.getHolidays();
        List<TimeSlotDto> holidaysDtoList = new ArrayList<>();

        for (TimeSlot currHoliday : holidays) {
            holidaysDtoList.add(TimeSlotService.timeslotToDTO(currHoliday));
        }

        return holidaysDtoList;
    }

    /**
     * Helper method to convert Business to BusinessDto
     *
     * @param business business to be converted to businessDto (Business)
     * @return businessDto (BusinessDTo)
     */
    public BusinessDto businessToDto(Business business) {

        //Create businessDto
        BusinessDto businessDto = new BusinessDto(business.getBusinessID(), business.getName(), business.getAddress(), business.getEmail(), business.getPhoneNumber(), business.getNumberOfRepairSpots());

       List<TimeSlotDto> holidayDtoList = new ArrayList<>();
       for(TimeSlot currHoliday:business.getHolidays()){
           holidayDtoList.add(TimeSlotService.timeslotToDTO(currHoliday));
       }
        businessDto.setHolidays(holidayDtoList);
        return businessDto;
    }

    /**
     * Checks if the inputs are valid.
     *
     * @param name                name of the business (String)
     * @param address             address of the business (String)
     * @param phoneNumber         phone number of the business (String)
     * @param email               email of the business (String)
     * @param numberOfRepairSpots number of repair spots of the business (int)
     * @throws Exception if one of the inputs is invalid
     */
    private void inputValidation(String name, String address, String phoneNumber, String email, int numberOfRepairSpots) throws Exception {

        if (name == null || name.equals("")) {
            throw new Exception("Business name cannot be empty!");
        }
        if (address == null || address.equals("")) {
            throw new Exception("Address cannot be empty!");
        }
        if (phoneNumber == null || phoneNumber.equals("")) {
            throw new Exception("Phone number cannot be empty!");
        }
        if (email == null || email.equals("")) {
            throw new Exception("Email cannot be empty!");
        }

        validateEmail(email); // if email is invalid, an exception will be thrown

        if (numberOfRepairSpots < 0) {
            throw new Exception("The number of repair spots cannot be negative");
        }
    }

}
