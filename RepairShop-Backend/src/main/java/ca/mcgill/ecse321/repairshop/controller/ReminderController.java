package ca.mcgill.ecse321.repairshop.controller;

import ca.mcgill.ecse321.repairshop.model.Customer;
import ca.mcgill.ecse321.repairshop.model.ReminderType;
import ca.mcgill.ecse321.repairshop.repository.CustomerRepository;
import ca.mcgill.ecse321.repairshop.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/reminder")
public class ReminderController {

    @Autowired
    private ReminderService reminderService;

    @Autowired
    private CustomerRepository customerRepository;

    /** Get a list of a customer's reminders
     * @param email the customer's email
     * @return a list of the customer's reminders
     */
    @GetMapping("/{email}")
    public ResponseEntity<?> getCustomerReminders(@PathVariable String email) {
        try {
            return new ResponseEntity<>(reminderService.getRemindersByCustomer(customerRepository.findCustomerByEmail(email)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /** Create a new reminder
     * @param dateTime of the new reminder
     * @param reminderType of the new reminder
     * @param customer associated to the new reminder
     * @return the new reminder if created successfully
     */
    @PostMapping("/create")
    public ResponseEntity<?> createReminder(@RequestParam Timestamp dateTime, @RequestParam ReminderType reminderType, @RequestParam Customer customer) {
        try {
            return new ResponseEntity<>(reminderService.createReminder(dateTime, reminderType, customer), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
