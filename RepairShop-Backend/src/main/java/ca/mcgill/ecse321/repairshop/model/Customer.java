package ca.mcgill.ecse321.repairshop.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Customer extends User {

    private Long customerID;

    @Id
    @GeneratedValue
    public Long getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Long customerID) {
        this.customerID = customerID;
    }

    ///////////////////////////////
    /*
    private RepairShop repairShop;

    public RepairShop getRepairShop() {
        return repairShop;
    }

    public void setRepairShop(RepairShop repairShop) {
        this.repairShop = repairShop;
    }
	*/
    ////////////////////////////////

    private List<Appointment> appointments;

    @OneToMany(targetEntity=Appointment.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "customer")
    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    ////////////////////////////////

    private List<Reminder> reminders;

    @OneToMany(targetEntity=Reminder.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "customer")
    public List<Reminder> getReminders() {
        return reminders;
    }

    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
    }

    ////////////////////////////////

    public String toString() {
        return super.toString() + "[" +
                "customerID" + ":" + getCustomerID() + "]" + System.getProperties().getProperty("line.separator");
    }


}