package ca.mcgill.ecse321.repairshop.controller;

import ca.mcgill.ecse321.repairshop.dto.LoginDto;
import ca.mcgill.ecse321.repairshop.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {


    @Autowired
    AuthenticationService authenticationService;

    /**
     * Log in a user
     *
     * @return status based on how login went
     */
    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto credentials) {
        try {
            if (authenticationService.logIn(credentials)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); //Invalid username or pass
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); //Email not found
        }
    }
}
