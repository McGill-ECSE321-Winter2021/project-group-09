package ca.mcgill.ecse321.repairshop.model;

import javax.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
public class Technician extends User implements Serializable{

    private List<TimeSlot> timeslots;

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "technician")
    public List<TimeSlot> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(List<TimeSlot> timeslots) {
        this.timeslots = timeslots;
    }

}