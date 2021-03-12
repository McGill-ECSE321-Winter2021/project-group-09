package ca.mcgill.ecse321.repairshop.service;

import ca.mcgill.ecse321.repairshop.dto.TimeSlotDto;
import ca.mcgill.ecse321.repairshop.model.TimeSlot;
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

    /**
     * helper method to transform an entity into a DTO
     *
     * @param timeslot entity to turn into DTO
     * @return input entity in DTO form
     */
    public static TimeSlotDto timeslotToDTO(TimeSlot timeslot) {
        TimeSlotDto dto = new TimeSlotDto();
        dto.setEndDateTime(timeslot.getEndDateTime());
        dto.setStartDateTime(timeslot.getStartDateTime());
        dto.setID(timeslot.getTimeSlotID());
        return dto;
    }

    /**
     * creates a new timeslot and stores it in db
     *
     * @param timeslot DTO describing new timeslot to associate
     */
    @Transactional
    public TimeSlotDto createTimeslot(TimeSlotDto timeslot) {
        TimeSlot newTimeslot = new TimeSlot();
        newTimeslot.setStartDateTime(timeslot.getStartDateTime());
        newTimeslot.setEndDateTime(timeslot.getEndDateTime());
        return timeslotToDTO(timeSlotRepository.save(newTimeslot));
    }

    /**
     * return a single timeslot by id
     *
     * @param id identifying the timeslot
     * @return a single timeslot
     * @throws Exception timeslot not found by id
     */
    @Transactional
    public TimeSlotDto getTimeslotByID(Long id) throws Exception {

        Optional<TimeSlot> timeslot = timeSlotRepository.findById(id);
        if (timeslot.isPresent()) {
            return timeslotToDTO(timeslot.get());
        } else {
            //TODO custom exception type
            throw new Exception("Timeslot not found...");
        }
    }

    /**
     * get all timeslots
     *
     * @return list of all timeslots
     */
    @Transactional
    public List<TimeSlotDto> getAllTimeslots() {
        return timeSlotRepository.findAll().stream().map(TimeSlotService::timeslotToDTO).collect(Collectors.toList());
    }
    
    
    
    public static TimeSlot DtoToTimeSlot(TimeSlotDto timeslot) {
        TimeSlot newTimeslot = new TimeSlot();
        newTimeslot.setStartDateTime(timeslot.getStartDateTime());
        newTimeslot.setEndDateTime(timeslot.getEndDateTime());
        return newTimeslot;
    }
    
    
    
    
}
