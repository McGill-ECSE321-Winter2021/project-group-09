package ca.mcgill.ecse321.repairshop.dto;

import java.util.List;

public class TimeSlotListDto {
    private List<TimeSlotDto> timeSlots;

    public List<TimeSlotDto> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<TimeSlotDto> timeSlots) {
        this.timeSlots = timeSlots;
    }
}
