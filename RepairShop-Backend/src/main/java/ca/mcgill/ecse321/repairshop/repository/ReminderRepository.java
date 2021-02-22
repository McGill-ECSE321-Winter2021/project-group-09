package ca.mcgill.ecse321.repairshop.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.repairshop.model.Customer;
import ca.mcgill.ecse321.repairshop.model.Reminder;
import ca.mcgill.ecse321.repairshop.model.ReminderType;

public interface ReminderRepository extends CrudRepository<Reminder, Long> {

	Reminder findReminderByReminderType(ReminderType reminderType);
	
	Reminder findByCustomer(Customer customer);

}
