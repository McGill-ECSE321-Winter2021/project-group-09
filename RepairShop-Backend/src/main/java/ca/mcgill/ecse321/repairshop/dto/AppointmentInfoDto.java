package ca.mcgill.ecse321.repairshop.dto;

import com.sun.istack.NotNull;

import java.sql.Timestamp;

public class AppointmentInfoDto {

    @NotNull
    private Timestamp startTime;
    @NotNull
    private String serviceName;
    @NotNull
    private String customerEmail;

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
}
