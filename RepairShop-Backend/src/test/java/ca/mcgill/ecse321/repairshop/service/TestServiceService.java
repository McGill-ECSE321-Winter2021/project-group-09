package ca.mcgill.ecse321.repairshop.service;

import ca.mcgill.ecse321.repairshop.dto.ServiceDto;
import ca.mcgill.ecse321.repairshop.model.Service;
import ca.mcgill.ecse321.repairshop.repository.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class TestServiceService {

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private ServiceService serviceService;

    // Test data
    private static final String SERVICE_NAME = "TestBusiness";
    private static final int SERVICE_DURATION = 2;
    private static final double SERVICE_PRICE = 49.99;

    @BeforeEach
    public void setMockOutput() {

        lenient().when(serviceRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {

            Service service = new Service();
            service.setName(SERVICE_NAME);
            service.setDuration(SERVICE_DURATION);
            service.setPrice(SERVICE_PRICE);

            List<Service> services = new ArrayList<>();
            services.add(service);
            return services;

        });

        lenient().when(serviceRepository.findServiceByName(anyString())).thenAnswer((InvocationOnMock invocation) -> {

            if (invocation.getArgument(0).equals(SERVICE_NAME)) {
                Service service = new Service();
                service.setName(SERVICE_NAME);
                service.setDuration(SERVICE_DURATION);
                service.setPrice(SERVICE_PRICE);
                return service;
            } else return null;

        });

        lenient().when(serviceRepository.save(any(Service.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

    }

    @Test // valid service
    public void testCreateService() {

        ServiceDto serviceDto = null;

        try {
            serviceDto = serviceService.createService("TestService2", SERVICE_DURATION, SERVICE_PRICE);
        } catch (Exception e) {
            fail();
        }

        assertNotNull(serviceDto);
        assertEquals("TestService2", serviceDto.getName());
        assertEquals(SERVICE_DURATION, serviceDto.getDuration());
        assertEquals(SERVICE_PRICE, serviceDto.getPrice());

    }

    @Test // invalid service (string for name)
    public void testCreateServiceEmpty() {

        ServiceDto serviceDto = null;
        String error = null;

        try {
            serviceDto = serviceService.createService("", 0, 0);
        } catch (Exception e) {
            error = e.getMessage();
        }

        assertNull(serviceDto);
        assertEquals("A service name is required", error);

    }

    @Test // invalid service (null for name)
    public void testCreateServiceNull() {

        ServiceDto serviceDto = null;
        String error = null;

        try {
            serviceDto = serviceService.createService(null, 0, 0);
        } catch (Exception e) {
            error = e.getMessage();
        }

        assertNull(serviceDto);
        assertEquals("A service name is required", error);

    }

    @Test
    public void testGetAllServices() {
        List<ServiceDto> serviceDtos = serviceService.getAllServices();
        assertEquals(1, serviceDtos.size()); // List contains a single element.
        ServiceDto serviceDto = (ServiceDto) serviceDtos.toArray()[0];
        assertEquals(SERVICE_NAME, serviceDto.getName()); // List contains the only comment.
        assertEquals(SERVICE_DURATION, serviceDto.getDuration()); // List contains the only comment.
        assertEquals(SERVICE_PRICE, serviceDto.getPrice()); // List contains the only comment.

    }

    @Test //valid service name
    public void testServiceByName() {
        ServiceDto serviceDto = null;
        try{
            serviceDto = serviceService.getServiceByName(SERVICE_NAME);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertNotNull(serviceDto);
        assertEquals(SERVICE_NAME, serviceDto.getName());
        assertEquals(SERVICE_DURATION, serviceDto.getDuration());
        assertEquals(SERVICE_PRICE, serviceDto.getPrice());
    }

    @Test //invalid service name
    public void testServiceByNameNonExistent() {
        String error=null;
        ServiceDto serviceDto = null;
        String name = "I don't exist";
        try{
            serviceDto = serviceService.getServiceByName(name);
        } catch (Exception e) {
            error = e.getMessage();
        }
        assertNull(serviceDto);
        assertEquals("Could not find service with name "+name, error);
    }

}
