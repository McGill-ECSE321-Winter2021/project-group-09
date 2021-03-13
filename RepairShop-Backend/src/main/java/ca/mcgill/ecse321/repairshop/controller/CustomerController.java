package ca.mcgill.ecse321.repairshop.controller;

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

import ca.mcgill.ecse321.repairshop.dto.CustomerDto;
import ca.mcgill.ecse321.repairshop.service.CustomerService;




@RestController
@CrossOrigin(origins = "*")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	
	/**
	 * POST request to create a new customer 
	 * @param customerDto
	 * @return a customer dto
	 * @throws IllegalArgumentException
	 */
	@PostMapping(value = { "/customer/register", "/customer/register/" })
	public ResponseEntity<?> createCustomer(@RequestBody CustomerDto customerDto) throws IllegalArgumentException {
		
		try {

			CustomerDto customer = customerService.createCustomer(customerDto.getEmail(), customerDto.getPassword(), customerDto.getPhoneNumber(), customerDto.getName(), customerDto.getAddress());
			return new ResponseEntity<>(customer, HttpStatus.OK); 
		
		} catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		
	}
	
	
	/*
	
	@PostMapping(value = { "/customers/{email}", "/customers/{email}/" })
	public ResponseEntity<?> changePassword(@PathVariable("email") String email, @RequestParam String newPassword) throws IllegalArgumentException {
		
		try {
			
			CustomerDto customer = customerService.changePassword(email, newPassword);
			return new ResponseEntity<>(customer, HttpStatus.OK); 
			
		} catch(Exception e) {
			 return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
	}
	
	*/
	
	
	/**
	 * DELETE request to delete a customer account by email
	 * @param email
	 * @return
	 */
	@DeleteMapping(value = { "/customer/{email}", "/customer/{email}/" })
	public ResponseEntity<?> deleteCustomer(@PathVariable("email") String email){
		
		try {
			
			String message = customerService.deleteCustomer(email);
            return new ResponseEntity<>(message, HttpStatus.OK);  
            
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
		
	}
	
	
	/**
	 * GET request to get a customer account by email
	 * @param email
	 * @return a customer dto
	 */
	@GetMapping(value = { "/customer/{email}", "/customer/{email}/" })
	public ResponseEntity<?> getCustomer(@PathVariable("email") String email){
		
		try {
			
			CustomerDto customerDto = customerService.getCustomer(email);
            return new ResponseEntity<>(customerDto, HttpStatus.OK);  
            
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
		
	}
	
	
	
	/**
	 * GET request to get all existing customers
	 * @return list of customer dtos
	 * 
	 */
	@GetMapping(value = { "/customer/all", "/customer/all/" })
	public ResponseEntity<?> getAllCustomers() {
		
		try {
			
			List<CustomerDto> customerDtos = customerService.getAllCustomers();
			return new ResponseEntity<>(customerDtos, HttpStatus.OK); 
			
		} catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

}
