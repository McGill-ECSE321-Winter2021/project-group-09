package ca.mcgill.ecse321.repairshop.model;

import javax.persistence.Entity;

@Entity
public class TimeSlot {

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //TimeSlot Attributes
    private Long timeSlotID;
    private String startDateTime;
    private String endDateTime;

    //TimeSlot Associations
    private Appointment appointment;
    private Technician technician;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public TimeSlot(Long aTimeSlotID, String aStartDateTime, String aEndDateTime, Technician aTechnician) {
        timeSlotID = aTimeSlotID;
        startDateTime = aStartDateTime;
        endDateTime = aEndDateTime;
        boolean didAddTechnician = setTechnician(aTechnician);
        if (!didAddTechnician) {
            throw new RuntimeException("Unable to create timeslot due to technician. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
        }
    }

    //------------------------
    // INTERFACE
    //------------------------

    public boolean setTimeSlotID(Long aTimeSlotID) {
        boolean wasSet = false;
        timeSlotID = aTimeSlotID;
        wasSet = true;
        return wasSet;
    }

    public boolean setStartDateTime(String aStartDateTime) {
        boolean wasSet = false;
        startDateTime = aStartDateTime;
        wasSet = true;
        return wasSet;
    }

    public boolean setEndDateTime(String aEndDateTime) {
        boolean wasSet = false;
        endDateTime = aEndDateTime;
        wasSet = true;
        return wasSet;
    }

    public Long getTimeSlotID() {
        return timeSlotID;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    /* Code from template association_GetOne */
    public Appointment getAppointment() {
        return appointment;
    }

    public boolean hasAppointment() {
        boolean has = appointment != null;
        return has;
    }

    /* Code from template association_GetOne */
    public Technician getTechnician() {
        return technician;
    }

    /* Code from template association_SetOptionalOneToMany */
    public boolean setAppointment(Appointment aAppointment) {
        boolean wasSet = false;
        Appointment existingAppointment = appointment;
        appointment = aAppointment;
        if (existingAppointment != null && !existingAppointment.equals(aAppointment)) {
            existingAppointment.removeAppointment(this);
        }
        if (aAppointment != null) {
            aAppointment.addAppointment(this);
        }
        wasSet = true;
        return wasSet;
    }

    /* Code from template association_SetOneToMany */
    public boolean setTechnician(Technician aTechnician) {
        boolean wasSet = false;
        if (aTechnician == null) {
            return wasSet;
        }

        Technician existingTechnician = technician;
        technician = aTechnician;
        if (existingTechnician != null && !existingTechnician.equals(aTechnician)) {
            existingTechnician.removeTimeslot(this);
        }
        technician.addTimeslot(this);
        wasSet = true;
        return wasSet;
    }

    public void delete() {
        if (appointment != null) {
            Appointment placeholderAppointment = appointment;
            this.appointment = null;
            placeholderAppointment.removeAppointment(this);
        }
        Technician placeholderTechnician = technician;
        this.technician = null;
        if (placeholderTechnician != null) {
            placeholderTechnician.removeTimeslot(this);
        }
    }


    public String toString() {
        return super.toString() + "[" +
                "timeSlotID" + ":" + getTimeSlotID() + "," +
                "startDateTime" + ":" + getStartDateTime() + "," +
                "endDateTime" + ":" + getEndDateTime() + "]" + System.getProperties().getProperty("line.separator") +
                "  " + "appointment = " + (getAppointment() != null ? Integer.toHexString(System.identityHashCode(getAppointment())) : "null") + System.getProperties().getProperty("line.separator") +
                "  " + "technician = " + (getTechnician() != null ? Integer.toHexString(System.identityHashCode(getTechnician())) : "null");
    }
}