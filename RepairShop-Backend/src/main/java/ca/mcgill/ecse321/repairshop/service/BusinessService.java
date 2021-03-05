package ca.mcgill.ecse321.repairshop.service;

import ca.mcgill.ecse321.repairshop.dto.BusinessDto;
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
     * @param name                name of the business (String)
     * @param address             address of the business (String)
     * @param phoneNumber         phone number of the business (String)
     * @param email               email of the business (String)
     * @param numberOfRepairSpots number of repair spots of the business (int)
     * @return businessDto BusinessDto (the business created by this method)
     * @throws Exception If at least one of the inputs is invalid
     */
    @Transactional
    public BusinessDto createBusiness(String name, String address, String phoneNumber, String email, int numberOfRepairSpots) throws Exception {

        //1) Check inputs
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

        //2) Check if there's an existing business with the same name
        if (businessRepository.findBusinessByName(name) != null) {
            throw new Exception("The business name is already taken.");
        }

        //3) Create Business
        Business business = new Business();
        business.setName(name);
        business.setAddress(address);
        business.setEmail(email);
        business.setPhoneNumber(phoneNumber);
        business.setNumberOfRepairSpots(numberOfRepairSpots);

        //Create empty list of TimeSlot vacations
        List<TimeSlot> vacations = new ArrayList<>();
        business.setVacations(vacations);

        //4) Save Business in backend
        businessRepository.save(business);

        //5) Convert business to BusinessDto and return it
        return businessToDto(business);
    }

    /**
     * Validate email using regex pattern
     *
     * @param email The email to validate.
     * @throws Exception Throws exception when email is invalid.
     * @author Tyler
     */
    private static void validateEmail(String email) throws Exception {
        if (!matches("[A-Za-z0-9._+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}", email)) throw new Exception("Invalid email");
    }


    /**
     * Finds a business by name and returns a businessDTO
     *
     * @param name name of the business to find (String)
     * @return businessDto object for the corresponding business (BusinessDto)
     * @throws Exception If a business with the name was not found
     */
    @Transactional
    public BusinessDto getBusinessByName(String name) throws Exception {

        //Check input
        if (name == null || name.equals("")) {
            throw new Exception("Could not find a business with name: " + name);
        }
        //Find business with the corresponding name in businessRepository
        Business businessFound = businessRepository.findBusinessByName(name);

        return businessToDto(businessFound); //Convert Business -> BusinessDto
    }

    //TODO: Instead of one updateBusiness(), would it be best to have updateName, updateAddress, updatePhoneNumber, updateEmail, updateNumberOfRepairSpots?

    /**
     * Updates business information (address, phone number, email, number of repair spots).
     * @param name                name of the business (String
     * @param address             address of the business (String)
     * @param phoneNumber         phone number of the business (String)
     * @param email               email of the business (String)
     * @param numberOfRepairSpots number of repair spots of the business (int)
     * @return businessDto object for the corresponding business (BusinessDto)
     * @throws Exception if there are invalid inputs or the business can't be found
     */
    @Transactional
    public BusinessDto updateBusiness(String name, String address, String phoneNumber, String email, int numberOfRepairSpots) throws Exception {

        //1) Check inputs
        if (name == null || name.equals("")) {
            throw new Exception("Enter business name");
        }
        if (address == null || address.equals("")) {
            throw new Exception("Enter address");
        }
        if (phoneNumber == null || phoneNumber.equals("")) {
            throw new Exception("Enter phone number");
        }
        if (email == null || email.equals("")) {
            throw new Exception("Enter email");
        }

        validateEmail(email); // if email is invalid, an exception will be thrown

        if (numberOfRepairSpots < 0) {
            throw new Exception("The number of repair spots cannot be negative");
        }

        //2) Find business byt its name
        Business business = businessRepository.findBusinessByName(name);

        //3) Check if the business exists
        if (business == null) {
            throw new Exception("Business with name \""+name+"\" not found");
        }

        //4) Update the business information
        business.setName(name);
        business.setAddress(address);
        business.setEmail(email);
        business.setPhoneNumber(phoneNumber);
        business.setNumberOfRepairSpots(numberOfRepairSpots);

        //5) Save Business in backend
        businessRepository.save(business);

        return businessToDto(business);// Convert and return business
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
     * Adds a new TimeSlot vacation to the business.
     * @param name name of the business (String)
     * @param startDateTime Start date and time of the new TimeSlot vacation (TimeStamp)
     * @param endDateTime End date and time of the new TimeSlot vacation (TimeStamp)
     * @return businessDto BusinessDto
     * @throws Exception if the business wasn't found
     */
    @Transactional
    public BusinessDto addVacation(String name, Timestamp startDateTime, Timestamp endDateTime) throws Exception {
        // Find business byt its name
        Business business = businessRepository.findBusinessByName(name);

        //Check if the business exists
        if (business == null) {
            throw new Exception("Business with name \""+name+"\" not found");
        }
        //Create TimeSlot vacation
        TimeSlot newVacation = new TimeSlot();
        newVacation.setStartDateTime(startDateTime);
        newVacation.setEndDateTime(endDateTime);

        //Adds the new TimeSlot vacation to the list of vacations
        business.getVacations().add(newVacation);

        //Save in BusinessRepository
        businessRepository.save(business);
        timeSlotRepository.save(newVacation);  //TODO: Do we need to save the new vacation to the timeslot repository?
        return businessToDto(business);  //TODO: Should we return BusinessDto or TimeSlotDto or List<TimeSlotDto>?
    }

    /**
     * Gets all vacations.
     * @return List of vacations (List<TimeSlot>)
     */
    @Transactional
    public List<TimeSlot> getAllVacations() {
        return timeSlotRepository.findAll();
        //TODO: not sure if this is the right way to get all the vacations.
        // We only have one business, so I think we can just get all the vacations stored into timeSlotRepository
    }

    private <T> List<T> toList(Iterable<T> iterable) {
        List<T> resultList = new ArrayList<T>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }

    /**
     * Helper method to convert Business to BusinessDto
     *
     * @param business business to be converted to businessDto (Business)
     * @return businessDto (BusinessDTo)
     */
    public BusinessDto businessToDto(Business business){

        //Create businessDto
        BusinessDto businessDto = new BusinessDto(business.getName(), business.getAddress(), business.getEmail(), business.getPhoneNumber(), business.getNumberOfRepairSpots());

        return businessDto;
    }

}
