package ca.mcgill.ecse321.repairshop.model;

import javax.persistence.*;


@Entity
public class Appointment {
	
	
    private Long appointmentID;
    
    @Id
    @GeneratedValue
    public Long getAppointmentID() {
        return this.appointmentID;
    }
    
    public void setAppointmentID(Long aAppointmentID) {
        this.appointmentID = aAppointmentID;
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

    ////////////////////////////////////////////////////////////////////////////////

    private Technician technician;

    @ManyToOne(optional = false)
    public Technician getTechnician() {
        return this.technician;
    }

    public void setTechnician(Technician technician) {
        this.technician = technician;
    }
   
    ///////////////////////////////////////////////////////////////////////////////
    
    private TimeSlot timeSlot;

    @OneToOne(fetch =  FetchType.LAZY)
    public TimeSlot getTimeSlot() {
        return this.timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    //////////////////////////////////////////////////////////////////////////////

}


