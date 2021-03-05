package ca.mcgill.ecse321.repairshop.service.utilities;

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

}
