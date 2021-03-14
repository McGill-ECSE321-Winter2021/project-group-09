package ca.mcgill.ecse321.repairshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    ServiceController serviceController;

    /** Create a new service
     * @param name of the new service
     * @param duration of the new service
     * @param price of the new service
     * @return the new service if created successfully
     */
    @PostMapping("/service/{name}")
    public ResponseEntity<?> createService(@PathVariable String name, @RequestParam int duration, @RequestParam double price) {
        try {
            return new ResponseEntity<>(serviceController.createService(name, duration, price), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
