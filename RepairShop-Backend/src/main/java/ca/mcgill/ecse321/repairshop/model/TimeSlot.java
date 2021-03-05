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
    
    public String toString() {
        return super.toString() + "[" +
                "timeSlotID" + ":" + getTimeSlotID() + "," +
                "startDateTime" + ":" + getStartDateTime() + "," +
                "endDateTime" + ":" + getEndDateTime() + "]" + System.getProperties().getProperty("line.separator");
    }

}


