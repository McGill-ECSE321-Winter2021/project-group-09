package ca.mcgill.ecse321.repairshop.dto;

import com.sun.istack.NotNull;

import java.sql.Timestamp;

public class TimeSlotDto {
    @NotNull
    private Timestamp startDateTime;
    @NotNull
    private Timestamp endDateTime;
    private AppointmentDto appointment;
    private TechnicianDto technician;

    public Timestamp getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Timestamp startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Timestamp getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Timestamp endDateTime) {
        this.endDateTime = endDateTime;
    }

    public AppointmentDto getAppointment() {
        return appointment;
    }

    public void setAppointment(AppointmentDto appointment) {
        this.appointment = appointment;
    }

    public TechnicianDto getTechnician() {
        return technician;
    }

    public void setTechnician(TechnicianDto technician) {
        this.technician = technician;
    }
}
