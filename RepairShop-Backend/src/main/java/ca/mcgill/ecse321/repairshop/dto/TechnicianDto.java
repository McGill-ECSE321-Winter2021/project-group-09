package ca.mcgill.ecse321.repairshop.dto;

import java.util.List;

import com.sun.istack.NotNull;

public class TechnicianDto {
	
	@NotNull
 	private String email;
	
	private String phoneNumber;
	private String name;
	private String address;
	private List<TimeSlotDto> timeSlots;
    
    
	
	public String getEmail() {
        return email;
    }
	
    public void setEmail(String email) {
        this.email = email;
    }
    
 
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    
    public List<TimeSlotDto> getTimeSlots() {
		return timeSlots;
	}

	public void setTimeSlots(List<TimeSlotDto> timeslots) {
		this.timeSlots = timeslots;
	}
    

}
