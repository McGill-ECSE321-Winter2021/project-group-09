package ca.mcgill.ecse321.repairshop.repository;

import ca.mcgill.ecse321.repairshop.model.Business;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BusinessRepository extends CrudRepository<Business, Long> {
	
    Business findBusinessByName(String name);
    List<Business> findAll();
}
