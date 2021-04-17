package ca.mcgill.ecse321.repairshop.controller;

import java.util.List;
import ca.mcgill.ecse321.repairshop.model.Admin;
import ca.mcgill.ecse321.repairshop.repository.AdminRepository;
import ca.mcgill.ecse321.repairshop.service.AuthenticationService;
import ca.mcgill.ecse321.repairshop.service.utilities.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ca.mcgill.ecse321.repairshop.dto.AdminDto;
import ca.mcgill.ecse321.repairshop.service.AdminService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;

	@Autowired
	private EmailService emailService;

	@Autowired
	AuthenticationService authenticationService;

	@Autowired
	AdminRepository adminRepository;
	
	/**
	 * POST request to create a new administrator
	 * @param adminDto (AdminDto)
	 * @param token        for the admin to create another admin
	 * @return A administrator Dto
	 */
	@PostMapping("/register")
	public ResponseEntity<?> createAdmin(@RequestBody AdminDto adminDto, @RequestHeader String token) {
		
		try {
			if (authenticationService.validateAdminToken(token) == null) {
				return new ResponseEntity<>("Must be logged in as admin.", HttpStatus.BAD_REQUEST);
			}
			AdminDto admin = adminService.createAdmin(adminDto.getEmail(), adminDto.getPassword(), adminDto.getPhoneNumber(), adminDto.getName(), adminDto.getAddress());
			emailService.accountCreationEmail(admin.getEmail(), admin.getName(), admin.getPassword());
			return new ResponseEntity<>(admin, HttpStatus.OK);
		
		} catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
		
	/**
	 * DELETE request to delete a administrator account by email
	 * @param email of admin
	 * @param token      for the admin
	 * @return an admin dto
	 */
	@DeleteMapping("/delete/{email}")
	public ResponseEntity<?> deleteAdmin(@PathVariable("email") String email, @RequestHeader String token){
		
		try {
			if (authenticationService.validateAdminToken(token) == null) {
				return new ResponseEntity<>("Must be logged in as admin.", HttpStatus.BAD_REQUEST);
			}
			String message = adminService.deleteAdmin(email);
            return new ResponseEntity<>(message, HttpStatus.OK);  
            
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
		
	}
	
	/**
	 * GET request to get an administrator account by email
	 * @param email of admin
	 * @param token for the admin
	 * @return an admin dto
	 */
	@GetMapping("/get/{email}")
	public ResponseEntity<?> getAdmin(@PathVariable("email") String email, @RequestHeader String token){
		
		try {
			if (authenticationService.validateAdminToken(token) == null) {
				return new ResponseEntity<>("Must be logged in as admin.", HttpStatus.BAD_REQUEST);
			}
			AdminDto adminDto = adminService.getAdmin(email);
            return new ResponseEntity<>(adminDto, HttpStatus.OK);  
            
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
		
	}


	

	
	/**
	 * GET request to get all existing admins
	 * @param token for the admin
	 * @return list of admin dtos
	 * 
	 */
	@GetMapping("/all")
	public ResponseEntity<?> getAllAdmins(@RequestHeader String token) {
		
		try {
			if (authenticationService.validateAdminToken(token) == null) {
				return new ResponseEntity<>("Must be logged in as admin.", HttpStatus.BAD_REQUEST);
			}
			List<AdminDto> adminDtos = adminService.getAllAdmins();
			return new ResponseEntity<>(adminDtos, HttpStatus.OK); 
			
		} catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}

	/**
	 * POST request to change the password
	 * @param email of the user
	 * @param newPassword of the user
	 * @param token of the user
	 * @return adminDto (AdminDto)
	 */
	@PostMapping("/changePassword/{email}")
	public ResponseEntity<?> changePassword(@PathVariable("email") String email, @RequestBody String newPassword, @RequestHeader String token) {

		try {
			Admin admin = adminRepository.findAdminByEmail(email);
			Admin adminToAuth = authenticationService.validateAdminToken(token);
			if (adminToAuth == null || admin == null || !admin.getEmail().equals(admin.getEmail())) {
				return new ResponseEntity<>("Must be logged in as  requested admin.", HttpStatus.BAD_REQUEST);
			}
			AdminDto adminDto = adminService.changePassword(email, newPassword);
			return new ResponseEntity<>(adminDto, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}

	}

}
