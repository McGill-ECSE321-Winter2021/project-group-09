package ca.mcgill.ecse321.repairshop.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Admin extends User {

    @Id
    public String getEmail() {
        return super.getEmail();
    }
    public void setEmail(String email) {
        super.setEmail(email);
    }
 
}
