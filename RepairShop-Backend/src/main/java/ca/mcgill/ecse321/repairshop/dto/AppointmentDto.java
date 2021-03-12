package ca.mcgill.ecse321.repairshop.dto;
import com.sun.istack.NotNull;
public class AppointmentDto {

    private Long appointmentID;
    @NotNull
    private ServiceDto serviceDto;
    @NotNull
    private CustomerDto customerDto;
    @NotNull
    private TechnicianDto technicianDto;
    @NotNull
    private TimeSlotDto timeSlotDto;

    public Long getAppointmentID() {
        return appointmentID;
    }
    public void setAppointmentID(Long appointmentID) {
        this.appointmentID = appointmentID;
    }

    public ServiceDto getServiceDto() {
        return serviceDto;
    }
    public void setServiceDto(ServiceDto serviceDto) {
        this.serviceDto = serviceDto;
    }

    public CustomerDto getCustomerDto() {
        return customerDto;
    }
    public void setCustomerDto(CustomerDto customerDto) {
        this.customerDto = customerDto;
    }

    public TechnicianDto getTechnicianDto() {
        return technicianDto;
    }
    public void setTechnicianDto(TechnicianDto technicianDto) {
        this.technicianDto = technicianDto;
    }

    public TimeSlotDto getTimeSlotDto() {
        return timeSlotDto;
    }
    public void setTimeSlotDto(TimeSlotDto timeSlotDto) {
        this.timeSlotDto = timeSlotDto;
    }

}
