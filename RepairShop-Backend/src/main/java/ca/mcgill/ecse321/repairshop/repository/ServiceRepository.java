package ca.mcgill.ecse321.repairshop.repository;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.repairshop.model.Service;

import java.util.List;

public interface ServiceRepository extends CrudRepository<Service, String> {

	/**
	 * Find a service by name
	 * @param name
	 * @return service (Service)
	 */
    Service findServiceByName(String name);

    /**
     * @return all services
     */
    List<Service> findAll();

}
