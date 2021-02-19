package ca.mcgill.ecse321.repairshop.dao;

import ca.mcgill.ecse321.repairshop.model.Appointment;
import ca.mcgill.ecse321.repairshop.model.Reminder;
import ca.mcgill.ecse321.repairshop.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRepository<Customer extends User> extends CrudRepository<Customer, Long> {

    Customer findCustomerByCustomerId(Long customerId);

    // Get customer by appointment - One customer is associated to many appointments
    Customer findByAppointment(Appointment appointment);

    // Get customer by reminder - One customer is associated to many reminders
    Customer findByReminder(Reminder reminder);

}