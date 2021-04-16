package ca.mcgill.ecse321.repairshop.Utils;

public class HelperMethods {


    /**
     * Helper to display service duration in hours
     * @param duration number of 30 minutes slots (int)
     * @return duration in hours (String)
     */
    public static String displayDuration(int duration) {
        if (duration == 2) return "1 hour";
        else return (duration / 2.0) + " hours";
    }


    /**
     * Helper to convert a timestamp format (2021-03-02T15:00:00.000+00:00) to format 2021-03-02 15:00
     * @param dateTime (String)
     * @return String in format 2021-03-02 at 15:00
     */
    public static String displayDateTime(String dateTime) {
        return dateTime.substring(0,10) + " at " + dateTime.substring(11, 16);
    }

    /**
     *  Converts two timestamps of format (2021-03-02T15:00:00.000+00:00) to format "2021-03-02 from 15:00 to 16:00"
     *
     * @param startDateTime format: 2021-03-02T15:00:00.000+00:00 (String)
     * @param endDateTime format: 2021-03-02T16:00:00.000+00:00 (String)
     * @return "start date" from "start time" to "end time" (String)
     */
    public static String displayDateTimeFromTo(String startDateTime, String endDateTime) {
        return startDateTime.substring(0, 10) + " from " + startDateTime.substring(11, 16) + " to " + endDateTime.substring(11, 16);
    }
}
