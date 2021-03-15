package ca.mcgill.ecse321.repairshop.controller;

import ca.mcgill.ecse321.repairshop.service.AppointmentService;
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

    /** Endpoint to create an appointment
     * @param startTimestamp Timestamp for the start time of the appointment as a string
     * @param serviceName The name of the service for the appointment
     * @param customerEmail The email of the customer for whom to book the appointment
     * @param businessName The name of the business (to check holidays)
     * @return the appointment that's been created
     */
    @PostMapping("/create")
    public ResponseEntity<?> createAppointment(@RequestParam String startTimestamp, @RequestParam String serviceName, @RequestParam String customerEmail, @RequestParam String businessName) {
        try {
            return new ResponseEntity<>(appointmentService.createAppointment(startTimestamp, serviceName, customerEmail, businessName), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /** Endpoint to get all possible appointment times for one week
     * @param startDate The initial date to start the search
     * @param serviceName The name of the service for the appointment
     * @param businessName The name of the business (to check holidays)
     * @return a list of timeslots for all the possible appointments
     */
    @GetMapping("/possibilities")
    public ResponseEntity<?> getPossibleAppointments(@RequestParam String startDate, @RequestParam String serviceName, @RequestParam String businessName) {
        try {
            return new ResponseEntity<>(appointmentService.getPossibleAppointments(startDate, serviceName, businessName), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Controller to cancel (delete) an appointment
     * @param id ID of the appointment to be deleted
     * @return response with a possible error message
     */
    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<?> cancel(@PathVariable("id") Long id) {
        try {
            appointmentService.cancelAppointment(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
