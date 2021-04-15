package ca.mcgill.ecse321.repairshop.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.repairshop.model.Customer;
import ca.mcgill.ecse321.repairshop.model.Reminder;

import java.util.List;

public interface ReminderRepository extends CrudRepository<Reminder, Long> {

	/**
	 * Find all the reminders for a certain customer
	 * @param customer
	 * @return list of a customer's reminders (List<Reminder>)
	 */
	List<Reminder> findByCustomer(Customer customer);
	
	/**
	 * @return all reminders
	 */
	List<Reminder> findAll();
	
	/**
	 * Delete a reminder by ID
	 * @param reminderID
	 */
	void deleteById(Long reminderID);
}
