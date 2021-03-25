package ca.mcgill.ecse321.repairshop.controller;

import ca.mcgill.ecse321.repairshop.service.AppointmentService;
import ca.mcgill.ecse321.repairshop.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    AuthenticationService authenticationService;

    /** Endpoint to create an appointment
     * @param startTimestamp Timestamp for the start time of the appointment as a string
     * @param serviceName The name of the service for the appointment
     * @param customerEmail The email of the customer for whom to book the appointment
     * @param token for the admin or customer to create an appointment
     * @return the appointment that's been created
     */
    @PostMapping("/create")
    public ResponseEntity<?> createAppointment(@RequestParam Timestamp startTimestamp, @RequestParam String serviceName, @RequestParam String customerEmail, @RequestHeader String token) {
        try {
            if (authenticationService.validateAdminToken(token) == null && authenticationService.validateCustomerToken(token) == null) {
                return new ResponseEntity<>("Must be logged in as admin or customer.", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(appointmentService.createAppointment(startTimestamp, serviceName, customerEmail), HttpStatus.OK);
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
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
