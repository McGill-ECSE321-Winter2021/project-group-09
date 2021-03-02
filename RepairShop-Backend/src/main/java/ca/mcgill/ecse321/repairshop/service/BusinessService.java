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

import static java.util.regex.Pattern.matches;

@Service
public class BusinessService {

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    TimeSlotRepository timeSlotRepository;

/*  //TODO: Remove this if not needed
    @Transactional
    public Business createBusiness(String name){
        Business business = new Business();
        business.setName(name);
        businessRepository.save(business);  //save business in backend
        return business;
    }
*/

    /**
     * Create a business with a name, an address, a phone number, an email and number of repair spot.
     *
     * @param name                String
     * @param address             String
     * @param phoneNumber         String
     * @param email               String
     * @param numberOfRepairSpots int
     * @return business Business (the business created by this method)
     */
    @Transactional
    public Business createBusiness(String name, String address, String phoneNumber, String email, int numberOfRepairSpots) {
        String error = "";

        //int intNumberOfRepairSpots = Integer.parseInt(numberOfRepairSpots); //convert String -> int

        //Check inputs
        if (name.equals("")) {
            error = error + "Enter Business name";
        }
        if (address.equals("")) {
            error = error + "Enter address";
        }
        if (email.equals("")) {
            error = error + "Enter email";
        }
        error = validateEmail(email, error);

        if (phoneNumber.equals("")) {
            error = error + "Enter phone number";
        }
        if (numberOfRepairSpots < 0) {
            error = error + "The number of repair spots cannot be negative";
        }
        error = error.trim();

        //Throw exception with error message
        if (error.length() > 0) {
            throw new IllegalArgumentException(error);
        }

        //Create Business
        Business business = new Business();
        business.setName(name);
        business.setEmail(email);
        business.setPhoneNumber(phoneNumber);
        business.setNumberOfRepairSpots(numberOfRepairSpots);

        //Save Business in backend
        businessRepository.save(business);

        return business;

    }

    /**
     * Validate email using regex pattern
     *
     * @param email The email to validate.
     * @throws IllegalArgumentException Throws exception when email is invalid.
     * @author Tyler
     */
    private static String validateEmail(String email, String error) {
        String newError = "";
        if (!matches("[A-Za-z0-9._+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}", email))
            newError = error + "Invalid email";
        return newError;
    }


    /**
     * Finds the business with the input name
     *
     * @param name Business name
     * @return business Business
     */
    @Transactional
    public Business getBusiness(String name) {
        //Check input
        if (name == null) {
            throw new IllegalArgumentException("Enter a business name.");
        }
        return businessRepository.findBusinessByName(name);
    }

    //TODO: Instead of one updateBusiness(), would it be best to have updateName, updateAddress, updatePhoneNumber, updateEmail, updateNumberOfRepairSpots?


    @Transactional
    public Business updateBusiness(String name, String address, String phoneNumber, String email, int numberOfRepairSpots) {
        String error = "";
        //int intNumberOfRepairSpots = Integer.parseInt(numberOfRepairSpots);
        if (numberOfRepairSpots < 0) {
            error = error + "The number of repair spots cannot be negative";
        }
        validateEmail(email, error);
        error = error.trim();

        Business business = businessRepository.findBusinessByName(name);

        business.setName(name);
        business.setAddress(address);
        business.setEmail(email);
        business.setPhoneNumber(phoneNumber);
        business.setNumberOfRepairSpots(numberOfRepairSpots);

        //TODO: Do we need to save it again?
        //Save Business in backend
        businessRepository.save(business);

        return business;


    }


    //TODO: Delete this method if not needed?

/**
     * Gets all businesses from businessRepository
     *
     * @return List of businesses : List<Business>
     */
    @Transactional
    public List<Business> getAllBusinesses() {
        return toList(businessRepository.findAll());
    }


    public TimeSlot addVacation(String startDateTimeStr, String endDateTimeStr) {

        //Create TimeSlot vacation
        TimeSlot vacation = new TimeSlot();

        //Convert String -> Timestamp
        Timestamp startDateTime = Timestamp.valueOf(startDateTimeStr);
        Timestamp endDateTime = Timestamp.valueOf(endDateTimeStr);
        vacation.setStartDateTime(startDateTime);
        vacation.setEndDateTime(endDateTime);

        //TODO: Complete this

        timeSlotRepository.save(vacation);
        return vacation;
    }

    /**
     * Gets all vacations from TimeSlotRepository
     *
     * @return List of vacations : List<TimeSlot>
     */
    @Transactional
    public List<TimeSlot> getVacations() {
        return toList(timeSlotRepository.findAll());
    }

    //TODO: deleteVacations()



    //TODO: Delete this method if not needed?
    private <T> List<T> toList(Iterable<T> iterable) {
        List<T> resultList = new ArrayList<T>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }

}
