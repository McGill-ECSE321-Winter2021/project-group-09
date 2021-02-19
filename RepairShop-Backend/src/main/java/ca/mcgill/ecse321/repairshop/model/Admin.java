package ca.mcgill.ecse321.repairshop.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Admin extends User {

    private Long adminID;

    @Id
    @GeneratedValue
    public Long getAdminID() {
        return adminID;
    }

    public void setAdminID(Long adminID) {
        this.adminID = adminID;
    }

    ///////////////////////////////



    public String toString() {
        return super.toString() + "[" +
                "adminID" + ":" + getAdminID() + "]" + System.getProperties().getProperty("line.separator");
    }


}