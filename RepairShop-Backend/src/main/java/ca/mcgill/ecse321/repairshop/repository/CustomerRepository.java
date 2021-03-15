package ca.mcgill.ecse321.repairshop.repository;

import ca.mcgill.ecse321.repairshop.model.Appointment;
import ca.mcgill.ecse321.repairshop.model.Customer;
import ca.mcgill.ecse321.repairshop.model.Reminder;
import ca.mcgill.ecse321.repairshop.model.Technician;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, String> {

    Customer findCustomerByEmail(String email);

    // Get customer by appointment - One customer is associated to many appointments
    Customer findByAppointments(Appointment appointment);

    // Get customer by reminder - One customer is associated to many reminders
    Customer findByReminders(Reminder reminder);
    
    List<Customer> findAll();	
    
    void deleteByEmail(String email);

    Customer findCustomerByToken(String token);

}

