package ca.mcgill.ecse321.repairshop.controller;

import ca.mcgill.ecse321.repairshop.dto.BusinessDto;
import ca.mcgill.ecse321.repairshop.dto.TimeSlotDto;
import ca.mcgill.ecse321.repairshop.model.Business;
import ca.mcgill.ecse321.repairshop.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/business")
public class BusinessRestController {

    @Autowired
    BusinessService businessService;


    /**
     * Get request for all businesses
     *
     * @return a list of all businesses
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllBusinesses() {
        try {
            List<BusinessDto> businessDtosList = businessService.getAllBusinesses();
            return new ResponseEntity<>(businessDtosList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get a business by businessID
     *
     * @param businessID ID of the business
     * @return the requested business if found
     */
    @GetMapping("/{businessID}")
    public ResponseEntity<?> getBusinessById(@PathVariable("businessID") Long businessID) {
        try {
            BusinessDto businessDto = businessService.getBusinessByID(businessID);
            return new ResponseEntity<>(businessDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Create a new business
     *
     * @param name                name of the business (String)
     * @param address             address of the business (String)
     * @param phoneNumber         phone number of the business (String)
     * @param email               email of the business (String)
     * @param numberOfRepairSpots number of repair spots of the business (int)
     * @return the new business if created successfully
     */
    @PostMapping("/create/{name}")
    public ResponseEntity<?> createBusiness(@PathVariable("name") String name, @RequestParam String address,
                                            @RequestParam String phoneNumber, @RequestParam String email, @RequestParam int numberOfRepairSpots) {
        try {
            Long newBusinessID = (new Business()).getBusinessID(); //TODO: A new businessID will be generated?
            return new ResponseEntity<>(businessService.createBusiness(newBusinessID,name, address, phoneNumber, email, numberOfRepairSpots), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Updates business information
     *
     * @param businessDto the update to the existing business information
     * @return the updated business if updated successfully
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateBusiness(@RequestBody BusinessDto businessDto) {
        try {
            return new ResponseEntity<>(businessService.updateBusiness(businessDto.getBusinessID(), businessDto.getName(), businessDto.getAddress(),
                    businessDto.getPhoneNumber(), businessDto.getEmail(), businessDto.getNumberOfRepairSpots()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }


    /*
     * Get request for all holidays for a business
     * @return a list of all holidays
     */
    @GetMapping("/{businessID}/holidays")
    public ResponseEntity<?> getAllHolidays(@PathVariable("businessID") Long businessID) {
        try {
            List<TimeSlotDto> holidaysDtoList = businessService.getAllHolidays(businessID);
            return new ResponseEntity<>(holidaysDtoList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /*
     * Add a new holiday to the business.
     * @return a list of all holidays
     */
    @GetMapping("create/{businessID}/holidays/")
    public ResponseEntity<?> addHoliday(@PathVariable("businessID") Long businessID, @RequestParam Timestamp startDateTime,
                                        @RequestParam Timestamp endDateTime) {
        try {
            businessService.addHoliday(businessID, startDateTime, endDateTime);
            List<TimeSlotDto> holidaysDtoList = businessService.getAllHolidays(businessID);
            return new ResponseEntity<>(holidaysDtoList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
