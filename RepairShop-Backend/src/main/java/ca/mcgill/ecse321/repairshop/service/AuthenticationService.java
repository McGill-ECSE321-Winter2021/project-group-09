package ca.mcgill.ecse321.repairshop.service;

import ca.mcgill.ecse321.repairshop.dto.LoginDto;
import ca.mcgill.ecse321.repairshop.model.Admin;
import ca.mcgill.ecse321.repairshop.model.Customer;
import ca.mcgill.ecse321.repairshop.model.Technician;
import ca.mcgill.ecse321.repairshop.repository.AdminRepository;
import ca.mcgill.ecse321.repairshop.repository.CustomerRepository;
import ca.mcgill.ecse321.repairshop.repository.TechnicianRepository;
import ca.mcgill.ecse321.repairshop.service.utilities.JWTTokenProvider;
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
    JWTTokenProvider jwtTokenProvider;

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

    private String authenticateTechnician(String email, String password) throws AuthenticationException {
        Technician tech = technicianRepository.findTechnicianByEmail(email);
        if (tech.getPassword().equals(password)) {
            tech.setToken(jwtTokenProvider.createToken(tech.getEmail()));
            return tech.getToken();
        } else {
            throw new AuthenticationException("Invalid password.");
        }
    }

    private String authenticateAdmin(String email, String password) throws AuthenticationException {
        Admin admin = adminRepository.findAdminByEmail(email);
        if (admin.getPassword().equals(password)) {
            admin.setToken(jwtTokenProvider.createToken(admin.getEmail()));
            return admin.getToken();
        } else {
            throw new AuthenticationException("Invalid password.");
        }
    }

    private String authenticateCustomer(String email, String password) throws AuthenticationException {
        Customer customer = customerRepository.findCustomerByEmail(email);
        if (customer.getPassword().equals(password)) {
            customer.setToken(jwtTokenProvider.createToken(customer.getEmail()));
            return customer.getToken();
        } else {
            throw new AuthenticationException("Invalid password.");
        }
    }

    private void logoutTechnician(String email) {
        Technician tech = technicianRepository.findTechnicianByEmail(email);
        tech.setToken(null);
        technicianRepository.save(tech);
    }

    private void logoutCustomer(String email) {
        Customer customer = customerRepository.findCustomerByEmail(email);
        customer.setToken(null);
        customerRepository.save(customer);
    }

    private void logoutAdmin(String email) {
        Admin admin = adminRepository.findAdminByEmail(email);
        admin.setToken(null);
        adminRepository.save(admin);
    }


}
