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

    @PostMapping("/create")
    public ResponseEntity<?> createAppointment(@RequestParam String startTimestamp, @RequestParam String serviceName, @RequestParam String customerEmail, @RequestParam String businessName) {
        try {
            return new ResponseEntity<>(appointmentService.createAppointment(startTimestamp, serviceName, customerEmail, businessName), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/possibilities")
    public ResponseEntity<?> getPossibleAppointments(@RequestParam String startDate, @RequestParam String serviceName, @RequestParam String businessName) {
        try {
            return new ResponseEntity<>(appointmentService.getPossibleAppointments(startDate, serviceName, businessName), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
