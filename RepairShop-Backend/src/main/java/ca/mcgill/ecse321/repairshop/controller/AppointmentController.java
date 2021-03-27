package ca.mcgill.ecse321.repairshop.controller;

import ca.mcgill.ecse321.repairshop.dto.AppointmentInfoDto;
import ca.mcgill.ecse321.repairshop.service.AppointmentService;
import ca.mcgill.ecse321.repairshop.service.AuthenticationService;
import ca.mcgill.ecse321.repairshop.service.exceptions.TimeConstraintException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    AuthenticationService authenticationService;

    /** Endpoint to create an appointment
     * @param appointmentInfoDto Contains the start time of the appointment, the service name and the customer email
     * @param token The token for the logged in customer
     * @return the appointment that's been created
     */
    @PostMapping("/create")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentInfoDto appointmentInfoDto, @RequestHeader String token) {
        try {
            if (authenticationService.validateCustomerToken(token) == null) {
                return new ResponseEntity<>("Must be logged in as a customer.", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(appointmentService.createAppointment(appointmentInfoDto.getStartTime(), appointmentInfoDto.getServiceName(), appointmentInfoDto.getCustomerEmail()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /** Endpoint to get all possible appointment times for one week
     * @param startDate The initial date to start the search of form YYYY-MM-DD
     * @param serviceName The name of the service for the appointment
     * @param token for the admin or customer to get all possible appointments
     * @return a list of timeslots for all the possible appointments
     */
    @GetMapping("/possibilities")
    public ResponseEntity<?> getPossibleAppointments(@RequestParam String startDate, @RequestParam String serviceName, @RequestHeader String token) {
        try {
            if (authenticationService.validateAdminToken(token) == null && authenticationService.validateCustomerToken(token) == null) {
                return new ResponseEntity<>("Must be logged in as admin or customer.", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(appointmentService.getPossibleAppointments(startDate, serviceName), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Controller to cancel (delete) an appointment
     * @param id ID of the appointment to be deleted
     * @param token for the admin or customer to delete an appointment
     * @return response with a possible error message
     */
    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<?> cancel(@PathVariable("id") Long id, @RequestHeader String token) {
        try {
            if (authenticationService.validateAdminToken(token) == null && authenticationService.validateCustomerToken(token) == null) {
                return new ResponseEntity<>("Must be logged in as admin or customer.", HttpStatus.BAD_REQUEST);
            }
            appointmentService.cancelAppointment(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            if (e instanceof TimeConstraintException) {
                return new ResponseEntity<>("Cant cancel within 7 days.", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
