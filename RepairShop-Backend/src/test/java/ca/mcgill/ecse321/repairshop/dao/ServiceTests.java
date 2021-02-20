package ca.mcgill.ecse321.repairshop.dao;

import ca.mcgill.ecse321.repairshop.model.Service;
import org.junit.jupiter.api.AfterEach;
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

    @AfterEach
    public void clearDatabase() {
        serviceRepository.deleteAll();
    }

    @Test
    void testPersistAndLoadService() {

        String name = "TestService";
        int duration = 30;
        double price = 49.99;

        Service service = new Service();
        service.setName(name);
        service.setDuration(duration);
        service.setPrice(price);
        serviceRepository.save(service);

        service = null;

        service = serviceRepository.findServiceByName(name);

        assertNotNull(service);
        assertEquals(name, service.getName());
        assertEquals(duration, service.getDuration());
        assertEquals(price, service.getPrice());

    }

}
