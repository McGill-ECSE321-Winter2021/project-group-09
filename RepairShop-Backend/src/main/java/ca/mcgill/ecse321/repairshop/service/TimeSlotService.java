package ca.mcgill.ecse321.repairshop.service;

import ca.mcgill.ecse321.repairshop.dto.TimeSlotDto;
import ca.mcgill.ecse321.repairshop.model.Technician;
import ca.mcgill.ecse321.repairshop.model.TimeSlot;
import ca.mcgill.ecse321.repairshop.repository.TechnicianRepository;
import ca.mcgill.ecse321.repairshop.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TimeSlotService {
    @Autowired
    TimeSlotRepository timeSlotRepository;

    @Autowired
    TechnicianRepository technicianRepository;

    @Transactional
    public TimeSlotDto getTimeslotByID(Long id) {
        Optional<TimeSlot> timeslot = timeSlotRepository.findById(id);
        if (timeslot.isPresent()) {
            return timeslotToDTO(timeslot.get());
        }
        else {
            //TODO custom exception type
            throw new Exception("Timeslot not found");
        }
    }

    @Transactional
    public List<TimeSlotDto> getTimeslotByEmployee(Long techID) {
        Optional<Technician> technician = technicianRepository.findById(techID);
        if (technician.isPresent()) {
            return timeSlotRepository.findTimeslotsByTechnician(technician.get()).stream().map(this::timeslotToDTO).collect(Collectors.toList());
        } else {
            //TODO custome exception
            throw new Exception("No such technician.");
        }
    }

    @Transactional
    public List<TimeSlotDto> getAllTimeslots() {
        return timeSlotRepository.findAll().stream().map(this::timeslotToDTO).collect(Collectors.toList());
    }

    //TODO
    //getTimeslotByAppointment
    //createTimeslot
    //deleteTimeslot
    //what else

    public TimeSlotDto timeslotToDTO(TimeSlot timeslot) {
        TimeSlotDto dto = new TimeSlotDto();
        //TODO
        dto.setAppointment(appointmentToDTO(timeslot.getAppointment()));
        dto.setEndDateTime(timeslot.getEndDateTime());
        dto.setStartDateTime(timeslot.getStartDateTime());
        //TODO
        dto.setTechnician(technicianToDto(timeslot.getTechnician()));
        return dto;
    }
}
