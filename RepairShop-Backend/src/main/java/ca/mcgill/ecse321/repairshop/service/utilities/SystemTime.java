package ca.mcgill.ecse321.repairshop.service.utilities;

import java.sql.Timestamp;
import java.util.Calendar;

public class SystemTime {

    /**
     * Gets the current system date without the time value.
     *
     * @return current system date in the format yyyy-MM-DD (String)
     */
    public static String systemDate() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.toString().substring(0, 10);
    }

    /**
     * Adds or subtracts a number of days to a timestamp.
     *
     * @param initialTimeStamp initial timestamp with date & time (Timestamp)
     * @param nbDays number of hours we want to add or subtract to the initial timestanp (int)
     * @return final time stamp with the number of days added or subtracted (Timestamp)
     */
    public static Timestamp addOrSubtractDays(Timestamp initialTimeStamp, int nbDays) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(initialTimeStamp);
        cal.add(Calendar.DATE, nbDays);
        return new Timestamp(cal.getTime().getTime());
    }


}
