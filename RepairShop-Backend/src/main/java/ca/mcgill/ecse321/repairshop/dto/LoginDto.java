package ca.mcgill.ecse321.repairshop.dto;

import ca.mcgill.ecse321.repairshop.service.utilities.UserType;
import com.sun.istack.NotNull;

public class LoginDto {

    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private UserType userType;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String toString() {
        return super.toString() + "[" +
                "email" + ":" + getEmail() + "," +
                "password" + ":" + getPassword() + "," +
                "usertype" + ":" + getUserType() + "]";
    }

}
