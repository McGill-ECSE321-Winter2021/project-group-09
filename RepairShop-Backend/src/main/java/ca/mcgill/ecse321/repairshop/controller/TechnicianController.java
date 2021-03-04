


package ca.mcgill.ecse321.repairshop.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	
	
	@PostMapping(value = { "/technician/register", "/technician/register/" })
	public ResponseEntity<?> createTechnician(@RequestBody TechnicianDto techDto) throws IllegalArgumentException {
		
		try {

			TechnicianDto tech = techService.createTechnician(techDto.getEmail(), techDto.getPassword(), techDto.getPhoneNumber(), techDto.getName(), techDto.getAddress());
			return new ResponseEntity<>(tech, HttpStatus.OK); 
		
		} catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		
	}
	
	
	/*
	
	@PostMapping(value = { "/changePassword", "/changePassword/" })
	public ResponseEntity<?> changePassword(@PathVariable("email") String email, @RequestParam String newPassword) throws IllegalArgumentException {
		
		try {
			
			TechnicianDto tech = techService.changePassword(email, newPassword);
			return new ResponseEntity<>(tech, HttpStatus.OK); 
			
		} catch(Exception e) {
			 return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
	}
	
	*/
	
	
	
	@DeleteMapping(value = { "/technician/{email}", "/technician/{email}/" })
	public ResponseEntity<?> deleteTechnician(@PathVariable("email") String email){
		
		try {
			
            techService.deleteTechnician(email);
            return new ResponseEntity<>(HttpStatus.OK);  
            
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
		
	}
	
	
	@GetMapping(value = { "/technician/{email}", "/technician/{email}/" })
	public ResponseEntity<?> getTechnician(@PathVariable("email") String email){
		
		try {
			
            TechnicianDto techDto = techService.getTechnician(email);
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



