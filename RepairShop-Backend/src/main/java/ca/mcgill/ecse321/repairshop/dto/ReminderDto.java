package ca.mcgill.ecse321.repairshop.dto;

import ca.mcgill.ecse321.repairshop.model.ReminderType;
import com.sun.istack.NotNull;

import java.sql.Timestamp;

public class ReminderDto {

    @NotNull
    private Timestamp dateTime;
    @NotNull
    private ReminderType reminderType;
    @NotNull
    private CustomerDto customerDto;

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

    public CustomerDto getCustomerDto() {
        return customerDto;
    }
    public void setCustomerDto(CustomerDto customerDto) {
        this.customerDto = customerDto;
    }

}
