package ca.mcgill.ecse321.repairshop;

import android.app.Application;

public class State extends Application {
    public String token;
    public String email;
    public String password;
    public static boolean loggedIn = false;
}
