package ca.mcgill.ecse321.repairshop.service;

import ca.mcgill.ecse321.repairshop.dto.AppointmentDto;
import ca.mcgill.ecse321.repairshop.dto.ServiceDto;
import ca.mcgill.ecse321.repairshop.dto.TechnicianDto;
import ca.mcgill.ecse321.repairshop.dto.TimeSlotDto;
import ca.mcgill.ecse321.repairshop.model.Technician;
import ca.mcgill.ecse321.repairshop.repository.AppointmentRepository;
import ca.mcgill.ecse321.repairshop.repository.TechnicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import static ca.mcgill.ecse321.repairshop.service.TechnicianService.technicianToDTO;

import javax.transaction.Transactional;
import java.sql.Timestamp;

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
        TimeSlotDto timeSlotDto = timeSlotService.createTimeslot(startTime, endTime);

        // TODO: create appointment with proper values
        return createAppointment(timeSlotDto, serviceDto, technicianToDTO(technician));

    }

    public AppointmentDto createAppointment(TimeSlotDto timeSlotDto, ServiceDto serviceDto, TechnicianDto technicianDto) throws Exception {
        return null;
    }

}
