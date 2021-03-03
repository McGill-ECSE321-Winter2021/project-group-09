package ca.mcgill.ecse321.repairshop.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.repairshop.dto.TechnicianDto;
import ca.mcgill.ecse321.repairshop.dto.TimeSlotDto;
import ca.mcgill.ecse321.repairshop.model.Technician;
import ca.mcgill.ecse321.repairshop.model.TimeSlot;
import ca.mcgill.ecse321.repairshop.service.TechnicianService;




@CrossOrigin(origins = "*")
@RestController
public class TechnicianController {
	
	@Autowired
	private TechnicianService techService;
	
	
	
	@PostMapping(value = { "/technicians/{email}", "/technicians/{email}/" })
	public ResponseEntity<?> createTechnician(@PathVariable("email") String email, @RequestParam String password, @RequestParam String name, @RequestParam String address, @RequestParam String phone) throws IllegalArgumentException {
		
		try {

			TechnicianDto tech = techService.createTechnician(email, password, phone, name, address);
			return new ResponseEntity<>(tech, HttpStatus.OK); 
		
		} catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		
	}
	
	
	
	
	@PostMapping(value = { "/technicians/{email}", "/technicians/{email}/" })
	public ResponseEntity<?> changePassword(@PathVariable("email") String email, @RequestParam String newPassword) throws IllegalArgumentException {
		
		try {
			
			TechnicianDto tech = techService.changePassword(email, newPassword);
			return new ResponseEntity<>(tech, HttpStatus.OK); 
			
		} catch(Exception e) {
			 return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
	}
	
	
	
	@GetMapping(value = { "/technicians/{email}", "/technician/{email}/" })
	public ResponseEntity<?> getTechnicianByEmail(@PathVariable("email") String email){
		
		try {
			
            TechnicianDto techDto = techService.getTechnicianByEmail(email);
            return new ResponseEntity<>(techDto, HttpStatus.OK);  
            
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
		
	}
	
	
	
	
	@GetMapping(value = { "/technicians", "/technicians/" })
	public ResponseEntity<?> getAllTechnicians() {
		
		try {
			
			List<TechnicianDto> techDtos = techService.getAllTechnicians();
			return new ResponseEntity<>(techDtos, HttpStatus.OK); 
			
		} catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
	

	
	
}
