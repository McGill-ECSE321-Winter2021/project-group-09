package ca.mcgill.ecse321.repairshop.dto;

import ca.mcgill.ecse321.repairshop.model.ReminderType;
import com.sun.istack.NotNull;

import java.sql.Timestamp;

public class ReminderDto {

    private Long reminderID;
    @NotNull
    private Timestamp dateTime;
    @NotNull
    private ReminderType reminderType;
    @NotNull
    private CustomerDto customerDto;
    @NotNull
    private String serviceName;
    @NotNull
    private Timestamp appointmentDateTime;


    public Long getReminderID() {
        return reminderID;
    }
    public void setReminderID(Long reminderID) {
        this.reminderID = reminderID;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }
    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public ReminderType getReminderType() {
        return reminderType;
    }
    public void setReminderType(ReminderType reminderType) {
        this.reminderType = reminderType;
    }

    public CustomerDto getCustomerDto() {
        return customerDto;
    }
    public void setCustomerDto(CustomerDto customerDto) {
        this.customerDto = customerDto;
    }

    public String getServiceName() {
        return serviceName;
    }
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Timestamp getAppointmentDateTime() {
        return this.appointmentDateTime;
    }
    public void setAppointmentDateTime(Timestamp appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

	public String toString()
	{
		return super.toString() + "["+
				"reminderID" + ":" + getReminderID()+ "," +
				"serviceName" + ":" + getServiceName()+ "]" + System.getProperties().getProperty("line.separator") +
				"  " + "dateTime" + "=" + (getDateTime() != null ? !getDateTime().equals(this)  ? getDateTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
				"  " + "appointmentDateTime" + "=" + (getAppointmentDateTime() != null ? !getAppointmentDateTime().equals(this)  ? getAppointmentDateTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
				"  " + "reminderType" + "=" + (getReminderType() != null ? !getReminderType().equals(this)  ? getReminderType().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
				"  " + "customer = "+(getCustomerDto()!=null?Integer.toHexString(System.identityHashCode(getCustomerDto())):"null") + System.getProperties().getProperty("line.separator") ;
	}
}
