package ca.mcgill.ecse321.repairshop.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Service {

    private String name;
    @Id
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
                "name" + ":" + getName() + "," +
                "duration" + ":" + getDuration() + "," +
                "price" + ":" + getPrice() + "]";
    }
}