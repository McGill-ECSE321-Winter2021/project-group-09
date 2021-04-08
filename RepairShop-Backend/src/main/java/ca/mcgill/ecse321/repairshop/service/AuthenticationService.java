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
     * '
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
     * Checks the type of user and calls the corresponding method to logout the user
     * @param credentials DTO containing login credentials and user type
     * @throws Exception if user type is invalid
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

    //Methods to validate token
    public Admin validateAdminToken(String token) {
        return adminRepository.findAdminByToken(token);
    }

    public Technician validateTechnicianToken(String token) {
        return technicianRepository.findTechnicianByToken(token);
    }

    public Customer validateCustomerToken(String token) {
        return customerRepository.findCustomerByToken(token);
    }

    
    
    /**
     * Method to log in a technician
     * @param email
     * @param password
     * @return
     * @throws AuthenticationException
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
     * Method to log in an admin
     * @param email
     * @param password
     * @return
     * @throws AuthenticationException
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
     * Method to log in a customer
     * @param email
     * @param password
     * @return
     * @throws AuthenticationException
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
     * Method to log out technician
     * @param email
     */
    private void logoutTechnician(String email) {
        Technician tech = technicianRepository.findTechnicianByEmail(email);
        tech.setToken(null);
        technicianRepository.save(tech);
    }

    /**
     * Method to log out customer
     * @param email
     */
    private void logoutCustomer(String email) {
        Customer customer = customerRepository.findCustomerByEmail(email);
        customer.setToken(null);
        customerRepository.save(customer);
    }

    /**
     * Method to log out admin
     * @param email
     */
    private void logoutAdmin(String email) {
        Admin admin = adminRepository.findAdminByEmail(email);
        admin.setToken(null);
        adminRepository.save(admin);
    }


}
