package ca.mcgill.ecse321.repairshop.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class TimeSlot {

    private Long timeSlotID;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getTimeSlotID() {
        return timeSlotID;
    }
    public void setTimeSlotID(Long timeSlotID) {
        this.timeSlotID = timeSlotID;
    }
    
    ///////////////////////////////////////////////////////////////////////////

    private Timestamp startDateTime;
    public Timestamp getStartDateTime() {
        return startDateTime;
    }
    public void setStartDateTime(Timestamp startDateTime) {
        this.startDateTime = startDateTime;
    }
    
    ///////////////////////////////////////////////////////////////////////////

    private Timestamp endDateTime;
    public Timestamp getEndDateTime() {
        return endDateTime;
    }
    public void setEndDateTime(Timestamp endDateTime) {
        this.endDateTime = endDateTime;
    }

    ///////////////////////////////////////////////////////////////////////////

    private Appointment appointment;
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    public Appointment getAppointment() {
        return appointment;
    }
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
    
    ///////////////////////////////////////////////////////////////////////////
    
    private Technician technician;
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    public Technician getTechnician() {
        return technician;
    }
    public void setTechnician(Technician technician) {
        this.technician = technician;
    }

    ///////////////////////////////////////////////////////////////////////////
    
    public String toString() {
        return super.toString() + "[" +
                "timeSlotID" + ":" + getTimeSlotID() + "," +
                "startDateTime" + ":" + getStartDateTime() + "," +
                "endDateTime" + ":" + getEndDateTime() + "]" + System.getProperties().getProperty("line.separator") +
                "  " + "appointment = " + (getAppointment() != null ? Integer.toHexString(System.identityHashCode(getAppointment())) : "null") + System.getProperties().getProperty("line.separator") +
                "  " + "technician = " + (getTechnician() != null ? Integer.toHexString(System.identityHashCode(getTechnician())) : "null");
    }

}


