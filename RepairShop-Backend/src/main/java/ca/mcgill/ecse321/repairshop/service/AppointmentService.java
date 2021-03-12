package ca.mcgill.ecse321.repairshop.service;

import ca.mcgill.ecse321.repairshop.dto.*;
import ca.mcgill.ecse321.repairshop.model.*;
import ca.mcgill.ecse321.repairshop.repository.AppointmentRepository;
import ca.mcgill.ecse321.repairshop.repository.CustomerRepository;
import ca.mcgill.ecse321.repairshop.repository.ServiceRepository;
import ca.mcgill.ecse321.repairshop.repository.TechnicianRepository;
import ca.mcgill.ecse321.repairshop.repository.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import static ca.mcgill.ecse321.repairshop.service.TimeSlotService.timeslotToDTO;
import static ca.mcgill.ecse321.repairshop.service.ServiceService.serviceToDTO;
import static ca.mcgill.ecse321.repairshop.service.TechnicianService.technicianToDTO;
import static ca.mcgill.ecse321.repairshop.service.CustomerService.customerToDTO;
import static ca.mcgill.ecse321.repairshop.service.utilities.ValidationHelperMethods.*;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class AppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    TechnicianRepository technicianRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BusinessRepository businessRepository;

    // TODO: Implement some more methods from the repository

    /** Helper method to determine if an appointment can be booked with certain parameters
     * @param timeSlot for the target start and end time of the appointment
     * @param technician to check for the appointment
     * @param business to check for conflicting holidays
     * @return a boolean for whether an appointment can be booked then
     */
    public static boolean isBookable(TimeSlot timeSlot, Technician technician, Business business) {

        if (technician == null) return false;
        // Get technician's work hours
        List<TimeSlot> workHours = technician.getTimeslots();

        // TODO: use adjusted hours. Just testing for now...
//        List<TimeSlot> adjustedHours = new ArrayList<>();
//        for (TimeSlot hours : workHours) {
//            adjustedHours.add(getUpdatedHours(hours));
//        }

        // Get Technician's appointments
        List<Appointment> appointments = technician.getAppointments();
        // Get their corresponding timeslots
        List<TimeSlot> appointmentTimeslots = new ArrayList<>();

        for (Appointment appointment : appointments) {
            appointmentTimeslots.add(appointment.getTimeSlot());
        }

        // Get all holidays (There will always only be one business)
        List<TimeSlot> allHolidays = business.getHolidays();

        // Check if it can be booked at that time
        // Need to check if there is a timeslot that can be booked within the technician's work hours
        // but not overlapping with other appointments the technician has, and not during holidays

        // Within technician's work hours
        boolean withinHours = false;

        for (TimeSlot hours : workHours) {
            if (!hours.getStartDateTime().after(timeSlot.getStartDateTime()) && !hours.getEndDateTime().before(timeSlot.getEndDateTime())) {
                withinHours = true;
                break;
            }
        }

        if (!withinHours) return false;

        // Does not overlap with the technician's other appointments. If it does, return false
        for (TimeSlot app : appointmentTimeslots) {
            if (!timeSlot.getStartDateTime().after(app.getEndDateTime()) && !timeSlot.getEndDateTime().before(app.getStartDateTime())) return false;
        }

        // Does not overlap with holidays. If it does, return false
        for (TimeSlot holiday : allHolidays) {
            if (!timeSlot.getStartDateTime().after(holiday.getEndDateTime()) && !timeSlot.getEndDateTime().before(holiday.getStartDateTime())) return false;
        }
        
        // Passed all checks, so can be booked
        return true;
    }

    /** Method to book an appointment given a valid timeslot
     * @param startTimestamp when the appointment will start
     * @param serviceName the name of the appointment's service
     * @param technicianEmails the emails of the technicians that can perform the service at the specified start time
     * @return an AppointmentDto for the bookedAppointment
     * @throws Exception for invalid timestamp, service name or technician's email
     */
    @Transactional
    public AppointmentDto createAppointment(String startTimestamp, String serviceName, String technicianEmails, String customerEmail, String businessName) throws Exception {

        // Validate all inputs

        if (startTimestamp == null || startTimestamp.equals("")) throw new Exception("The Timestamp is mandatory");
        if (serviceName == null || serviceName.equals("")) throw new Exception("The service name is mandatory");
        if (technicianEmails == null || technicianEmails.equals("")) throw new Exception("Technicians are mandatory");
        if (customerEmail == null || customerEmail.equals("")) throw new Exception("The customer is mandatory");
        if (businessName == null || businessName.equals("")) throw new Exception("The business is mandatory");

        Timestamp startTime;
        Service service;
        Technician technician = null;
        Customer customer;
        Business business;

        try {
            startTime = Timestamp.valueOf(startTimestamp);
        } catch (Exception e) {
            throw new Exception("The provided Timestamp is invalid");
        }

        service = serviceRepository.findServiceByName(serviceName);
        if (service == null) throw new Exception("The provided service name is invalid");

        customer = customerRepository.findCustomerByEmail(customerEmail);
        if (customer == null) throw new Exception("The provided customer email is invalid");

        business = businessRepository.findBusinessByName(businessName);
        if (business == null) throw new Exception("The provided business name is invalid");

        // input is all available technicians' emails (comma separated), so finding each technician:
        // going to use technician with the least appointments already booked
        // if a technician has 0 appointments, go with that one

        String[] allEmails = technicianEmails.split(", ");
        Technician tempTech;
        int numAppointments = Integer.MAX_VALUE;

        for (String email : allEmails) {
            tempTech = technicianRepository.findTechnicianByEmail(email);
            if (tempTech == null) throw new Exception("A technician's email is invalid");
            else {
                int tempNumApps = tempTech.getAppointments().size();
                if (numAppointments > tempNumApps) {
                    numAppointments = tempNumApps;
                    technician = tempTech;
                    if (tempNumApps == 0) break;
                }
            }
        }

        // Create timeslot
        // the end time is the start time + service duration * 30 minutes * 60 seconds * 1000 milliseconds
        // (the service duration is the number of blocks of 30 minutes)
        int durationInMillis = service.getDuration() * 30 * 60 * 1000;
        Timestamp endTime = new Timestamp(startTime.getTime() + durationInMillis);
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setStartDateTime(startTime);
        timeSlot.setEndDateTime(endTime);

        // Verify that the appointment can still be booked
        // This should always be good, except if the customer leaves the booking page open for a while and someone else
        // books an appointment while they are looking at it
        if (!isBookable(timeSlot, technician, business)) throw new Exception("The appointment cannot be booked");

        Appointment appointment = new Appointment();
        appointment.setTimeSlot(timeSlot);
        appointment.setService(service);
        appointment.setTechnician(technician);
        appointment.setCustomer(customer);

        appointmentRepository.save(appointment);

        return appointmentToDto(appointment);

    }

    /** Helper method to convert Appointment to AppointmentDto
     * @param appointment to convert to dto
     * @return appointmentDto object
     */
    public static AppointmentDto appointmentToDto(Appointment appointment) {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setTimeSlotDto(timeslotToDTO(appointment.getTimeSlot()));
        appointmentDto.setServiceDto(serviceToDTO(appointment.getService()));
        appointmentDto.setTechnicianDto(technicianToDTO(appointment.getTechnician()));
        appointmentDto.setCustomerDto(customerToDTO(appointment.getCustomer()));
        return appointmentDto;
    }

}
