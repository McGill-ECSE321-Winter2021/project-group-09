package ca.mcgill.ecse321.repairshop.repository;

import ca.mcgill.ecse321.repairshop.model.Service;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class ServiceTest {

    @Autowired
    private ServiceRepository serviceRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        serviceRepository.deleteAll();
    }

    // Test data
    private static final String name = "TestService";
    private static final int duration = 30;
    private static final double price = 49.99;

    @Test
    void testPersistAndLoadService() {

        // create service
        Service service = new Service();
        service.setName(name);
        service.setDuration(duration);
        service.setPrice(price);

        // save service in db
        serviceRepository.save(service);

        // retrieve service
        service = serviceRepository.findServiceByName(name);

        // assertions
        assertNotNull(service);
        assertEquals(name, service.getName());
        assertEquals(duration, service.getDuration());
        assertEquals(price, service.getPrice());

    }

    @Test
    void testDeleteService() {

        // create service
        Service service = new Service();
        service.setName(name);
        service.setDuration(duration);
        service.setPrice(price);

        // save service in db
        serviceRepository.save(service);

        // delete from db
        serviceRepository.deleteById(service.getName());

        // assertion
        assertNull(serviceRepository.findServiceByName(service.getName()));

    }

}
