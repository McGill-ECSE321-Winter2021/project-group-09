package ca.mcgill.ecse321.repairshop.controller;

import java.util.List;

import ca.mcgill.ecse321.repairshop.model.Technician;
import ca.mcgill.ecse321.repairshop.repository.TechnicianRepository;
import ca.mcgill.ecse321.repairshop.service.AuthenticationService;
import ca.mcgill.ecse321.repairshop.service.utilities.EmailService;
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
    private TechnicianRepository technicianRepository;

    @Autowired
    private TechnicianService techService;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
	EmailService emailService;


    /**
     * POST request to create a new technician
     *
     * @param techDto (TechnicianDto)
     * @param token   for the admin to register technician
     * @return A technician Dto
     */
    @PostMapping("/register")
    public ResponseEntity<?> createTechnician(@RequestBody TechnicianDto techDto, @RequestHeader String token) {

        try {
            if (authenticationService.validateAdminToken(token) == null) {
                return new ResponseEntity<>("Must be logged in as admin.", HttpStatus.BAD_REQUEST);
            }
            TechnicianDto tech = techService.createTechnician(techDto.getEmail(), techDto.getPassword(), techDto.getPhoneNumber(), techDto.getName(), techDto.getAddress());
            emailService.accountCreationEmail(tech.getEmail(),tech.getName(), tech.getPassword());
            return new ResponseEntity<>(tech, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }


    }


    /**
     * POST request to change a password for a technician
     *
     * @param email       of technician
     * @param newPassword of technician
     * @param token       of logged in technician making the request
     * @return a technician dto
     */
    @PostMapping("/changePassword/{email}")
    public ResponseEntity<?> changePassword(@PathVariable("email") String email, @RequestBody String newPassword, @RequestHeader String token) {

        try {
            Technician technician = technicianRepository.findTechnicianByEmail(email);
            Technician techToAuth = authenticationService.validateTechnicianToken(token);
            if (techToAuth == null || technician == null || !technician.getEmail().equals(techToAuth.getEmail())) {
                return new ResponseEntity<>("Must be logged in as requested technician.", HttpStatus.BAD_REQUEST);
            }
            TechnicianDto tech = techService.changePassword(email, newPassword);
            return new ResponseEntity<>(tech, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }


    /**
     * Deletes a technician by email
     *
     * @param email of the technician
     * @param token of logged in admin making the request
     * @return http response with status or error message
     */
    @DeleteMapping("/delete/{email}")
    public ResponseEntity<?> deleteTechnician(@PathVariable String email, @RequestHeader String token) {
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
     *
     * @param email of the technician
     * @param token of the admin or of the requested technician
     * @return a technician Dto
     */
    @GetMapping("/get/{email}")
    public ResponseEntity<?> getTechnician(@PathVariable("email") String email, @RequestHeader String token) {

        try {
            Technician technician = technicianRepository.findTechnicianByEmail(email);
            Technician techToAuth = authenticationService.validateTechnicianToken(token);
            if (authenticationService.validateAdminToken(token) == null && (techToAuth == null || technician == null || !technician.getEmail().equals(techToAuth.getEmail()))) {
                return new ResponseEntity<>("Must be logged in as admin or as requested technician.", HttpStatus.BAD_REQUEST);
            }
            TechnicianDto techDto = techService.getTechnician(email);
            return new ResponseEntity<>(techDto, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }


    /**
     * GET request to get all existing technicians
     *
     * @param token for the admin
     * @return list of technician Dtos
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllTechnicians(@RequestHeader String token) {

        try {
            if (authenticationService.validateAdminToken(token) == null) {
                return new ResponseEntity<>("Must be logged in as admin.", HttpStatus.BAD_REQUEST);
            }
            List<TechnicianDto> techDtos = techService.getAllTechnicians();
            return new ResponseEntity<>(techDtos, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    /**
     * GET  request to get the work hours of the technician by email
     *
     * @param email of technician
     * @param token of the admin or of the requested technician
     * @return list of timeslot Dtos
     */
    @GetMapping("/{email}/work_hours")
    public ResponseEntity<?> getTechnicianWorkHours(@PathVariable("email") String email, @RequestHeader String token) {

        try {
            if (authenticationService.validateAdminToken(token) == null && authenticationService.validateTechnicianToken(token) == null) {
                return new ResponseEntity<>("Must be logged in as admin or as requested technician.", HttpStatus.BAD_REQUEST);
            }
            List<TimeSlotDto> tDtos = techService.getWorkHours(email);
            return new ResponseEntity<>(tDtos, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    /**
     * GET request to get all work schedule of a technician
     *
     * @param email         of a technician
     * @param weekStartDate StarDate of the work week
     * @param token         of the admin or of the requested technician
     * @return list of timeslot Dtos
     */
    @GetMapping("/{email}/schedule")
    public ResponseEntity<?> viewTechnicianSchedule(@PathVariable("email") String email, @RequestBody String weekStartDate, @RequestHeader String token) {

        try {
            Technician technician = technicianRepository.findTechnicianByEmail(email);
            Technician techToAuth = authenticationService.validateTechnicianToken(token);
            if (authenticationService.validateAdminToken(token) == null && (techToAuth == null || technician == null || !technician.getEmail().equals(techToAuth.getEmail()))) {
                return new ResponseEntity<>("Must be logged in as admin or as requested technician.", HttpStatus.BAD_REQUEST);
            }
            List<TimeSlotDto> tDtos = techService.viewTechnicianSchedule(email, weekStartDate);
            return new ResponseEntity<>(tDtos, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    /**
     * GET request to get all appointments of a technician
     *
     * @param email of a technician
     * @param token of the admin or of the requested technician
     * @return list of timeslot Dtos
     */
    @GetMapping("/{email}/appointments")
    public ResponseEntity<?> viewTechnicianAppointments(@PathVariable("email") String email, @RequestHeader String token) {

        try {
            Technician technician = technicianRepository.findTechnicianByEmail(email);
            Technician techToAuth = authenticationService.validateTechnicianToken(token);
            if (authenticationService.validateAdminToken(token) == null && (techToAuth == null || technician == null || !technician.getEmail().equals(techToAuth.getEmail()))) {
                return new ResponseEntity<>("Must be logged in as admin or as requested technician.", HttpStatus.BAD_REQUEST);
            }
            List<AppointmentDto> appDtos = techService.viewAppointments(email);
            return new ResponseEntity<>(appDtos, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * POST request to add a new technician work hours
     *
     * @param email           of a technician
     * @param timeSlotDtoList (List<TimeSlotDto>)
     * @param token           of the admin
     * @return http response with status or error message
     */
    @PostMapping("/{email}/add_work_hours")
    public ResponseEntity<?> addTechnicianWorkHours(@PathVariable("email") String email, @RequestBody List<TimeSlotDto> timeSlotDtoList, @RequestHeader String token) {

        try {
            if (authenticationService.validateAdminToken(token) == null) {
                return new ResponseEntity<>("Must be logged in as admin.", HttpStatus.BAD_REQUEST);
            }
            String message = techService.addTechnicianWorkHours(email, timeSlotDtoList);
            return new ResponseEntity<>(message, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * DELETE request to delete a specific technician work hour
     *
     * @param email         of technician
     * @param timeSlotDto work hours (TimeSlotDto)
     * @param token         of the admin
     * @return whether the specific work schedule was removed successfully
     */
    @PostMapping("/delete/hours/{email}")
    public ResponseEntity<?> deleteSpecificWorkHours(@PathVariable("email") String email, @RequestBody TimeSlotDto timeSlotDto, @RequestHeader String token) {

        try {
            if (authenticationService.validateAdminToken(token) == null) {
                return new ResponseEntity<>("Must be logged in as admin.", HttpStatus.BAD_REQUEST);
            }
            String message = techService.deleteSpecificWorkHours(email, timeSlotDto.getStartDateTime(), timeSlotDto.getEndDateTime());
            return new ResponseEntity<>(message, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * DELETE request to delete the entire technician work schedule
     *
     * @param email of technician
     * @param token of the admin
     * @return whether the specific work schedule was removed successfully
     */
    @DeleteMapping("/delete/schedule/{email}")
    public ResponseEntity<?> deleteSchedule(@PathVariable("email") String email, @RequestHeader String token) {

        try {
            if (authenticationService.validateAdminToken(token) == null) {
                return new ResponseEntity<>("Must be logged in as admin.", HttpStatus.BAD_REQUEST);
            }
            String message = techService.deleteSchedule(email);
            return new ResponseEntity<>(message, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}