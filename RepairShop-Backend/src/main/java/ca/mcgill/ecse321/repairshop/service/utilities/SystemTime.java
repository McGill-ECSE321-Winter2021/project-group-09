package ca.mcgill.ecse321.repairshop.service.utilities;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;

public class SystemTime {

    // When testing, output specified time instead of real system time
    // Change these with setters
    private static boolean isTest = false;
    private static Timestamp testTime = Timestamp.valueOf(LocalDateTime.now());

    /**
     * Return boolean value to indicate whether it is a test
     * @return isTest (boolean)
     */
    public static boolean isTest() {
        return isTest;
    }

    /**
     * Sets the value of isTest.
     * @param test true or false (whether it is a test)
     */
    public static void setTest(boolean test) {
        isTest = test;
    }

    /**
     * Sets the testing time
     * @param testTime (Timestamp)
     */
    public static void setTestTime(Timestamp testTime) {
        SystemTime.testTime = testTime;
    }

    /** Method to get the current system time
     * @return either the current time, or the test time
     */
    public static Timestamp getCurrentDateTime() {
        return isTest ? testTime : Timestamp.valueOf(LocalDateTime.now());
    }

    /**
     * Gets the current system date without the time value.
     *
     * @return current system date in the format yyyy-MM-DD (String)
     */
    public static String getSystemDate() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return isTest ? testTime.toString() : timestamp.toString().substring(0, 10);
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
