package ca.mcgill.ecse321.repairshop.controller;

import ca.mcgill.ecse321.repairshop.dto.TimeSlotDto;
import ca.mcgill.ecse321.repairshop.service.TimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/timeslot")
public class TimeSlotController {

    @Autowired
    TimeSlotService timeSlotService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getTimeslot(@PathVariable Long id) {
        TimeSlotDto timeslot = timeSlotService.getTimeslotByID(id);
        if (timeslot != null) {
            return new ResponseEntity<>(timeslot, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<?> getEmployeesTimeslots(@PathVariable Long id) {
        List<TimeSlotDto> timeslots = timeSlotService.getTimeslotByEmployee(id);
        if (timeslots != null) {
            return new ResponseEntity<>(timeslots, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
