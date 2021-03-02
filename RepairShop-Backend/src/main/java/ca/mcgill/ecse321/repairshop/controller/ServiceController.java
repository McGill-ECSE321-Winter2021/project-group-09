package ca.mcgill.ecse321.repairshop.controller;

import ca.mcgill.ecse321.repairshop.dto.ServiceDto;
import ca.mcgill.ecse321.repairshop.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/service")
public class ServiceController {

    @Autowired
    ServiceService serviceService;

    /** Get request for all services
     * @return a list of all services
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        try {
            return new ResponseEntity<>(serviceService.getAllServices(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /** Get a service by name
     * @param name unique service name
     * @return the requested service if found
     */
    @GetMapping("/{name}")
    public ResponseEntity<?> getService(@PathVariable String name) {
        try {
            ServiceDto serviceDto = serviceService.getServiceByName(name); // throws exception if not found
            return new ResponseEntity<>(serviceDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /** Create a new service
     * @param name of the new service
     * @param duration of the new service
     * @param price of the new service
     * @return the new service if created successfully
     */
    @GetMapping("/create/{name}")
    public ResponseEntity<?> createService(@PathVariable String name, @RequestParam int duration, @RequestParam double price) {
        try {
            return new ResponseEntity<>(serviceService.serviceToDto(serviceService.createService(name, duration, price)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
