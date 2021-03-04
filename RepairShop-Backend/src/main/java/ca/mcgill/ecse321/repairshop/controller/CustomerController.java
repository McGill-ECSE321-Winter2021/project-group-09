package ca.mcgill.ecse321.repairshop.controller;

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

import ca.mcgill.ecse321.repairshop.dto.CustomerDto;
import ca.mcgill.ecse321.repairshop.service.CustomerService;




@RestController
@CrossOrigin(origins = "*")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	
	
	@PostMapping(value = { "/customer/{email}", "/customer/{email}/" })
	public ResponseEntity<?> createCustomer(@PathVariable("email") String email, @RequestParam String password, @RequestParam String name, @RequestParam String address, @RequestParam String phone) throws IllegalArgumentException {
		
		try {

			CustomerDto customer = customerService.createCustomer(email, password, phone, name, address);
			return new ResponseEntity<>(customer, HttpStatus.OK); 
		
		} catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		
	}
	
	
	
	
	@PostMapping(value = { "/customers/{email}", "/customers/{email}/" })
	public ResponseEntity<?> changePassword(@PathVariable("email") String email, @RequestParam String newPassword) throws IllegalArgumentException {
		
		try {
			
			CustomerDto customer = customerService.changePassword(email, newPassword);
			return new ResponseEntity<>(customer, HttpStatus.OK); 
			
		} catch(Exception e) {
			 return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
	}
	
	
	
	@GetMapping(value = { "/customers/{email}", "/customer/{email}/" })
	public ResponseEntity<?> deleteCustomer(@PathVariable("email") String email){
		
		try {
			
			customerService.deleteCustomer(email);
            return new ResponseEntity<>(HttpStatus.OK);  
            
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
		
	}
	
	
	@GetMapping(value = { "/customers/{email}", "/customers/{email}/" })
	public ResponseEntity<?> getCustomer(@PathVariable("email") String email){
		
		try {
			
			CustomerDto customerDto = customerService.getCustomer(email);
            return new ResponseEntity<>(customerDto, HttpStatus.OK);  
            
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
		
	}
	
	
	
	
	@GetMapping(value = { "/customers", "/customers/" })
	public ResponseEntity<?> getAllCustomers() {
		
		try {
			
			List<CustomerDto> customerDtos = customerService.getAllCustomers();
			return new ResponseEntity<>(customerDtos, HttpStatus.OK); 
			
		} catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

}
