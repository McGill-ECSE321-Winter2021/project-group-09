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

    /**
     * GET request to get all timeslots
     * @return List of all timeslots
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        try {
            return new ResponseEntity<>(timeSlotService.getAllTimeslots(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     *  GET request to get a timeslot by id
     * @param id ID of the desired timeslot
     * @return a single timeslot
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getTimeslot(@PathVariable Long id) {
        try {
            TimeSlotDto timeslot = timeSlotService.getTimeslotByID(id);
            if (timeslot != null) {
                return new ResponseEntity<>(timeslot, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    /**
//     * GET request to get all timeslots associated to a technician
//     * @param email identifier of the technician
//     * @return a list of associated timeslots
//     */
//    @GetMapping("/technician/{email}")
//    public ResponseEntity<?> getTechniciansTimeslots(@PathVariable String email) {
//        try {
//            List<TimeSlotDto> timeslots = timeSlotService.getTimeslotByTechnician(email);
//            if (timeslots != null) {
//                return new ResponseEntity<>(timeslots, HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}
