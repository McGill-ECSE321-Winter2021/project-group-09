package ca.mcgill.ecse321.repairshop.controller;

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

    /** Get a list of a customer's reminders
     * @param email of the target customer
     * @return a list of the customer's reminders
     */
    @GetMapping("/customer")
    public ResponseEntity<?> getCustomerReminders(@RequestParam String email) {
        try {
            return new ResponseEntity<>(reminderService.getRemindersByCustomerEmail(email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /** Create a new reminder
     * @param dateTime of the new reminder
     * @param type of the new reminder
     * @param email of the customer associated to the new reminder
     * @return the new reminder if created successfully
     */
    @PostMapping("/create")
    public ResponseEntity<?> createReminder(@RequestParam String dateTime, @RequestParam String type, @RequestParam String email) {
        try {
            return new ResponseEntity<>(reminderService.createReminder(dateTime, type, email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
