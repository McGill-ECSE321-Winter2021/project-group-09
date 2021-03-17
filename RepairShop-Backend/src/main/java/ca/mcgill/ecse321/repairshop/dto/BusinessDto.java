package ca.mcgill.ecse321.repairshop.dto;

import java.util.List;

public class BusinessDto {
    private String name;
    private String address;
    private String email;
    private String phoneNumber;
    private int numberOfRepairSpots;
    private List<TimeSlotDto> holidays;
    
    
    //default constructor is needed for the controller
    public BusinessDto() {
    	
    }

    
    public BusinessDto(Long businessID, String name, String address, String email, String phoneNumber, int numberOfRepairSpots) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.numberOfRepairSpots = numberOfRepairSpots;
    }
    


    public String getName() {
        return this.name;
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

    public int getNumberOfRepairSpots() {
        return numberOfRepairSpots;
    }

    public void setNumberOfRepairSpots(int numberOfRepairSpots) {
        this.numberOfRepairSpots = numberOfRepairSpots;
    }

    public List<TimeSlotDto> getHolidays() {
        return this.holidays;
    }

    public void setHolidays(List<TimeSlotDto> holidays) {
        this.holidays = holidays;
    }


}
