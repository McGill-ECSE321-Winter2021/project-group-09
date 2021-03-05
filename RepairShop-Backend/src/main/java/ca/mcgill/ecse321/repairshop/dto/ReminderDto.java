package ca.mcgill.ecse321.repairshop.dto;

import ca.mcgill.ecse321.repairshop.model.Customer;
import ca.mcgill.ecse321.repairshop.model.ReminderType;
import com.sun.istack.NotNull;

import java.sql.Timestamp;

public class ReminderDto {

    @NotNull
    private Timestamp dateTime;
    @NotNull
    private ReminderType reminderType;
    @NotNull
    private Customer customer;

    public Timestamp getDateTime() {
        return dateTime;
    }
    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public ReminderType getReminderType() {
        return reminderType;
    }
    public void setReminderType(ReminderType reminderType) {
        this.reminderType = reminderType;
    }

    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
