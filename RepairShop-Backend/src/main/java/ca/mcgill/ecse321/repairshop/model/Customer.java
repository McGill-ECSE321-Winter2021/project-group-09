package ca.mcgill.ecse321.repairshop.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Customer extends User {

    @Id
    public String getEmail() {
        return super.getEmail();
    }
    public void setEmail(String email) {
        super.setEmail(email);
    }

    ///////////////////////////////////////////////////////////////////////////

	private List<Appointment> appointments;

    @OneToMany(targetEntity = Appointment.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "customer")
    public List<Appointment> getAppointments() {
        return appointments;
    }
    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    ///////////////////////////////////////////////////////////////////////////

    private List<Reminder> reminders;

    @OneToMany(targetEntity=Reminder.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "customer")
    public List<Reminder> getReminders() {
        return reminders;
    }

    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
    }

    // TODO why are there no toString method for customer, technician, admin??
}


