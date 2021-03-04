package ca.mcgill.ecse321.repairshop.service;

import ca.mcgill.ecse321.repairshop.model.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TestServiceService {

    @InjectMocks
    private ServiceService serviceService;

    @BeforeEach
    public void setMockOutput() {
        // TODO: finish setMockOutput for Service
    }

    // Test data
    private static final String name = "TestService";
    private static final int duration = 30;
    private static final double price = 49.99;

    @Test // valid service
    public void testCreateService() {

        assertEquals(0, serviceService.getAllServices().size());

        Service service = null;

        try {
            service = serviceService.createService(name, duration, price);
        } catch (Exception e) {
            fail();
        }

        assertNotNull(service);
        assertEquals(name, service.getName());

    }

    @Test // invalid service
    public void testCreateServiceEmpty() {

        Service service = null;
        String error = null;

        try {
            service = serviceService.createService("", 0, 0);
        } catch (Exception e) {
            error = e.getMessage();
        }

        assertNull(service);
        assertEquals("A service name is required", error);

    }

}
