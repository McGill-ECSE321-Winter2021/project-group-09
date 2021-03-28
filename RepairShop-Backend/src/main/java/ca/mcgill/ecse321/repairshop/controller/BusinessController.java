package ca.mcgill.ecse321.repairshop.controller;

import ca.mcgill.ecse321.repairshop.dto.BusinessDto;
import ca.mcgill.ecse321.repairshop.dto.TimeSlotDto;
import ca.mcgill.ecse321.repairshop.service.AuthenticationService;
import ca.mcgill.ecse321.repairshop.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/business")
public class BusinessController {

    @Autowired
    BusinessService businessService;

    @Autowired
    AuthenticationService authenticationService;

    /**
     * Get the business
     *
     * @return the business if found
     */
    @GetMapping("/info")
    public ResponseEntity<?> getBusiness() {
        try {
            BusinessDto businessDto = businessService.getBusiness();
            return new ResponseEntity<>(businessDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Create a new business
     *
     * @param businessDto The businessDto object from which to create the business
     * @param token of the admin
     * @return the new business if created successfully
     */
    @PostMapping("/create")
    public ResponseEntity<?> createBusiness(@RequestBody BusinessDto businessDto, @RequestHeader String token) {
        try {
            if (authenticationService.validateAdminToken(token) == null) {
                return new ResponseEntity<>("Must be logged in as admin.", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(businessService.createBusiness(businessDto.getName(), businessDto.getAddress(), businessDto.getPhoneNumber(), businessDto.getEmail()), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Updates business information
     *
     * @param businessDto the update to the existing business information
     * @param token of the admin
     * @return the updated business if updated successfully
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateBusiness(@RequestBody BusinessDto businessDto, @RequestHeader String token) {
        try {
            if (authenticationService.validateAdminToken(token) == null) {
                return new ResponseEntity<>("Must be logged in as admin.", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(businessService.updateBusiness(businessDto.getName(), businessDto.getAddress(),
                    businessDto.getPhoneNumber(), businessDto.getEmail(), businessDto.getNumberOfRepairSpots()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * Get request for all holidays for a business
     * @return a list of all holidays
     */
    @GetMapping("/holidays")
    public ResponseEntity<?> getAllHolidays() {
        try {
            List<TimeSlotDto> holidaysDtoList = businessService.getAllHolidays();
            return new ResponseEntity<>(holidaysDtoList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),  HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Add a new holiday to the business.
     * @param timeSlot Holiday time slot (TimeSlotDto)
     * @param token of the admin
     * @return a list of all holidays
     */
    @PostMapping("/create/holidays")
    public ResponseEntity<?> addHoliday(@RequestBody TimeSlotDto timeSlot, @RequestHeader String token) {
        try {
            if (authenticationService.validateAdminToken(token) == null) {
                return new ResponseEntity<>("Must be logged in as admin.", HttpStatus.BAD_REQUEST);
            }
            BusinessDto dto = businessService.addHoliday(timeSlot.getStartDateTime(), timeSlot.getEndDateTime());
            List<TimeSlotDto> holidaysDtoList = dto.getHolidays();
            return new ResponseEntity<>(holidaysDtoList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Delete a holiday from the business.
     * @param timeSlot Holiday time slot to delete(TimeSlotDto)
     * @param token of the admin
     * @return a list of all holidays
     */
    @DeleteMapping("/delete/holidays")
    public ResponseEntity<?> deleteHoliday(@RequestBody TimeSlotDto timeSlot, @RequestHeader String token) {
        try {
            if (authenticationService.validateAdminToken(token) == null) {
                return new ResponseEntity<>("Must be logged in as admin.", HttpStatus.BAD_REQUEST);
            }
            String message =  businessService.deleteHoliday(timeSlot.getStartDateTime(), timeSlot.getEndDateTime());

            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /** Method to get the number of available repair spots at the current time
     * @return number of available repair spots
     */
    @GetMapping("/spots")
    public ResponseEntity<?> getAvailableSpots() {
        try {
            return new ResponseEntity<>(businessService.getAvailableRepairSpots(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


}
