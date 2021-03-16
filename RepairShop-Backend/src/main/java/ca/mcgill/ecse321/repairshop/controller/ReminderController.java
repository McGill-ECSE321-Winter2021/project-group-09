package ca.mcgill.ecse321.repairshop.controller;

import ca.mcgill.ecse321.repairshop.service.AuthenticationService;
import ca.mcgill.ecse321.repairshop.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/reminder")
public class ReminderController {

    @Autowired
    private ReminderService reminderService;

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Get a list of a customer's reminders
     *
     * @param email of the target customer
     * @param token of the admin
     * @return a list of the customer's reminders
     */
    @GetMapping("/customer/{email}")
    public ResponseEntity<?> getCustomerReminders(@PathVariable String email, @RequestHeader String token) {
        try {
            if (authenticationService.validateAdminToken(token) == null) {
                return new ResponseEntity<>("Must be logged in as admin.", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(reminderService.getRemindersByCustomerEmail(email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Create a new reminder
     *
     * @param dateTime            of the new reminder
     * @param appointmentDateTime date and time of the appointment
     * @param serviceName         name of the service
     * @param type                of the new reminder
     * @param email               of the customer associated to the new reminder
     * @param token               of the admin
     * @return the new reminder if created successfully
     */
    @PostMapping("/create")
    public ResponseEntity<?> createReminder(@RequestBody String dateTime, @RequestBody String appointmentDateTime,
                                            @RequestBody String serviceName, @RequestBody String type, @RequestBody String email, @RequestHeader String token) {
        try {
            if (authenticationService.validateAdminToken(token) == null) {
                return new ResponseEntity<>("Must be logged in as admin.", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(reminderService.createReminder(dateTime, appointmentDateTime, serviceName, type, email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * GET request to get all reminders
     * @param token of the admin
     * @return List of all reminders
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAll(@RequestHeader String token) {
        try {
            if (authenticationService.validateAdminToken(token) == null) {
                return new ResponseEntity<>("Must be logged in as admin.", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(reminderService.getAllReminders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete a reminder by the id.
     *
     * @param reminderID ID of the reminder to be deleted
     * @param token of the admin
     * @return message to indicated whether the delete was successful
     */
    @DeleteMapping("/delete/{reminderID}")
    public ResponseEntity<?> deleteReminderByID(@PathVariable Long reminderID, @RequestHeader String token) {
        try {
            if (authenticationService.validateAdminToken(token) == null) {
                return new ResponseEntity<>("Must be logged in as admin.", HttpStatus.BAD_REQUEST);
            }
            String message = reminderService.deleteReminderById(reminderID);
            return new ResponseEntity<>(message, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


}
