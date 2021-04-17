package ca.mcgill.ecse321.repairshop.dto;

import com.sun.istack.NotNull;

import java.sql.Timestamp;

public class AppointmentInfoDto {

    @NotNull
    private Timestamp startTime;  //we're using this in the frontend (Please ignore the warning)
    @NotNull
    private String serviceName;
    @NotNull
    private String customerEmail; //we're using this in the frontend (Please ignore the warning)

    public Timestamp getStartTime() {
        return startTime;
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
}
