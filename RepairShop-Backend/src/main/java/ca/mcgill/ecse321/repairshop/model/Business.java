package ca.mcgill.ecse321.repairshop.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import java.util.List;
//IMPORTS for PERSISTENCE

@Entity
public class Business {

    /***************** ID ****************/

    private Long businessID;

    @Id
    @GeneratedValue
    public Long getBusinessID() {
        return businessID;
    }

    public void setBusinessID(Long businessID) {
        this.businessID = businessID;
    }

    ///////////////////////////////////////////////////////////////

    /**************** Name ****************/

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    ///////////////////////////////////////////////////////////////

    /**************** Address *****************/

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    ///////////////////////////////////////////////////////////////

    /*************** Phone Number ****************/


    private String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    ///////////////////////////////////////////////////////////////

    /**************** Email ****************/
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    ///////////////////////////////////////////////////////////////

    /************** Number Of Repair Spots ****************/

    private int numberOfRepairSpots;

    public int getNumberOfRepairSpots() {
        return numberOfRepairSpots;
    }

    public void setNumberOfRepairSpots(int numberOfRepairSpots) {
        this.numberOfRepairSpots = numberOfRepairSpots;
    }

    ///////////////////////////////////////////////////////////////

/** BUSINESS ASSOCIATIONS **/

    /************** TIMESLOT Vacations ****************/
    // Business 0..1-->* TimeSlots (vacations)
    //CacadeTupe.ALL: If delete Business, we want to delete ALL related TimeSlot vacations

    private List<TimeSlot> vacations;

    @OneToMany(cascade = CascadeType.ALL)
    public List<TimeSlot> getVacations() {
        return vacations;
    }

    public void setVacations(List<TimeSlot> vacations) {
        this.vacations = vacations;
    }

    ////////////////////////////////
    /************** Repair Shop ****************/

    private RepairShop repairShop;

    public RepairShop getRepairShop() {
        return repairShop;
    }

    public void setRepairShop(RepairShop repairShop) {
        this.repairShop = repairShop;
    }

    ////////////////////////////////

    public String toString() {
        return super.toString() + "[" +
                "businessID" + ":" + getBusinessID() + "," +
                "name" + ":" + getName() + "," +
                "address" + ":" + getAddress() + "," +
                "phoneNumber" + ":" + getPhoneNumber() + "," +
                "email" + ":" + getEmail() + "," +
                "numberOfRepairSpots" + ":" + getNumberOfRepairSpots() + "]" + System.getProperties().getProperty("line.separator") +
                "  " + "repairShop = " + (getRepairShop() != null ? Integer.toHexString(System.identityHashCode(getRepairShop())) : "null");
    }
}
