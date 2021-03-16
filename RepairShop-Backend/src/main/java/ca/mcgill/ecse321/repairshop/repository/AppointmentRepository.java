package ca.mcgill.ecse321.repairshop.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.repairshop.model.Appointment;
import ca.mcgill.ecse321.repairshop.model.Customer;
import ca.mcgill.ecse321.repairshop.model.Service;

public interface AppointmentRepository extends CrudRepository<Appointment, Long>{

	//List of appointments the customer has booked
	List<Appointment> findAppointmentByCustomer(Customer customer);

	List<Appointment> findByService(Service service);

	Appointment findAppointmentByAppointmentID(Long appointmentID);
	
	List<Appointment> findAll();	
	
	void deleteById(Long appointmentID);
	
	void deleteAll();
	
}



