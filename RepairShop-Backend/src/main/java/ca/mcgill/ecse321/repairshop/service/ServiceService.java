package ca.mcgill.ecse321.repairshop.service;

import ca.mcgill.ecse321.repairshop.dto.ServiceDto;
import ca.mcgill.ecse321.repairshop.model.Service;
import ca.mcgill.ecse321.repairshop.repository.ServiceRepository;
import com.sun.javaws.exceptions.InvalidArgumentException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ServiceService {

    @Autowired
    ServiceRepository serviceRepository;

    /** Finds a service by name and returns a serviceDto
     * @param name of the service to find
     * @return serviceDto object for the corresponding service
     * @throws InvalidArgumentException if a service with the name was not found
     */
    @Transactional
    public ServiceDto getServiceByName(String name) throws Exception {

        Service service = serviceRepository.findServiceByName(name);

        // Make sure the service was found
        if (service != null) return serviceToDto(service);
        else throw new Exception("Could not find service with name " + name);
    }

    /** Gets all services and returns them in a list
     * @return a list of serviceDto objects
     */
    @Transactional
    public List<ServiceDto> getAllServices() {
        return serviceRepository.findAll().stream().map(this::serviceToDto).collect(Collectors.toList());
    }

    /** Creates a service object
     * @param name of the service (unique string)
     * @param duration of the service (int)
     * @param price of the service (double)
     * @return the service object
     * @throws Exception if the name of the service is invalid
     */
    @Transactional
    public Service createService(String name, int duration, double price) throws Exception {

        // verify that the name is valid
        if (name == null || name.equals("")) throw new Exception("A service name is required");
        else if (serviceRepository.findServiceByName(name) != null) throw new Exception("A service with name \"" + name + "\" already exists");

        Service service = new Service();
        service.setName(name);
        service.setDuration(duration);
        service.setPrice(price);

        serviceRepository.save(service);

        return service;

    }

    /** Helper method to convert Service to ServiceDto
     * @param service to convert to dto
     * @return serviceDto object
     */
    public ServiceDto serviceToDto(Service service) {
        ServiceDto serviceDto = new ServiceDto();
        serviceDto.setName(service.getName());
        serviceDto.setDuration(service.getDuration());
        serviceDto.setPrice(service.getPrice());
        return serviceDto;
    }

}