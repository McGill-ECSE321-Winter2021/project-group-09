package ca.mcgill.ecse321.repairshop.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;


import ca.mcgill.ecse321.repairshop.model.Technician;
import ca.mcgill.ecse321.repairshop.model.TimeSlot;

public interface TechnicianRepository extends CrudRepository<Technician, Long>{
	
	Optional<Technician> findById(Long technicianID);
	
	Technician findByTimeslots(TimeSlot timeSlot);
	
	List<Technician> findAll();	
	
	void deleteById(Long technicianID);
	
	void deleteAll();
	
}
