package ca.mcgill.ecse321.repairshop.model;

import javax.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "technician")
public class Technician extends User {

    
    
    private List<TimeSlot> timeslots;  // workHours

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "technician")
    public List<TimeSlot> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(List<TimeSlot> timeslots) {
        this.timeslots = timeslots;
    }
    

}