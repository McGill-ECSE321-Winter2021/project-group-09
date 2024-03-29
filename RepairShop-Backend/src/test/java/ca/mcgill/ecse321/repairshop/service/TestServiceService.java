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

        try {
            serviceDto = serviceService.createService("", 0, 0);
        } catch (Exception e) {
            assertEquals("A service name is required", e.getMessage());
        }

        assertNull(serviceDto);

    }

    @Test // invalid service (null for name)
    public void testCreateServiceNull() {

        ServiceDto serviceDto = null;

        try {
            serviceDto = serviceService.createService(null, 0, 0);
        } catch (Exception e) {
            assertEquals("A service name is required", e.getMessage());
        }

        assertNull(serviceDto);

    }

    @Test // invalid duration (negative)
    public void testCreateServiceInvalidDuration() {

        ServiceDto serviceDto = null;

        try {
            serviceDto = serviceService.createService("Service with invalid duration", -6, 0);
        } catch (Exception e) {
            assertEquals("The duration cannot be negative", e.getMessage());
        }
        assertNull(serviceDto);
    }

    @Test // invalid price (negative)
    public void testCreateServiceInvalidPrice() {

        ServiceDto serviceDto = null;

        try {
            serviceDto = serviceService.createService("Service with invalid price", 0, -255);
        } catch (Exception e) {
            assertEquals("The price cannot be negative", e.getMessage());
        }

        assertNull(serviceDto);

    }
    @Test //valid
    public void testGetAllServices() {

        List<ServiceDto> serviceDtos = null;
        try {
            serviceDtos = serviceService.getAllServices();
        } catch (Exception e) {
            fail(e.getMessage());
        }

        // verify the entry
        assertEquals(1, serviceDtos.size());

        ServiceDto serviceDto = (ServiceDto) serviceDtos.toArray()[0];

        assertEquals(SERVICE_NAME, serviceDto.getName());
        assertEquals(SERVICE_DURATION, serviceDto.getDuration());
        assertEquals(SERVICE_PRICE, serviceDto.getPrice());

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

        ServiceDto serviceDto = null;
        String name = "inexistant";

        try{
            serviceDto = serviceService.getServiceByName(name);
        } catch (Exception e) {
            assertEquals("Could not find service with name "+name, e.getMessage());
        }

        assertNull(serviceDto);
    }

}
