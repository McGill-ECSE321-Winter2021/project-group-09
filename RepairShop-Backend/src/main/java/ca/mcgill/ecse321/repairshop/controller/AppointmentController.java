package ca.mcgill.ecse321.repairshop.controller;

import ca.mcgill.ecse321.repairshop.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
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

}
