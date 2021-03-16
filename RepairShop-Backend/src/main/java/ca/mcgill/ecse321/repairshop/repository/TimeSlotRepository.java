package ca.mcgill.ecse321.repairshop.repository;

import ca.mcgill.ecse321.repairshop.model.TimeSlot;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface TimeSlotRepository extends CrudRepository<TimeSlot, Long> {

	List<TimeSlot> findAll();
	void deleteById(Long timeSlotID);

}


