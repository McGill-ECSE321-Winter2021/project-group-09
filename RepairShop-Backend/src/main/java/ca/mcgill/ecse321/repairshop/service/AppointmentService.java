package ca.mcgill.ecse321.repairshop.service;

import ca.mcgill.ecse321.repairshop.dto.AppointmentDto;
import ca.mcgill.ecse321.repairshop.dto.ServiceDto;
import ca.mcgill.ecse321.repairshop.dto.TechnicianDto;
import ca.mcgill.ecse321.repairshop.dto.TimeSlotDto;
import ca.mcgill.ecse321.repairshop.model.Appointment;
import ca.mcgill.ecse321.repairshop.model.Technician;
import ca.mcgill.ecse321.repairshop.repository.AppointmentRepository;
import ca.mcgill.ecse321.repairshop.repository.TechnicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import static ca.mcgill.ecse321.repairshop.service.TechnicianService.technicianToDTO;
import static ca.mcgill.ecse321.repairshop.service.TimeSlotService.timeslotToDTO;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class AppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    ServiceService serviceService;

    @Autowired
    TechnicianRepository technicianRepository;

    @Autowired
    TimeSlotService timeSlotService;

    @Autowired
    BusinessService businessService;

    @Transactional
    public boolean isBookable(Timestamp startTime, ServiceDto serviceDto, String technicianEmail) throws Exception {

        // Get timeslot of appointment to be booked
        TimeSlotDto timeSlotDto = new TimeSlotDto();
        timeSlotDto.setStartDateTime(startTime);
        Timestamp endTime = new Timestamp(startTime.getTime() + (long) serviceDto.getDuration() * 30 * 60 * 1000);
        timeSlotDto.setEndDateTime(endTime);

        // Get Technician
        Technician technician = technicianRepository.findTechnicianByEmail(technicianEmail);
        if (technician == null) throw new Exception("The technician's email is invalid");

        // Get Technician's appointments
        List<Appointment> appointments = technician.getAppointments();

        // Get their corresponding timeslots
        List<TimeSlotDto> appointmentTimeslots = new ArrayList<>();

        for (Appointment appointment : appointments) {
            //appointmentTimeslots.add(timeslotToDTO(appointment.getTimeSlot())); TODO: modify timeslots
        }

        // Get all holidays
        List<TimeSlotDto> holidayTimeslots = businessService.getAllBusinesses().get(0).getHolidays(); // There will always only be one business

        // check if it can be booked at that time






        return false;
    }

    /** Method to book an appointment given a valid timeslot
     * @param startTimestamp when the appointment will start
     * @param serviceName the name of the appointment's service
     * @param technicianEmails the emails of the technicians that can perform the service at the specified start time
     * @return an AppointmentDto for the bookedAppointment
     * @throws Exception for invalid timestamp, service name or technician's email
     */
    @Transactional
    public AppointmentDto bookAppointment(String startTimestamp, String serviceName, String technicianEmails) throws Exception {

        if (startTimestamp == null || startTimestamp.equals("")) throw new Exception("The Timestamp is mandatory");
        if (serviceName == null || serviceName.equals("")) throw new Exception("The service name is mandatory");
        if (technicianEmails == null || technicianEmails.equals("")) throw new Exception("Technicians are mandatory");

        Timestamp startTime;
        ServiceDto serviceDto;
        Technician technician = null;

        try {
            startTime = Timestamp.valueOf(startTimestamp);
        } catch (Exception e) {
            throw new Exception("The provided Timestamp is invalid");
        }

        serviceDto = serviceService.getServiceByName(serviceName);
        if (serviceDto == null) throw new Exception("The provided service name is invalid");

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
        int durationInMillis = serviceDto.getDuration() * 30 * 60 * 1000;
        Timestamp endTime = new Timestamp(startTime.getTime() + durationInMillis);
        TimeSlotDto timeSlotDto = new TimeSlotDto();
        timeSlotDto.setStartDateTime(startTime);
        timeSlotDto.setEndDateTime(endTime);

        // TODO: create appointment with proper values
        return createAppointment(timeSlotDto, serviceDto, technicianToDTO(technician));

    }

    public AppointmentDto createAppointment(TimeSlotDto timeSlotDto, ServiceDto serviceDto, TechnicianDto technicianDto) throws Exception {
        return null;
    }

}
