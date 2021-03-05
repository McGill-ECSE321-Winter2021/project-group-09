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

    /**
     * return a single timeslot by id
     * @param id identifying the timeslot
     * @return  a single timeslot
     * @throws Exception timeslot not found by id
     */
    @Transactional
    public TimeSlotDto getTimeslotByID(Long id) throws Exception {
        Optional<TimeSlot> timeslot = timeSlotRepository.findById(id);
        if (timeslot.isPresent()) {
            return timeslotToDTO(timeslot.get());
        } else {
            //TODO custom exception type
            throw new Exception("Timeslot not found");
        }
    }

//    /**
//     * Get all timeslots associated with a single technician
//     * @param techEmail email of the technuician
//     * @return list of all timeslots
//     * @throws Exception technician doesn't exist
//     */
//    @Transactional
//    public List<TimeSlotDto> getTimeslotByTechnician(String techEmail) throws Exception {
//        Optional<Technician> technician = technicianRepository.findById(techEmail);
//        if (technician.isPresent()) {
//            return timeSlotRepository.findTimeslotsByTechnician(technician.get()).stream().map(TimeSlotService::timeslotToDTO).collect(Collectors.toList());
//        } else {
//            //TODO custome exception
//            throw new Exception("No such technician.");
//        }
//    }

    /**
     * get all timeslots
     * @return list of all timeslots
     */
    @Transactional
    public List<TimeSlotDto> getAllTimeslots() {
        return timeSlotRepository.findAll().stream().map(TimeSlotService::timeslotToDTO).collect(Collectors.toList());
    }

    //TODO
    //getTimeslotByAppointment
    //createTimeslot
    //deleteTimeslot
    //what else

    /**
     * helper method to transform an entity into a DTO
     * @param timeslot entity to turn into DTO
     * @return input entity in DTO form
     */
    public static TimeSlotDto timeslotToDTO(TimeSlot timeslot) {
        TimeSlotDto dto = new TimeSlotDto();
        dto.setEndDateTime(timeslot.getEndDateTime());
        dto.setStartDateTime(timeslot.getStartDateTime());
        return dto;
    }
}
