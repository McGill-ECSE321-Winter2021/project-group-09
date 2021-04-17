package ca.mcgill.ecse321.repairshop.service;

import ca.mcgill.ecse321.repairshop.dto.LoginDto;
import ca.mcgill.ecse321.repairshop.model.Admin;
import ca.mcgill.ecse321.repairshop.model.Customer;
import ca.mcgill.ecse321.repairshop.model.Technician;
import ca.mcgill.ecse321.repairshop.repository.AdminRepository;
import ca.mcgill.ecse321.repairshop.repository.CustomerRepository;
import ca.mcgill.ecse321.repairshop.repository.TechnicianRepository;
import ca.mcgill.ecse321.repairshop.service.utilities.TokenProvider;
import ca.mcgill.ecse321.repairshop.service.utilities.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class TestAuthenticationService {

    final String userEmail = "bobby@gmail.com";
    final String userPassword = "letmein123";
    final String badUserEmail = "rick@gmail.com";
    @Mock
    CustomerRepository customerRepository;
    @Mock
    TechnicianRepository technicianRepository;
    @Mock
    AdminRepository adminRepository;
    @Mock
    TokenProvider tokenProvider;   //we need to keep this... can't remove it
    @InjectMocks
    AuthenticationService authenticationService;

    @BeforeEach
    public void setMockOutputs() {

        lenient().when(technicianRepository.findTechnicianByEmail(anyString())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(userEmail)) {
                Technician tech = new Technician();
                tech.setEmail(userEmail);
                tech.setPassword(userPassword);
                return tech;
            } else {
                throw new EntityNotFoundException();
            }
        });

        lenient().when(customerRepository.findCustomerByEmail(anyString())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(userEmail)) {
                Customer cus = new Customer();
                cus.setEmail(userEmail);
                cus.setPassword(userPassword);
                return cus;
            } else {
                throw new EntityNotFoundException();
            }
        });

        lenient().when(adminRepository.findAdminByEmail(anyString())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(userEmail)) {
                Admin admin = new Admin();
                admin.setEmail(userEmail);
                admin.setPassword(userPassword);
                return admin;
            } else {
                throw new EntityNotFoundException();
            }
        });
    }

    @Test
    public void testValidAdminLogin() {
        try {
            LoginDto loginDto = new LoginDto();
            loginDto.setEmail(userEmail);
            loginDto.setPassword(userPassword);
            loginDto.setUserType(UserType.Admin);
            authenticationService.logIn(loginDto);
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testInvalidAdminLogin() {
        assertThrows(EntityNotFoundException.class,
                () -> {
                    LoginDto loginDto = new LoginDto();
                    loginDto.setEmail(badUserEmail);
                    loginDto.setPassword(userPassword);
                    loginDto.setUserType(UserType.Admin);
                    authenticationService.logIn(loginDto);
                }
        );
    }


}
