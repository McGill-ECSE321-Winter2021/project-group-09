package ca.mcgill.ecse321.repairshop.controller;

import java.util.List;

import ca.mcgill.ecse321.repairshop.model.Customer;
import ca.mcgill.ecse321.repairshop.repository.CustomerRepository;
import ca.mcgill.ecse321.repairshop.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.repairshop.dto.AppointmentDto;
import ca.mcgill.ecse321.repairshop.dto.CustomerDto;
import ca.mcgill.ecse321.repairshop.service.CustomerService;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AuthenticationService authenticationService;
    /**
     * POST request to create a new customer
     * @param customerDto (CustomerDto)
     * @return a customer dto
     */
    @PostMapping("/register")
    public ResponseEntity<?> createCustomer(@RequestBody CustomerDto customerDto) {

        try {

            CustomerDto customer = customerService.createCustomer(customerDto.getEmail(), customerDto.getPassword(), customerDto.getPhoneNumber(), customerDto.getName(), customerDto.getAddress());
            return new ResponseEntity<>(customer, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }


    }


    /**
     * POST request to change a password for a customer
     *
     * @param email       of customer
     * @param newPassword of customer
     * @param token for the customer to change password
     * @return a customer dto
     */
    @PostMapping("/changePassword/{email}")
    public ResponseEntity<?> changePassword(@PathVariable("email") String email, @RequestParam String newPassword, @RequestHeader String token) {

        try {
            Customer customer = customerRepository.findCustomerByEmail(email);
            Customer cusToAuth = authenticationService.validateCustomerToken(token);
            if (cusToAuth == null || customer == null || !customer.getEmail().equals(cusToAuth.getEmail())) {
                return new ResponseEntity<>("Must be logged in as the correct customer.", HttpStatus.BAD_REQUEST);
            }

            CustomerDto customerDto = customerService.changePassword(email, newPassword);
            return new ResponseEntity<>(customerDto, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }


    /**
     * DELETE request to delete a customer account by email
     *
     * @param email of customer
     * @param token for the customer to delete their account
     * @return a customer dto
     */
    @DeleteMapping("/delete/{email}")
    public ResponseEntity<?> deleteCustomer(@PathVariable("email") String email, @RequestHeader String token) {

        try {
            Customer customer = customerRepository.findCustomerByEmail(email);
            Customer cusToAuth = authenticationService.validateCustomerToken(token);
            if (cusToAuth == null || customer == null || !customer.getEmail().equals(cusToAuth.getEmail())) {
                return new ResponseEntity<>("Must be logged in as the correct customer.", HttpStatus.BAD_REQUEST);
            }

            String message = customerService.deleteCustomer(email);
            return new ResponseEntity<>(message, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }


    /**
     * GET request to get a customer account by email
     *
     * @param email of customer
     * @param token for the admin to get a customer
     * @return a customer dto
     */
    @GetMapping("/get/{email}")
    public ResponseEntity<?> getCustomer(@PathVariable("email") String email, @RequestHeader String token) {

        try {
            if (authenticationService.validateAdminToken(token) == null) {
                return new ResponseEntity<>("Must be logged in as admin.", HttpStatus.BAD_REQUEST);
            }

            CustomerDto customerDto = customerService.getCustomer(email);
            return new ResponseEntity<>(customerDto, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    /**
     * GET request to get all existing customers
     * @param token for the admin to get a list of customers
     * @return list of customer dtos
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllCustomers(@RequestHeader String token) {

        try {
            if (authenticationService.validateAdminToken(token) == null) {
                return new ResponseEntity<>("Must be logged in as admin.", HttpStatus.BAD_REQUEST);
            }

            List<CustomerDto> customerDtos = customerService.getAllCustomers();
            return new ResponseEntity<>(customerDtos, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    /**
     * GET request to get all appointments of a customer
     *
     * @param email of customer
     * @param token for the admin or customer to view all the customer's appointments
     * @return a customer dto
     */
    @GetMapping("/{email}/appointments")
    public ResponseEntity<?> viewCustomerAppointments(@PathVariable("email") String email, @RequestHeader String token) {

        try {
            Customer customer = customerRepository.findCustomerByEmail(email);
            Customer cusToAuth = authenticationService.validateCustomerToken(token);


            if (cusToAuth == null || customer == null || !customer.getEmail().equals(cusToAuth.getEmail())|| authenticationService.validateAdminToken(token)==null) {
                return new ResponseEntity<>("Must be logged in as the correct customer or admin.", HttpStatus.BAD_REQUEST);
            }

            List<AppointmentDto> appDtos = customerService.viewAppointments(email);
            return new ResponseEntity<>(appDtos, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
