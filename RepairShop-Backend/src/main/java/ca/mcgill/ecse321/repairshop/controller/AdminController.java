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
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.repairshop.dto.AdminDto;
import ca.mcgill.ecse321.repairshop.service.AdminService;

@CrossOrigin(origins = "*")
@RestController
public class AdminController {

	@Autowired
	private AdminService adminService; 
	
	/**
	 * POST request to create a new administrator
	 * @param adminDto
	 * @return A administrator Dto
	 * @throws IllegalArgumentException
	 */
	@PostMapping(value = { "/admin/register", "/admin/register/" })
	public ResponseEntity<?> createAdmin(@RequestBody AdminDto adminDto) throws IllegalArgumentException {
		
		try {

			AdminDto admin = adminService.createAdmin(adminDto.getEmail(), adminDto.getPassword(), adminDto.getPhoneNumber(), adminDto.getName(), adminDto.getAddress());
			return new ResponseEntity<>(admin, HttpStatus.OK); 
		
		} catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
		
	/**
	 * DELETE request to delete a administrator account by email
	 * @param email
	 * @return an admin dto
	 */
	@DeleteMapping(value = { "/admin/{email}", "/admin/{email}/" })
	public ResponseEntity<?> deleteAdmin(@PathVariable("email") String email){
		
		try {
			
			String message = adminService.deleteAdmin(email);
            return new ResponseEntity<>(message, HttpStatus.OK);  
            
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
		
	}
	
	
	/**
	 * GET request to get an administrator account by email
	 * @param email
	 * @return an admin dto
	 */
	@GetMapping(value = { "/admin/{email}", "/admin/{email}/" })
	public ResponseEntity<?> getAdmin(@PathVariable("email") String email){
		
		try {
			
			AdminDto adminDto = adminService.getAdmin(email);
            return new ResponseEntity<>(adminDto, HttpStatus.OK);  
            
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
		
	}

	
	/**
	 * GET request to get all existing admins
	 * @return list of admin dtos
	 * 
	 */
	@GetMapping(value = { "/admin/all", "/admin/all/" })
	public ResponseEntity<?> getAllAdmins() {
		
		try {
			
			List<AdminDto> adminDtos = adminService.getAllAdmins();
			return new ResponseEntity<>(adminDtos, HttpStatus.OK); 
			
		} catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}

	
}
