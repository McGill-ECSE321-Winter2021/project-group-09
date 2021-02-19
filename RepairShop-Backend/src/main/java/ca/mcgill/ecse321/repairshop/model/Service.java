package ca.mcgill.ecse321.repairshop.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Service {

    private Long serviceID;

    @Id
    @GeneratedValue
    public Long getServiceID() {
        return serviceID;
    }
    public void setServiceID(Long serviceID) {
        this.serviceID = serviceID;
    }

    ///////////////////////////////////////////////////////////////////////////

    private int duration;
    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }

    ///////////////////////////////////////////////////////////////////////////

    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    ///////////////////////////////////////////////////////////////////////////

    private double price;
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    ///////////////////////////////////////////////////////////////////////////

    public String toString() {
        return super.toString() + "[" +
                "serviceID" + ":" + getServiceID() + "," +
                "duration" + ":" + getDuration() + "," +
                "name" + ":" + getName() + "," +
                "price" + ":" + getPrice() + "]";
    }
}