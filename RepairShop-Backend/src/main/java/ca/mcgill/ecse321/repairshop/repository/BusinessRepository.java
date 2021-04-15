package ca.mcgill.ecse321.repairshop.repository;

import ca.mcgill.ecse321.repairshop.model.Business;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BusinessRepository extends CrudRepository<Business, Long> {
	
	/**
	 * Find a business by ID
	 * @param businessID
	 * @return business (Business)
	 */
    Business findBusinessByBusinessID(Long businessID);

    /**
     * @return all the businesses
     */
    List<Business> findAll();
}
