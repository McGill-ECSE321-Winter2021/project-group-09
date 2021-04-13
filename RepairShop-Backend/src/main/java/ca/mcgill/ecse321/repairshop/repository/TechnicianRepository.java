package ca.mcgill.ecse321.repairshop.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


import ca.mcgill.ecse321.repairshop.model.Technician;


public interface TechnicianRepository extends CrudRepository<Technician, String>{
	
	/**
	 * Find a technician by email
	 * @param email
	 * @return
	 */
	Technician findTechnicianByEmail(String email);
	
	/**
	 * @return all technicians
	 */
	List<Technician> findAll();	
	
	/**
	 * Delete the technician with the given email
	 * @param email
	 */
	void deleteTechnicianByEmail(String email);

	/**
	 * Find a technician by token
	 * @param token
	 * @return
	 */
	Technician findTechnicianByToken(String token);
}
