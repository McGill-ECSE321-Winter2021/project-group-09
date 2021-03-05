package ca.mcgill.ecse321.repairshop.dto;

import com.sun.istack.NotNull;

import java.sql.Timestamp;

public class TimeSlotDto {
    @NotNull
    private Timestamp startDateTime;
    @NotNull
    private Timestamp endDateTime;

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

}
