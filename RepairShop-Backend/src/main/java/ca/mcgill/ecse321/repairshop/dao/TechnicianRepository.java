package ca.mcgill.ecse321.repairshop.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import ca.mcgill.ecse321.repairshop.model.Technician;
import ca.mcgill.ecse321.repairshop.model.TimeSlot;


public interface TechnicianRepository extends CrudRepository<Technician, String>{
	
	Technician findTechnicianByEmail(String email);
	
	Technician findByTimeslots(TimeSlot timeSlot);
	
	List<Technician> findAll();	
	
	void deleteByEmail(String email);
	
}
