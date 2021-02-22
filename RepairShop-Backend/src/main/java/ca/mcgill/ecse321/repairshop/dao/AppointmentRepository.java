package ca.mcgill.ecse321.repairshop.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.repairshop.model.Appointment;
import ca.mcgill.ecse321.repairshop.model.Customer;
import ca.mcgill.ecse321.repairshop.model.Service;
import ca.mcgill.ecse321.repairshop.model.TimeSlot;

public interface AppointmentRepository extends CrudRepository<Appointment, Long>{

	//List of appointments the customer has booked
	List<Appointment> findByCustomer(Customer customer);

	//true if there is at least one appointment booked by the customer for a certain service
	boolean existsByCustomerAndService(Customer customer, Service service);

	 //The customer-service pair is not unique so it returns a list of appointments
	 //(customer can book multiple appointments with same service)
	List<Appointment> findByCustomerAndService(Customer customer, Service service);

	//A timeSlot has only 1 appointment
	Appointment findByTimeSlots(TimeSlot timeSlot);

	Appointment findAppointmentByAppointmentID(Long appointmentID);
	
	List<Appointment> findAll();	
	
	void deleteById(Long appointmentID);
	
	void deleteAll();
	
}



