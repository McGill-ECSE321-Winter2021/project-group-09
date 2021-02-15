package ca.mcgill.ecse321.repairshop.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// line 17 "model.ump"
// line 92 "model.ump"
public class Technician extends User {

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //Technician Attributes
    private Long technicianID;

    //Technician Associations
    private final List<TimeSlot> timeslots;
    private RepairShop repairShop;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public Technician(String aEmail, String aPassword, String aPhoneNumber, String aName, String aAddress, Long aTechnicianID, RepairShop aRepairShop) {
        super(aEmail, aPassword, aPhoneNumber, aName, aAddress);
        technicianID = aTechnicianID;
        timeslots = new ArrayList<TimeSlot>();
        boolean didAddRepairShop = setRepairShop(aRepairShop);
        if (!didAddRepairShop) {
            throw new RuntimeException("Unable to create technician due to repairShop. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
        }
    }

    //------------------------
    // INTERFACE
    //------------------------

    /* Code from template association_MinimumNumberOfMethod */
    public static int minimumNumberOfTimeslots() {
        return 0;
    }

    public boolean setTechnicianID(Long aTechnicianID) {
        boolean wasSet = false;
        technicianID = aTechnicianID;
        wasSet = true;
        return wasSet;
    }

    public Long getTechnicianID() {
        return technicianID;
    }

    /* Code from template association_GetMany */
    public TimeSlot getTimeslot(int index) {
        TimeSlot aTimeslot = timeslots.get(index);
        return aTimeslot;
    }

    public List<TimeSlot> getTimeslots() {
        List<TimeSlot> newTimeslots = Collections.unmodifiableList(timeslots);
        return newTimeslots;
    }

    public int numberOfTimeslots() {
        int number = timeslots.size();
        return number;
    }

    public boolean hasTimeslots() {
        boolean has = timeslots.size() > 0;
        return has;
    }

    public int indexOfTimeslot(TimeSlot aTimeslot) {
        int index = timeslots.indexOf(aTimeslot);
        return index;
    }

    /* Code from template association_GetOne */
    public RepairShop getRepairShop() {
        return repairShop;
    }

    /* Code from template association_AddManyToOne */
    public TimeSlot addTimeslot(Long aTimeSlotID, String aStartDateTime, String aEndDateTime) {
        return new TimeSlot(aTimeSlotID, aStartDateTime, aEndDateTime, this);
    }

    public boolean addTimeslot(TimeSlot aTimeslot) {
        boolean wasAdded = false;
        if (timeslots.contains(aTimeslot)) {
            return false;
        }
        Technician existingTechnician = aTimeslot.getTechnician();
        boolean isNewTechnician = existingTechnician != null && !this.equals(existingTechnician);
        if (isNewTechnician) {
            aTimeslot.setTechnician(this);
        } else {
            timeslots.add(aTimeslot);
        }
        wasAdded = true;
        return wasAdded;
    }

    public boolean removeTimeslot(TimeSlot aTimeslot) {
        boolean wasRemoved = false;
        //Unable to remove aTimeslot, as it must always have a technician
        if (!this.equals(aTimeslot.getTechnician())) {
            timeslots.remove(aTimeslot);
            wasRemoved = true;
        }
        return wasRemoved;
    }

    /* Code from template association_AddIndexControlFunctions */
    public boolean addTimeslotAt(TimeSlot aTimeslot, int index) {
        boolean wasAdded = false;
        if (addTimeslot(aTimeslot)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfTimeslots()) {
                index = numberOfTimeslots() - 1;
            }
            timeslots.remove(aTimeslot);
            timeslots.add(index, aTimeslot);
            wasAdded = true;
        }
        return wasAdded;
    }

    public boolean addOrMoveTimeslotAt(TimeSlot aTimeslot, int index) {
        boolean wasAdded = false;
        if (timeslots.contains(aTimeslot)) {
            if (index < 0) {
                index = 0;
            }
            if (index > numberOfTimeslots()) {
                index = numberOfTimeslots() - 1;
            }
            timeslots.remove(aTimeslot);
            timeslots.add(index, aTimeslot);
            wasAdded = true;
        } else {
            wasAdded = addTimeslotAt(aTimeslot, index);
        }
        return wasAdded;
    }

    /* Code from template association_SetOneToMany */
    public boolean setRepairShop(RepairShop aRepairShop) {
        boolean wasSet = false;
        if (aRepairShop == null) {
            return wasSet;
        }

        RepairShop existingRepairShop = repairShop;
        repairShop = aRepairShop;
        if (existingRepairShop != null && !existingRepairShop.equals(aRepairShop)) {
            existingRepairShop.removeTechnician(this);
        }
        repairShop.addTechnician(this);
        wasSet = true;
        return wasSet;
    }

    public void delete() {
        for (int i = timeslots.size(); i > 0; i--) {
            TimeSlot aTimeslot = timeslots.get(i - 1);
            aTimeslot.delete();
        }
        RepairShop placeholderRepairShop = repairShop;
        this.repairShop = null;
        if (placeholderRepairShop != null) {
            placeholderRepairShop.removeTechnician(this);
        }
        super.delete();
    }


    public String toString() {
        return super.toString() + "[" +
                "technicianID" + ":" + getTechnicianID() + "]" + System.getProperties().getProperty("line.separator") +
                "  " + "repairShop = " + (getRepairShop() != null ? Integer.toHexString(System.identityHashCode(getRepairShop())) : "null");
    }
}