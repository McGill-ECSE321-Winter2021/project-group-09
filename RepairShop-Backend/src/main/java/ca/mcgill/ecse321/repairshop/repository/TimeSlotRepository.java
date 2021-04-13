package ca.mcgill.ecse321.repairshop.repository;

import ca.mcgill.ecse321.repairshop.model.TimeSlot;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface TimeSlotRepository extends CrudRepository<TimeSlot, Long> {

	/**
	 * @return all timeslots
	 */
	List<TimeSlot> findAll();
	
	/**
	 * Delte a timeslot by ID
	 * @param timeSlotID
	 */
	void deleteById(Long timeSlotID);

}


