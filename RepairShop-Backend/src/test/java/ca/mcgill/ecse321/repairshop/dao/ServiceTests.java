package ca.mcgill.ecse321.repairshop.dao;

import ca.mcgill.ecse321.repairshop.model.Service;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ServiceTests {

    @Autowired
    private ServiceRepository serviceRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        serviceRepository.deleteAll();
    }

    @Test
    void testPersistAndLoadService() {

        // create service
        String name = "TestService";
        int duration = 30;
        double price = 49.99;

        Service service = new Service();
        service.setName(name);
        service.setDuration(duration);
        service.setPrice(price);

        // save service in db
        serviceRepository.save(service);

        service = null;

        // retrieve service
        service = serviceRepository.findServiceByName(name);

        // assertions
        assertNotNull(service);
        assertEquals(name, service.getName());
        assertEquals(duration, service.getDuration());
        assertEquals(price, service.getPrice());

    }

}
