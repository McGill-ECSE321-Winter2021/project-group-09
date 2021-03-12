package ca.mcgill.ecse321.repairshop.dto;

import com.sun.istack.NotNull;

public class CustomerDto {
	
	@NotNull
 	private String email;
	
	private String phoneNumber;
	private String name;
	private String address;
	private String password;
	
	
	
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
    
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

}
