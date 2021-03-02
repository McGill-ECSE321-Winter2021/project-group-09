package ca.mcgill.ecse321.repairshop.repository;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.repairshop.model.Service;

import java.util.List;

public interface ServiceRepository extends CrudRepository<Service, String> {

    Service findServiceByName(String name);

    List<Service> findAll();

}
