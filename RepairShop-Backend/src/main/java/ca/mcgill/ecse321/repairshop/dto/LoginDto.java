package ca.mcgill.ecse321.repairshop.dto;

import com.sun.istack.NotNull;

public class LoginDto {
    
    @NotNull
    private String email;
    @NotNull
    private String password;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setSetPassword(String password) {
        this.password = password;
    }

}
