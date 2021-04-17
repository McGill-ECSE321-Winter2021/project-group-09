package ca.mcgill.ecse321.repairshop.service;

import ca.mcgill.ecse321.repairshop.dto.LoginDto;
import ca.mcgill.ecse321.repairshop.model.Admin;
import ca.mcgill.ecse321.repairshop.model.Customer;
import ca.mcgill.ecse321.repairshop.model.Technician;
import ca.mcgill.ecse321.repairshop.repository.AdminRepository;
import ca.mcgill.ecse321.repairshop.repository.CustomerRepository;
import ca.mcgill.ecse321.repairshop.repository.TechnicianRepository;
import ca.mcgill.ecse321.repairshop.service.utilities.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.naming.AuthenticationException;
import javax.transaction.Transactional;

@Service
public class AuthenticationService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    TechnicianRepository technicianRepository;

    @Autowired
    TokenProvider tokenProvider;

    /**
     * Validates an email + password pair for a user type
     *
     * @param credentials DTO containing login credentials and user type
     * @return true is login was successful otherwise false
     */
    @Transactional
    public String logIn(LoginDto credentials) throws Exception {
        switch (credentials.getUserType()) {
            case Technician:
                return authenticateTechnician(credentials.getEmail(), credentials.getPassword());
            case Admin:
                return authenticateAdmin(credentials.getEmail(), credentials.getPassword());
            case Customer:
                return authenticateCustomer(credentials.getEmail(), credentials.getPassword());
            default:
                throw new Exception("Invalid user type.");
        }
    }

    /**
     * Method to logout the user.
     * @param credentials (LoginDto)
     * @throws Exception if wrong user type
     */
    public void logout(LoginDto credentials) throws Exception {
        switch (credentials.getUserType()) {
            case Technician:
                logoutTechnician(credentials.getEmail());
                break;
            case Admin:
                logoutAdmin(credentials.getEmail());
                break;
            case Customer:
                logoutCustomer(credentials.getEmail());
                break;
            default:
                throw new Exception("Invalid user type.");
        }
    }

    /**
     * Validate admin's token
     * @param token token of the admin
     * @return admin (Admin)
     */
    public Admin validateAdminToken(String token) {
        return adminRepository.findAdminByToken(token);
    }

    /**
     * Validate technician's token
     * @param token token of the technician
     * @return technician (Technician)
     */
    public Technician validateTechnicianToken(String token) {
        return technicianRepository.findTechnicianByToken(token);
    }

    /**
     * Validate customer's token
     * @param token token of the customer
     * @return technician (Customer)
     */
    public Customer validateCustomerToken(String token) {
        return customerRepository.findCustomerByToken(token);
    }

    /**
     * Verifies that the correct technician' is logged in with the correct email and password
     * @param email of the technician
     * @param password of the technician
     * @return token of the technician (String)
     * @throws AuthenticationException if invalid password
     */
    private String authenticateTechnician(String email, String password) throws AuthenticationException {
        Technician tech = technicianRepository.findTechnicianByEmail(email);
        if (tech.getPassword().equals(password)) {
            tech.setToken(tokenProvider.createToken(tech.getEmail()));
            return tech.getToken();
        } else {
            throw new AuthenticationException("Invalid password.");
        }
    }

    /**
     * Verifies that the correct admin' is logged in with the correct email and password
     * @param email of the admin
     * @param password of the admin
     * @return token of the admin (String)
     * @throws AuthenticationException if invalid password
     */
    private String authenticateAdmin(String email, String password) throws AuthenticationException {
        Admin admin = adminRepository.findAdminByEmail(email);
        if (admin.getPassword().equals(password)) {
            admin.setToken(tokenProvider.createToken(admin.getEmail()));
            return admin.getToken();
        } else {
            throw new AuthenticationException("Invalid password.");
        }
    }

    /**
     * Verifies that the correct customer' is logged in with the correct email and password
     * @param email of the customer
     * @param password of the customer
     * @return token of the customer (String)
     * @throws AuthenticationException if invalid password
     */
    private String authenticateCustomer(String email, String password) throws AuthenticationException {
        Customer customer = customerRepository.findCustomerByEmail(email);
        if (customer.getPassword().equals(password)) {
            customer.setToken(tokenProvider.createToken(customer.getEmail()));
            return customer.getToken();
        } else {
            throw new AuthenticationException("Invalid password.");
        }
    }

    /**
     * Method to logout the technician
     * @param email of the technician
     */
    private void logoutTechnician(String email) {
        Technician tech = technicianRepository.findTechnicianByEmail(email);
        tech.setToken(null);
        technicianRepository.save(tech);
    }

    /**
     * Method to logout the customer
     * @param email of the customer
     */
    private void logoutCustomer(String email) {
        Customer customer = customerRepository.findCustomerByEmail(email);
        customer.setToken(null);
        customerRepository.save(customer);
    }

    /**
     * Method to logout the admin
     * @param email of the admin
     */
    private void logoutAdmin(String email) {
        Admin admin = adminRepository.findAdminByEmail(email);
        admin.setToken(null);
        adminRepository.save(admin);
    }


}
