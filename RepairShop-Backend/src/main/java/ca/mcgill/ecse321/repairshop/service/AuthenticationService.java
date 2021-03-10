package ca.mcgill.ecse321.repairshop.service;

import ca.mcgill.ecse321.repairshop.dto.LoginDto;
import ca.mcgill.ecse321.repairshop.model.Admin;
import ca.mcgill.ecse321.repairshop.model.Customer;
import ca.mcgill.ecse321.repairshop.model.Technician;
import ca.mcgill.ecse321.repairshop.repository.AdminRepository;
import ca.mcgill.ecse321.repairshop.repository.CustomerRepository;
import ca.mcgill.ecse321.repairshop.repository.TechnicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AuthenticationService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    TechnicianRepository technicianRepository;

    /**'
     * Validates an email + password pair for a user type
     * @param credentials DTO containing login credentials and user type
     * @return true is login was successful otherwise false
     */
    @Transactional
    public boolean logIn(LoginDto credentials) {
        switch (credentials.getUserType()) {
            case Technician:
                return authenticateTechnician(credentials.getEmail(), credentials.getPassword());
            case Admin:
                return authenticateAdmin(credentials.getEmail(), credentials.getPassword());
            case Customer:
                return authenticateCustomer(credentials.getEmail(), credentials.getPassword());
        }
        return false;
    }

    private boolean authenticateTechnician(String email, String password) {
        Technician tech = technicianRepository.findTechnicianByEmail(email);
        return tech.getPassword().equals(password);
    }

    private boolean authenticateAdmin(String email, String password) {
        Admin admin = adminRepository.findAdminByEmail(email);
        return admin.getPassword().equals(password);
    }

    private boolean authenticateCustomer(String email, String password) {
        Customer customer = customerRepository.findCustomerByEmail(email);
        return customer.getPassword().equals(password);
    }


}
