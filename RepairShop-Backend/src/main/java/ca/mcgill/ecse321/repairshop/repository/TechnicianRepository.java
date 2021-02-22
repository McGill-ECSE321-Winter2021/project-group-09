package ca.mcgill.ecse321.repairshop.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


import ca.mcgill.ecse321.repairshop.model.Technician;
import ca.mcgill.ecse321.repairshop.model.TimeSlot;


public interface TechnicianRepository extends CrudRepository<Technician, String>{
	
	Technician findTechnicianByEmail(String email);
	
	Technician findByTimeslots(TimeSlot timeSlot);
	
	List<Technician> findAll();	
	
	void deleteByEmail(String email);
	
}
