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

    @PostMapping("/book")
    public ResponseEntity<?> book(@RequestParam String startTime, @RequestParam String serviceName, @RequestParam String technicianEmails) {




        return null; // temp
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
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

}
