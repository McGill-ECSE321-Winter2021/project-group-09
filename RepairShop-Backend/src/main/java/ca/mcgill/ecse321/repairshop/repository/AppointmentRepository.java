package ca.mcgill.ecse321.repairshop.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.repairshop.model.Appointment;
import ca.mcgill.ecse321.repairshop.model.Customer;
import ca.mcgill.ecse321.repairshop.model.Service;

public interface AppointmentRepository extends CrudRepository<Appointment, Long>{

	/**
	 * Find all the appointments a customer has booked
	 * @param customer
	 * @return list of customer's appointments (List<Appointment>)
	 */
	List<Appointment> findAppointmentByCustomer(Customer customer);

	/**
	 * Find all the appointments booked for a given service
	 * @param service
	 * @return List of appointments (List<Appointment>)
	 */
	List<Appointment> findByService(Service service);

	/**
	 * Find appointment by ID
	 * @param appointmentID
	 * @return appointment with matching ID (Appointment)
	 */
	Appointment findAppointmentByAppointmentID(Long appointmentID);
	
	/**
	 * @return all the appointments booked
	 */
	List<Appointment> findAll();	
	
	/**
	 * Delete an appointment by ID
	 * @param appointmentID
	 */
	void deleteById(Long appointmentID);
	
	/**
	 * Delete all appointments
	 */
	void deleteAll();
	
}



