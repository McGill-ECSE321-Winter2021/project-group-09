package ca.mcgill.ecse321.repairshop.dao;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.repairshop.model.Service;

public interface ServiceRepository extends CrudRepository<Service, String> {

    Service findServiceByName(String name);

}
