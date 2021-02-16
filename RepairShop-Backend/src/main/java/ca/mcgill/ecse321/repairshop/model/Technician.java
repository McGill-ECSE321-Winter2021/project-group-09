package ca.mcgill.ecse321.repairshop.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Technician extends User {

    private Long technicianID;

    @Id
    @GeneratedValue
    public Long getTechnicianID() {
        return technicianID;
    }

    public void setTechnicianID(Long technicianID) {
        this.technicianID = technicianID;
    }

    ////////////////////////////////

    private List<TimeSlot> timeslots;
    @OneToMany(targetEntity=TimeSlot.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "technician")
    public List<TimeSlot> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(List<TimeSlot> timeslots) {
        this.timeslots = timeslots;
    }

    ////////////////////////////////

    private RepairShop repairShop;

    public RepairShop getRepairShop() {
        return repairShop;
    }

    public void setRepairShop(RepairShop repairShop) {
        this.repairShop = repairShop;
    }

    ////////////////////////////////

    public String toString() {
        return super.toString() + "[" +
                "technicianID" + ":" + getTechnicianID() + "]" + System.getProperties().getProperty("line.separator") +
                "  " + "repairShop = " + (getRepairShop() != null ? Integer.toHexString(System.identityHashCode(getRepairShop())) : "null");
    }





}