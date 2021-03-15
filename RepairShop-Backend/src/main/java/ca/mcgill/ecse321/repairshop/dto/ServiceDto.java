package ca.mcgill.ecse321.repairshop.dto;

import com.sun.istack.NotNull;

public class ServiceDto {

    @NotNull
    private String name;
    @NotNull
    private int duration;
    @NotNull
    private double price;


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    
    public String toString() {
        return super.toString() + "[" +
                "name" + ":" + getName() + "," +
                "duration" + ":" + getDuration() + "," +
                "price" + ":" + getPrice() + "]";
    }

}
