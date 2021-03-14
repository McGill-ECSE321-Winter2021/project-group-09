package ca.mcgill.ecse321.repairshop.service.utilities;

import ca.mcgill.ecse321.repairshop.model.TimeSlot;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static java.util.regex.Pattern.matches;

public class ValidationHelperMethods {
    
    /**
     * Validate email using regex pattern
     *
     * @param email The email to validate.
     * @throws Exception Throws exception when email is invalid.
     * @author Tyler
     */
    public static void validateEmail(String email) throws Exception {
        if (!matches("[A-Za-z0-9._+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}", email)) throw new Exception("Invalid email");
    }

    /** Helper method to get a timeslot with the same days of the week at some offset (keeps the times constant)
     * ex: If the start time is on a Tuesday, the adjusted date will be the first Tuesday after the offset
     * @param timeSlot to adjust with another date
     * @param targetOffset a timestamp from which to offset the new timeslot
     * @return an adjusted timeslot
     */
    public static TimeSlot getUpdatedHours(TimeSlot timeSlot, Timestamp targetOffset) {
        // Working with LocalDateTime to make use of its methods
        LocalDateTime startTime = timeSlot.getStartDateTime().toLocalDateTime();
        LocalDateTime endTime = timeSlot.getEndDateTime().toLocalDateTime();
        LocalDate offset = targetOffset.toLocalDateTime().toLocalDate(); // only want to offset the date, not the time
        LocalDate newStartDate = offset.with(TemporalAdjusters.next(startTime.getDayOfWeek())); // Adjust the date to the new week with the same day of the week
        LocalDate newEndDate = offset.with(TemporalAdjusters.next(endTime.getDayOfWeek()));
        // convert to LocalDateTime to get the times back
        LocalDateTime newStartDateTime = newStartDate.atTime(startTime.getHour(), startTime.getMinute());
        LocalDateTime newEndDateTime = newEndDate.atTime(endTime.getHour(), endTime.getMinute());

        TimeSlot newTimeSlot = new TimeSlot();
        newTimeSlot.setStartDateTime(Timestamp.valueOf(newStartDateTime));
        newTimeSlot.setEndDateTime(Timestamp.valueOf(newEndDateTime));

        return newTimeSlot;
    }

    /** Helper method to get a timeslot with the same days of the week at some offset (keeps the times constant)
     * Uses an offset of the current day (or test day)
     * @param timeSlot to adjust to another day
     * @return an adjusted timeslot
     */
    public static TimeSlot getUpdatedHours(TimeSlot timeSlot) {
        return getUpdatedHours(timeSlot, SystemTime.getCurrentDateTime());
    }

}
