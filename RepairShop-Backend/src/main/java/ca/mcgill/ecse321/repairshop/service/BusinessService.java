package ca.mcgill.ecse321.repairshop.service;

import ca.mcgill.ecse321.repairshop.dto.BusinessDto;
import ca.mcgill.ecse321.repairshop.dto.TimeSlotDto;
import ca.mcgill.ecse321.repairshop.model.Business;
import ca.mcgill.ecse321.repairshop.model.Technician;
import ca.mcgill.ecse321.repairshop.model.TimeSlot;
import ca.mcgill.ecse321.repairshop.repository.BusinessRepository;
import ca.mcgill.ecse321.repairshop.repository.TechnicianRepository;
import ca.mcgill.ecse321.repairshop.repository.TimeSlotRepository;
import ca.mcgill.ecse321.repairshop.service.utilities.SystemTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import static ca.mcgill.ecse321.repairshop.service.utilities.ValidationHelperMethods.validateEmail;

@Service
public class BusinessService {

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    TimeSlotRepository timeSlotRepository;

    @Autowired
    TechnicianRepository technicianRepository;

    /**
     * Creates a business with a name, an address, a phone number, an email and number of repair spot.
     *
     * @param name                name of the business (String)
     * @param address             address of the business (String)
     * @param phoneNumber         phone number of the business (String)
     * @param email               email of the business (String)
     * @return businessDto BusinessDto (the business created by this method)
     * @throws Exception If at least one of the inputs is invalid
     */
    @Transactional
    public BusinessDto createBusiness(String name, String address, String phoneNumber, String email) throws Exception {
        
        inputValidation(name, address, phoneNumber, email);

        Business business = businessRepository.findAll().get(0); // should always exist
        business.setName(name);
        business.setAddress(address);
        business.setEmail(email);
        business.setPhoneNumber(phoneNumber);
        business.setNumberOfRepairSpots(0);

        List<TimeSlot> holidays = new ArrayList<>();
        business.setHolidays(holidays);

        businessRepository.save(business);

        return businessToDto(business);
    }

    /**
     * Finds the business and returns the businessDto
     *
     * @return businessDto object for the corresponding business (BusinessDto)
     * @throws Exception If a business was not found
     */
    @Transactional
    public BusinessDto getBusiness() throws Exception {

        List<Business> businesses = businessRepository.findAll();
        if (businesses.size() == 0) throw new Exception("Business not found");

        return businessToDto(businesses.get(0));
    }

    /**
     * Updates business information (address, phone number, email, number of repair spots).
     *
     * @param name                name of the business (String)
     * @param address             address of the business (String)
     * @param phoneNumber         phone number of the business (String)
     * @param email               email of the business (String)
     * @param numberOfRepairSpots number of repair spots of the business (int)
     * @return businessDto object for the corresponding business (BusinessDto)
     * @throws Exception if there are invalid inputs or the business can't be found
     */
    @Transactional
    public BusinessDto updateBusiness(String name, String address, String phoneNumber, String email, int numberOfRepairSpots) throws Exception {

        inputValidation(name, address, phoneNumber, email);

        List<Business> businesses = businessRepository.findAll();
        if (businesses.size() == 0) throw new Exception("Business not found");

        Business business = businesses.get(0);

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
     *
     * @param numberOfRepairSpots number of repair spots of the business (int)
     * @return businessDto object for the corresponding business (BusinessDto)
     * @throws Exception if there are invalid inputs or the business can't be found
     */
    @Transactional
    public BusinessDto updateNbRepairSpots(int numberOfRepairSpots) throws Exception {

        List<Business> businesses = businessRepository.findAll();
        if (businesses.size() == 0) throw new Exception("Business not found");

        Business business = businesses.get(0);

        if (numberOfRepairSpots < 0) {
            throw new Exception("The number of repair spots cannot be negative");
        }

        business.setNumberOfRepairSpots(numberOfRepairSpots);
        businessRepository.save(business);
        return businessToDto(business);
    }

  
    /**
     * Adds a new TimeSlot holiday to the business.
     *
     * @param startDateTime Start date and time of the new TimeSlot Holiday (TimeStamp)
     * @param endDateTime   End date and time of the new TimeSlot Holiday (TimeStamp)
     * @return businessDto BusinessDto
     * @throws Exception if the business wasn't found
     */
    @Transactional
    public BusinessDto addHoliday(Timestamp startDateTime, Timestamp endDateTime) throws Exception {

        Business business = businessRepository.findAll().get(0);
        if (business == null) throw new Exception("Business not found");

        TimeSlot newHoliday = new TimeSlot();
        newHoliday.setStartDateTime(startDateTime);
        newHoliday.setEndDateTime(endDateTime);

        List<TimeSlot> holidayList = business.getHolidays();
        holidayList.add(newHoliday);
        business.setHolidays(holidayList);

        businessRepository.save(business);
        return businessToDto(business);
    }
    
    /**
     * Removes a new TimeSlot holiday from the business.
     *
     * @param startDateTime Start date and time of the new TimeSlot Holiday (TimeStamp)
     * @param endDateTime   End date and time of the new TimeSlot Holiday (TimeStamp)
     * @throws Exception if the business wasn't found
     */
    @Transactional
    public BusinessDto deleteHoliday(Timestamp startDateTime, Timestamp endDateTime) throws Exception {

        Business business = businessRepository.findAll().get(0);

        for (TimeSlotDto holidayToDelete: getAllHolidays()) {
        	if (holidayToDelete.getStartDateTime() == startDateTime && holidayToDelete.getEndDateTime() == endDateTime) { 
        		timeSlotRepository.deleteById(holidayToDelete.getID());
        		break;
        	}
        }
        
        businessRepository.save(business);
        return businessToDto(business);  // returning List<TimeSlotDto> holidays that are still remaining
    }

    /**
     * Gets all Holidays of the business.
     *
     * @return List of Holidays (List<TimeSlotDto>)
     * @throws Exception If the business was not found
     */
    @Transactional
    public List<TimeSlotDto> getAllHolidays() throws Exception {

        List<Business> businesses = businessRepository.findAll();
        if (businesses.size() == 0) throw new Exception("Business not found");

        Business business = businesses.get(0);

        List<TimeSlot> holidays = business.getHolidays();
        List<TimeSlotDto> holidaysDtoList = new ArrayList<>();

        for (TimeSlot currHoliday : holidays) {
            holidaysDtoList.add(TimeSlotService.timeslotToDTO(currHoliday));
        }

        return holidaysDtoList;
    }

    /** Method to get the number of available repair spots at the current time
     * @return number of available repair spots
     * @throws Exception if the business was not found
     */
    @Transactional
    public int getAvailableRepairSpots() throws Exception {

        List<Business> businesses = businessRepository.findAll();
        if (businesses.size() == 0) throw new Exception("Business not found");

        // Simulate booking an appointment to get availability

        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setStartDateTime(SystemTime.getCurrentDateTime());
        timeSlot.setEndDateTime(SystemTime.getCurrentDateTime());

        List<Technician> technicians = technicianRepository.findAll();

        int availableSpots = 0;

        for (Technician technician : technicians) {
            if (AppointmentService.isBookable(timeSlot, technician, businesses.get(0))) availableSpots += 1;
        }

        return availableSpots;

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
       for(TimeSlot currHoliday : business.getHolidays()){
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
     * @throws Exception if one of the inputs is invalid
     */
    private void inputValidation(String name, String address, String phoneNumber, String email) throws Exception {

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
    }

}