package ca.mcgill.ecse321.repairshop.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.persistence.Id;

@Entity
public class Reminder {

    private Long reminderID;
    
    @Id
    @GeneratedValue
    public Long getReminderID() {
		return reminderID;
	}

	public void setReminderID(Long reminderID) {
		this.reminderID = reminderID;
	}
    
	
    ///////////////////////////////

    private RepairShop repairShop;

    public RepairShop getRepairShop() {
        return repairShop;
    }

    public void setRepairShop(RepairShop repairShop) {
        this.repairShop = repairShop;
    }
    
    
	///////////////////////////////
    
    private String dateTime;

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	
	///////////////////////////////
	
    public enum ReminderType {OilChange, Confirmation, Maintenance, RegularCheckups}
    
    ///////////////////////////////
	
	private ReminderType reminderType;
	
	@Enumerated(EnumType.STRING)
	public ReminderType getReminderType() {
		return reminderType;
	}

	public void setReminderType(ReminderType reminderType) {
		this.reminderType = reminderType;
	}

	
    ///////////////////////////////
    
    private Customer customer;
    
    @ManyToOne(targetEntity = Customer.class, cascade = CascadeType.ALL)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
  
	
    ///////////////////////////////

    public String toString() {
        return super.toString() + "[" +
                "reminderID" + ":" + getReminderID() + "," +
                "dateTime" + ":" + getDateTime() + "," +
                "  " + "reminderType" + "=" + (getReminderType() != null ? !getReminderType().equals(this) ? getReminderType().toString().replaceAll("  ", "    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
                "  " + "customer = " + (getCustomer() != null ? Integer.toHexString(System.identityHashCode(getCustomer())) : "null");
    }

}