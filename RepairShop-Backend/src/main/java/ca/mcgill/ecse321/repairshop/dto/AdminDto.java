package ca.mcgill.ecse321.repairshop.dto;

import com.sun.istack.NotNull;

public class AdminDto {

	@NotNull
 	private String email;
	@NotNull
	private String phoneNumber;
	@NotNull
	private String name;
	private String address;
	@NotNull
	private String password;

    public String token;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
