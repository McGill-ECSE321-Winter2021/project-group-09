package ca.mcgill.ecse321.repairshop.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.repairshop.model.Appointment;
import ca.mcgill.ecse321.repairshop.model.Customer;
import ca.mcgill.ecse321.repairshop.model.Service;
import ca.mcgill.ecse321.repairshop.model.TimeSlot;




public interface AppointmentRepository extends CrudRepository<Appointment, Long>{
	
	
	/**
	 * @author Shaswata
	 * @param customer
	 * @return List of appointments the customer has booked 
	 *  
	 */
	List<Appointment> findByCustomer(Customer customer);

	
	/**
	 * @author Shaswata
	 * @param customer
	 * @param service
	 * @return returns true if there is at least one appointment booked by the customer for a certain service
	 */
	boolean existsByCustomerAndService(Customer customer, Service service);

	/**
	 * @author Shaswata
	 * @param customer
	 * @param service
	 * @return a list of appointments booked by a customer for a certain service
	 * The customer-service pair is not unique (customer can book multiple appointments with same service) so it returns a list of appointments
	 */
	List<Appointment> findByCustomerAndService(Customer customer, Service service);
	
	
	/**
	 * @author Shaswata
	 * @param timeSlot
	 * @return the appointment booked at the timeslot
	 * A timeSlot has only 1 appointment
	 */
	Appointment findByTimeSlot(TimeSlot timeSlot);
	
	
	
	Optional<Appointment> findById(Long appointmentID);
	
	List<Appointment> findAll();	
	
	void deleteById(Long appointmentID);
	
	void deleteAll();
	
}
