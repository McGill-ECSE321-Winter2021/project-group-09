


package ca.mcgill.ecse321.repairshop.controller;
import java.sql.Timestamp;
import java.util.List;
import ca.mcgill.ecse321.repairshop.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ca.mcgill.ecse321.repairshop.dto.AppointmentDto;
import ca.mcgill.ecse321.repairshop.dto.TechnicianDto;
import ca.mcgill.ecse321.repairshop.dto.TimeSlotDto;
import ca.mcgill.ecse321.repairshop.service.TechnicianService;




@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/technician")
public class TechnicianController {
	
	@Autowired
	private TechnicianService techService;

	@Autowired
	AuthenticationService authenticationService;
	
	
	
	/**
	 * POST request to create a new technician
	 * @param techDto (TechnicianDto)
	 * @return A technician Dto
	 */
	@PostMapping("/register")
	public ResponseEntity<?> createTechnician(@RequestBody TechnicianDto techDto) {
		
		try {
			TechnicianDto tech = techService.createTechnician(techDto.getEmail(), techDto.getPassword(), techDto.getPhoneNumber(), techDto.getName(), techDto.getAddress());
			return new ResponseEntity<>(tech, HttpStatus.OK); 
		
		} catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		
	}


	/**
	 * POST request to change a password for a technician
	 * @param email of technician
	 * @param newPassword of technician
	 * @return a technician dto
	 */
	@PostMapping("/changePassword/{email}")
	public ResponseEntity<?> changePassword(@PathVariable("email") String email, @RequestParam String newPassword){
		
		try {
			
			TechnicianDto tech = techService.changePassword(email, newPassword);
			return new ResponseEntity<>(tech, HttpStatus.OK); 
			
		} catch(Exception e) {
			 return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
	}
	


	/**
	 * Deletes a technician by email
	 * @param email of the technician
	 * @param token of logged in admin making the request
	 * @return http response with status or error message
	 */
	@DeleteMapping("/delete/{email}")
	public ResponseEntity<?> deleteTechnician(@PathVariable String email, @RequestHeader String token){
		try {
			if (authenticationService.validateAdminToken(token) == null) {
				return new ResponseEntity<>("Must be logged in as admin.", HttpStatus.BAD_REQUEST);
			}
            String message = techService.deleteTechnician(email);
            return new ResponseEntity<>(message, HttpStatus.OK);  
            
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
	}
	
	
	/**
	 * GET request to get the technician by email
	 * @param email of the technician
	 * @return a technician Dto
	 */
	@GetMapping("/get/{email}")
	public ResponseEntity<?> getTechnician(@PathVariable("email") String email){
		
		try {
			
            TechnicianDto techDto = techService.getTechnician(email);
            return new ResponseEntity<>(techDto, HttpStatus.OK);  
            
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
		
	}
	
	
	
	/**
	 * GET request to get all existing technicians
	 * @return list of technician Dtos
	 */
	@GetMapping("/all")
	public ResponseEntity<?> getAllTechnicians() {
		
		try {
			
			List<TechnicianDto> techDtos = techService.getAllTechnicians();
			return new ResponseEntity<>(techDtos, HttpStatus.OK); 
			
		} catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
	/**
	 * GET  request to get the work hours of the technician by email
	 * @param email of technician
	 * @return list of timeslot Dtos
	 */
	@GetMapping("/{email}/work_hours")
	public ResponseEntity<?> getTechnicianWorkHours(@PathVariable("email") String email) {
		
		try {
			
			List<TimeSlotDto> tDtos = techService.getWorkHours(email);
			return new ResponseEntity<>(tDtos, HttpStatus.OK); 
			
		} catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
	}

	/**
	 * GET request to get all work schedule of a technician
	 * @param email of a technician
	 * @param weekStartDate StarDate of the work week
	 * @return list of timeslot Dtos
	 */
	@GetMapping("/{email}/schedule")
	public ResponseEntity<?> viewTechnicianSchedule(@PathVariable("email") String email, @RequestParam("weekStartDate") String weekStartDate) {
		
		try {
			
			List<TimeSlotDto> tDtos = techService.viewTechnicianSchedule(email, weekStartDate);
			return new ResponseEntity<>(tDtos, HttpStatus.OK); 
			
		} catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
	}

	/**
	 * GET request to get all appointments of a technician
	 * @param email of a technician
	 * @return list of timeslot Dtos
	 */
	@GetMapping("/{email}/appointments")
	public ResponseEntity<?> viewTechnicianAppointments(@PathVariable("email") String email) {
		
		try {
			
			List<AppointmentDto> appDtos = techService.viewAppointments(email);
			return new ResponseEntity<>(appDtos, HttpStatus.OK); 
			
		} catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * POST request to add a new technician work hours
	 * @param email of a technician
	 * @param timeSlotDtoList (List<TimeSlotDto>)
	 * @return  http response with status or error message
	 * @throws IllegalArgumentException	 */
	@PostMapping("/{email}/add_work_hours")
	public ResponseEntity<?> addTechnicianWorkHours(@PathVariable("email") String email, @RequestBody List<TimeSlotDto> timeSlotDtoList) {
		
		try {
			String message = techService.addTechnicianWorkHours(email, timeSlotDtoList);
			return new ResponseEntity<>(message, HttpStatus.OK); 
		
		} catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	 /* DELETE request to delete a specific technician work hour
	 * @param email of technician
	 * @param startTimeSlot is beginning timeslot time to be removed
	 * @param endTimeSlot is the end timeslot time to be removed
	 * @return whether the specific work schedule was removed successfully
	 * @throws Exception if email is empty or technician cannot be found
	 */
	@DeleteMapping("/delete/hours/{email}")
	public ResponseEntity<?> deleteSpecificWorkHours(@PathVariable("email") String email, @RequestParam Timestamp startTimeSlot, @RequestParam Timestamp endTimeSlot) {
		
		try {
			String message = techService.deleteSpecificWorkHours(email, startTimeSlot, endTimeSlot);
			return new ResponseEntity<>(message, HttpStatus.OK); 
		
		} catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	 /* DELETE request to delete the entire technician work schedule
	 * @param email of technician
	 * @return whether the specific work schedule was removed successfully
	 * @throws Exception if email is empty or technician cannot be found
	 */
	@DeleteMapping("/delete/schedule/{email}")
	public ResponseEntity<?> deleteSchedule(@PathVariable("email") String email) {
		
		try {
			String message = techService.deleteSchedule(email);
			return new ResponseEntity<>(message, HttpStatus.OK); 
		
		} catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}