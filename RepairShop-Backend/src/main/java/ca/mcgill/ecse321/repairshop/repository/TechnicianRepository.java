package ca.mcgill.ecse321.repairshop.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


import ca.mcgill.ecse321.repairshop.model.Technician;


public interface TechnicianRepository extends CrudRepository<Technician, String>{
	
	Technician findTechnicianByEmail(String email);
	
	List<Technician> findAll();	
	
	void deleteTechnicianByEmail(String email);

	Technician findTechnicianByToken(String token);
}
