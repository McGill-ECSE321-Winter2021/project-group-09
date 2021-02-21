package ca.mcgill.ecse321.repairshop.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class RepairShop {

    private Long repairShopID;

    @Id
    @GeneratedValue
    public Long getRepairShopID() {
        return repairShopID;
    }

    public void setRepairShopID(Long aRepairShopID) {
        repairShopID = aRepairShopID;
    }
    
    
    ///////////////////////////////////////////////////////////////////////////

    
    private TimeSlot timeslot;

    @OneToOne(cascade = CascadeType.ALL)
    public TimeSlot getTimeSlot() {
        return timeslot;
    }

    public void setTimeSlot(TimeSlot aNewTimeSlot) {
        timeslot = aNewTimeSlot;
    }

    
    ///////////////////////////////////////////////////////////////////////////
    
    
    private Appointment appointment;

    @OneToOne(cascade = CascadeType.ALL)
    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment aNewAppointment) {
        appointment = aNewAppointment;
    }

    
    ///////////////////////////////////////////////////////////////////////////

    
    private Service service;

    @OneToOne(cascade = CascadeType.ALL)
    public Service getService() {
        return service;
    }

    public void setService(Service aNewService) {
        service = aNewService;
    }
    
    
    ///////////////////////////////////////////////////////////////////////////

    
    private Reminder reminder;

    @OneToOne(cascade = CascadeType.ALL)
    public Reminder getReminder() {
        return reminder;
    }

    public void setReminder(Reminder aNewReminder) {
        reminder = aNewReminder;
    }

    
    ///////////////////////////////////////////////////////////////////////////
    

    private Business business;

    @OneToOne(cascade = CascadeType.ALL)
    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business aNewBusiness) {
        business = aNewBusiness;
    }

    
    ///////////////////////////////////////////////////////////////////////////

    
    private List<Customer> customers;

    @OneToMany(cascade = CascadeType.ALL)
    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> newCustomers) {
        customers = newCustomers;
    }

    
    ///////////////////////////////////////////////////////////////////////////

    
    private List<Technician> technicians;

    @OneToMany(cascade = CascadeType.ALL)
    public List<Technician> getTechnicians() {
        return technicians;
    }

    public void setTechnicians(List<Technician> technicians) {
        this.technicians = technicians;
    }

    
    ///////////////////////////////////////////////////////////////////////////

    
    private List<Admin> admin;

    @OneToMany(cascade = CascadeType.ALL)
    public List<Admin> getAdmin() {
        return admin;
    }

    public void setAdmin(List<Admin> admin) {
        this.admin = admin;
    }

    
    ///////////////////////////////////////////////////////////////////////////

    
    public String toString() {
        return super.toString() + "[" +
                "repairShopID" + ":" + getRepairShopID() + "]" + System.getProperties().getProperty("line.separator") +
                "  " + "business = " + (getBusiness() != null ? Integer.toHexString(System.identityHashCode(getBusiness())) : "null");
    }
}


