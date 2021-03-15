package ca.mcgill.ecse321.repairshop.dto;

import java.util.List;

import com.sun.istack.NotNull;

public class TechnicianDto {
	
	@NotNull
 	private String email;
	
	private String phoneNumber;
	private String name;
	private String address;
	private String password;
	private List<TimeSlotDto> workHours;
	private String token;
    
    
	
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
		return workHours;
	}

	public void setTimeSlots(List<TimeSlotDto> timeslots) {
		this.workHours = timeslots;
	}
	
	public String getPassword() {
	    return password;
	}
	    
	public void setSetPassword(String password) {
	    this.password = password;
	}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
