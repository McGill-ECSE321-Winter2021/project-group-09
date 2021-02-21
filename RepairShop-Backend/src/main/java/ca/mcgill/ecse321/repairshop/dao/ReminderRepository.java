package ca.mcgill.ecse321.repairshop.dao;

import java.sql.Timestamp;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.repairshop.model.Customer;
import ca.mcgill.ecse321.repairshop.model.Reminder;
import ca.mcgill.ecse321.repairshop.model.ReminderType;

public interface ReminderRepository extends CrudRepository<Reminder, Long> {

	Reminder findReminderByReminderType(Reminder reminderType);
	
	Reminder findByCustomer(Customer customer);
	
//	Reminder findByCustomerAndType(Customer customer, ReminderType reminderType);
	
//	Reminder findByTimeStampAndCustomer(Customer customer, Timestamp timestamp);

}
