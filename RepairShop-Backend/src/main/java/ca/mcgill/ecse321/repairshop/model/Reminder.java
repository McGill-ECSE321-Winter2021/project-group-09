package ca.mcgill.ecse321.repairshop.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.persistence.Id;
import java.sql.Timestamp;

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

	///////////////////////////////////////////////////////////////////////////

	private String serviceName;
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	///////////////////////////////////////////////////////////////////////////

    private Timestamp dateTime;
	public Timestamp getDateTime() {
		return dateTime;
	}
	public void setDateTime(Timestamp dateTime) {
		this.dateTime = dateTime;
	}
	
    ///////////////////////////////////////////////////////////////////////////

	private Timestamp appointmentDateTime;
	public Timestamp getAppointmentDateTime() {
		return appointmentDateTime;
	}
	public void setAppointmentDateTime(Timestamp appointmentDateTime) {
		this.appointmentDateTime = appointmentDateTime;
	}

	///////////////////////////////////////////////////////////////////////////
	private ReminderType reminderType;
	@Enumerated(EnumType.STRING)
	public ReminderType getReminderType() {
		return reminderType;
	}
	public void setReminderType(ReminderType reminderType) {
		this.reminderType = reminderType;
	}

    ///////////////////////////////////////////////////////////////////////////

    private Customer customer;
    @ManyToOne
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
    ///////////////////////////////////////////////////////////////////////////

	public String toString()
	{
		return super.toString() + "["+
				"reminderID" + ":" + getReminderID()+ "," +
				"serviceName" + ":" + getServiceName()+ "]" + System.getProperties().getProperty("line.separator") +
				"  " + "dateTime" + "=" + (getDateTime() != null ? !getDateTime().equals(this)  ? getDateTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
				"  " + "appointmentDateTime" + "=" + (getAppointmentDateTime() != null ? !getAppointmentDateTime().equals(this)  ? getAppointmentDateTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
				"  " + "reminderType" + "=" + (getReminderType() != null ? !getReminderType().equals(this)  ? getReminderType().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
				"  " + "customer = "+(getCustomer()!=null?Integer.toHexString(System.identityHashCode(getCustomer())):"null") + System.getProperties().getProperty("line.separator") ;
	}
}

