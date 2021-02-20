


package ca.mcgill.ecse321.repairshop.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


@Entity
public class Appointment {

    private int repairSpotNumber;
	
    private Long appointmentID;
    
    public void setAppointmentID(Long aAppointmentID) {
        this.appointmentID = aAppointmentID;
    }

    @Id
    @GeneratedValue
    public Long getAppointmentID() {
        return this.appointmentID;
    }

    //////////////////////////////////////////////////////////////////////////////////
    
    private Service service;
    
    @ManyToOne(optional = false)
    public Service getService() {
        return this.service;
    }
    
    public void setService(Service service) {
    	this.service = service;
    }

    ////////////////////////////////////////////////////////////////////////////////
    
    private Customer customer;
    
    @ManyToOne(optional = false)
    public Customer getCustomer() {
        return this.customer;
    }
    
    public void setCustomer(Customer customer) {
    	this.customer = customer;
    }
   
    ///////////////////////////////////////////////////////////////////////////////
    
    private List<TimeSlot> timeSlots;

    @OneToMany(mappedBy = "appointment")
    public List<TimeSlot> getTimeSlots() {
        return this.timeSlots;
    }

    public void setTimeSlots(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }

    //////////////////////////////////////////////////////////////////////////////
    
    public int getRepairSpotNumber() {
    	return this.repairSpotNumber;
    }
    
    public void setRepairSpotNumber(int n) {
    	this.repairSpotNumber = n;
    }

    //////////////////////////////////////////////////////////////////////////////

    public String toString() {
        return super.toString() + "[" +
                "appointmentID" + ":" + getAppointmentID() + "]" + System.getProperties().getProperty("line.separator") +
                "  " + "service = " + (getService() != null ? Integer.toHexString(System.identityHashCode(getService())) : "null") + System.getProperties().getProperty("line.separator") +
                "  " + "customer = " + (getCustomer() != null ? Integer.toHexString(System.identityHashCode(getCustomer())) : "null");
    }
}


