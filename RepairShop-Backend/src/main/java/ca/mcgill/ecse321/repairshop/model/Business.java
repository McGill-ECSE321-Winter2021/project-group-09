package ca.mcgill.ecse321.repairshop.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Business {

    private Long businessID;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getBusinessID() {
        return businessID;
    }
    public void setBusinessID(Long businessID) {
        this.businessID = businessID;
    }

    ///////////////////////////////////////////////////////////////////////////

    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    ///////////////////////////////////////////////////////////////////////////

    private String address;
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    ///////////////////////////////////////////////////////////////////////////

    private String phoneNumber;
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    ///////////////////////////////////////////////////////////////////////////

    private String email;
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    
    ///////////////////////////////////////////////////////////////////////////

    private int numberOfRepairSpots;
    public int getNumberOfRepairSpots() {
        return numberOfRepairSpots;
    }
    public void setNumberOfRepairSpots(int numberOfRepairSpots) {
        this.numberOfRepairSpots = numberOfRepairSpots;
    }

    ///////////////////////////////////////////////////////////////////////////

    private List<TimeSlot> holidays;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<TimeSlot> getHolidays() {
        return holidays;
    }
    public void setHolidays(List<TimeSlot> holidays) {
        this.holidays = holidays;
    }

    ///////////////////////////////////////////////////////////////////////////

}
