package ca.mcgill.ecse321.repairshop.service;

import ca.mcgill.ecse321.repairshop.model.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class TestServiceService {

    @InjectMocks
    private ServiceService serviceService;

    // Test data
    private static final String SERVICE_NAME = "TestBusiness";
    private static final int SERVICE_DURATION = 2;
    private static final double SERVICE_PRICE = 49.99;

    @BeforeEach
    public void setMockOutput() {
        lenient().when(serviceService.getAllServices()).thenAnswer((InvocationOnMock invocation) -> Collections.emptyList());
    }

    @Test // valid service
    public void testCreateService() {

        assertEquals(0, serviceService.getAllServices().size());

        Service service = null;
        String name = "TestService2";

        try {
            service = serviceService.createService(name, SERVICE_DURATION, SERVICE_PRICE);
        } catch (Exception e) {
            fail();
        }

        assertNotNull(service);
        assertEquals(name, service.getName());

    }

    @Test // invalid service (string for name)
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

    @Test // invalid service (null for name)
    public void testCreateServiceNull() {

        Service service = null;
        String error = null;

        try {
            service = serviceService.createService(null, 0, 0);
        } catch (Exception e) {
            error = e.getMessage();
        }

        assertNull(service);
        assertEquals("A service name is required", error);

    }

}
