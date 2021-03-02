package ca.mcgill.ecse321.repairshop.controller;

import ca.mcgill.ecse321.repairshop.dto.BusinessDto;
import ca.mcgill.ecse321.repairshop.model.Business;
import ca.mcgill.ecse321.repairshop.repository.BusinessRepository;
import ca.mcgill.ecse321.repairshop.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
public class BusinessRestController {

    @Autowired
    BusinessService businessService;
    private Object HttpsStatus;

    //GETTERS
    @GetMapping(value = {"/businesses", "/businesses/"})
    public List<BusinessDto> getAllBusinesses() {
        return businessService.getAllBusinesses().stream().map(b -> convertToDto(b)).collect(Collectors.toList());
    }

    @GetMapping(value = {"/businesses/{name}", "businesses/{name}/"})
    public ResponseEntity<?> getBusinessByName(@PathVariable("name") String name) throws IllegalArgumentException {
        try{
            Business business = businessService.getBusiness(name);
            return new ResponseEntity<>(convertToDto(business), HttpStatus.OK);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    //CREATE
    @PostMapping(value = {"/business/create", "/business/create/"})
    public ResponseEntity<?> createBusiness(@RequestBody BusinessDto businessDto) {
        try {
            Business business = businessService.createBusiness(businessDto.getName(), businessDto.getAddress(), businessDto.getPhoneNumber(), businessDto.getEmail(), businessDto.getNumberOfRepairSpots());
            return new ResponseEntity<>(convertToDto(business), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    //UPDATE
    @PostMapping(value = {"/business/update", "/business/update/"})
    public ResponseEntity<?> updateBusiness(@RequestBody BusinessDto businessDto){
        try{
            Business business = businessService.updateBusiness(businessDto.getName(), businessDto.getAddress(), businessDto.getPhoneNumber(), businessDto.getEmail(), businessDto.getNumberOfRepairSpots());
            return new ResponseEntity<>(convertToDto(business), HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    //TODO: Update List<TimeSlot> vacations?


    /**
     * Convert business to DTO
     *
     * @param b Business
     * @return businessDto BusinessDTo
     */
    private BusinessDto convertToDto(Business b) {
        if (b == null) {
            throw new IllegalArgumentException("The business doesn't exist");
        }

        //Create businessDto
        BusinessDto businessDto = new BusinessDto(b.getName(), b.getAddress(), b.getEmail(), b.getPhoneNumber(), b.getNumberOfRepairSpots());

        return businessDto;
    }


}
