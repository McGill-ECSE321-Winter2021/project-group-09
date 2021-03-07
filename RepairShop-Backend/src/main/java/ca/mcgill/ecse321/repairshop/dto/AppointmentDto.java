package ca.mcgill.ecse321.repairshop.dto;

import java.util.List;

import com.sun.istack.NotNull;


public class AppointmentDto {

private Long appointmentID;
	@NotNull
    private ServiceDto service;
	@NotNull
    private CustomerDto customer;
	@NotNull
    private TechnicianDto technician;
	@NotNull
    private List<TimeSlotDto> timeSlots;
    
	
	public ServiceDto getService() {
		return service;
	}

	public void setService(ServiceDto service) {
		this.service = service;
	}
	
	public CustomerDto getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerDto customer) {
		this.customer = customer;
	}
	
	public TechnicianDto getTechnician() {
		return technician;
	}

	public void setTechnician(TechnicianDto technician) {
		this.technician = technician;
	}
	
    public List<TimeSlotDto> getTimeSlots() {
		return timeSlots;
	}

	public void setTimeSlots(List<TimeSlotDto> timeslots) {
		this.timeSlots = timeslots;
	}
    
}
