package ca.mcgill.ecse321.repairshop.controller;

import ca.mcgill.ecse321.repairshop.dto.ServiceDto;
import ca.mcgill.ecse321.repairshop.service.AuthenticationService;
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

    @Autowired
    AuthenticationService authenticationService;

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
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /** Create a new service
     * @param  serviceDto (ServiceDto)
     * @param token of the admin
     * @return the new service if created successfully
     */
    @PostMapping("/create")
    public ResponseEntity<?> createService(@RequestBody ServiceDto serviceDto, @RequestHeader String token) {
        try {
            if (authenticationService.validateAdminToken(token) == null) {
                return new ResponseEntity<>("Must be logged in as admin.", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(serviceService.createService(serviceDto.getName(), serviceDto.getDuration(), serviceDto.getPrice()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
