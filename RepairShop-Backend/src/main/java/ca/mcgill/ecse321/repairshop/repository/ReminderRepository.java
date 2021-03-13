package ca.mcgill.ecse321.repairshop.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.repairshop.model.Customer;
import ca.mcgill.ecse321.repairshop.model.Reminder;
import ca.mcgill.ecse321.repairshop.model.ReminderType;

import java.util.List;

public interface ReminderRepository extends CrudRepository<Reminder, Long> {

	Reminder findReminderByReminderType(ReminderType reminderType);
	
	List<Reminder> findByCustomer(Customer customer);
	List<Reminder> findAll();
	void deleteById(Long reminderID);

}
