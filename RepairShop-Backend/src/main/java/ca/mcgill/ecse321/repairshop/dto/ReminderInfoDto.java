package ca.mcgill.ecse321.repairshop.dto;

import com.sun.istack.NotNull;

import java.sql.Timestamp;

public class ReminderInfoDto {

    @NotNull
    private Timestamp dateTime;
    @NotNull
    private Timestamp appointmentDateTime;
    @NotNull
    private String serviceName;
    @NotNull
    private String type;
    @NotNull
    private String email;

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) { //ignore this warning
        this.dateTime = dateTime;
    }

    public Timestamp getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(Timestamp appointmentDateTime) { //ignore this warning
        this.appointmentDateTime = appointmentDateTime;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    } //ignore this warning

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
