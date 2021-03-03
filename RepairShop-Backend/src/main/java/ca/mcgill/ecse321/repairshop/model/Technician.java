package ca.mcgill.ecse321.repairshop.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Technician extends User {

    @Id
    public String getEmail() {
        return super.getEmail();
    }
    public void setEmail(String email) {
        super.setEmail(email);
    }

    ///////////////////////////////////////////////////////////////////////////

    
    private List<TimeSlot> timeslots;  // workHours

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<TimeSlot> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(List<TimeSlot> timeslots) {
        this.timeslots = timeslots;
    }

    ///////////////////////////////////////////////////////////////////////////

    private List<Appointment> appointments;

    @OneToMany(targetEntity = Appointment.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "technician")
    public List<Appointment> getAppointments() {
        return appointments;
    }
    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

}