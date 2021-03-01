package ca.mcgill.ecse321.repairshop.repository;

import ca.mcgill.ecse321.repairshop.model.Technician;
import ca.mcgill.ecse321.repairshop.model.TimeSlot;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface TimeSlotRepository extends CrudRepository<TimeSlot, Long> {

	TimeSlot findTimeSlotByTimeSlotID(Long timeSlotID);
	
	TimeSlot findByTechnician(Technician technician);

	List<TimeSlot> findTimeslotsByTechnician(Technician technician);
	
	List<TimeSlot> findAll();	
	
}


