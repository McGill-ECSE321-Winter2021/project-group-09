package ca.mcgill.ecse321.repairshop;

import android.app.Application;

/**
 * Keeps track of the logged in user's token, email and login state
 */
public class State extends Application {
    public static String token = "";
    public static String email = "";
    public static boolean loggedIn = false;
}
