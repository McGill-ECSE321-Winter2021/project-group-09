package ca.mcgill.ecse321.repairshop.service;

import ca.mcgill.ecse321.repairshop.dto.AppointmentDto;
import ca.mcgill.ecse321.repairshop.model.Service;
import ca.mcgill.ecse321.repairshop.model.Technician;
import ca.mcgill.ecse321.repairshop.repository.AppointmentRepository;
import ca.mcgill.ecse321.repairshop.repository.ServiceRepository;
import ca.mcgill.ecse321.repairshop.repository.TechnicianRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.sql.Timestamp;

@org.springframework.stereotype.Service
public class AppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    TechnicianRepository technicianRepository;

    @Transactional
    public AppointmentDto bookAppointment(String startTime, String serviceName, String technicianEmails) throws Exception {

        if (startTime == null || startTime.equals("")) throw new Exception("The Timestamp is mandatory");
        if (serviceName == null || serviceName.equals("")) throw new Exception("The service name is mandatory");
        if (technicianEmails == null || technicianEmails.equals("")) throw new Exception("Technicians are mandatory");

        Timestamp timestamp;
        Service service;
        Technician technician;

        try {
            timestamp = Timestamp.valueOf(startTime);
        } catch (Exception e) {
            throw new Exception("The provided Timestamp is invalid");
        }

        service = serviceRepository.findServiceByName(serviceName);
        if (service == null) throw new Exception("The provided service name is invalid");

        // input is all available technicians' emails (comma separated), so finding each technician:
        // going to use technician with the least appointments already booked
        // if a technician has 0 appointments, go with that one

        String[] allEmails = technicianEmails.split(",( )?");
        Technician tempTech = null;
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

        return createAppointment();

    }

    public AppointmentDto createAppointment() throws Exception {
        return null;
    }

}
