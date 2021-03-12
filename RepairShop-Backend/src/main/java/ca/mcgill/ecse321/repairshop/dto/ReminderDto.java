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
        return appointmentDateTime;
    }
    public void setAppointmentDateTime(Timestamp appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }


}
