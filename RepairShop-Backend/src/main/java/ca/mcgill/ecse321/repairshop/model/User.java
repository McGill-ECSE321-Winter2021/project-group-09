package ca.mcgill.ecse321.repairshop.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class User {

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    ////////////////////////////////

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    ////////////////////////////////

    private String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    ////////////////////////////////

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    ////////////////////////////////

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    ////////////////////////////////

    public String toString() {
        return super.toString() + "[" +
                "email" + ":" + getEmail() + "," +
                "password" + ":" + getPassword() + "," +
                "phoneNumber" + ":" + getPhoneNumber() + "," +
                "name" + ":" + getName() + "," +
                "address" + ":" + getAddress() + "]";
    }


}