package ca.mcgill.ecse321.repairshop.service;

import ca.mcgill.ecse321.repairshop.dto.TimeSlotDto;
import ca.mcgill.ecse321.repairshop.model.TimeSlot;
import ca.mcgill.ecse321.repairshop.repository.TimeSlotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class TestTimeSlotService {

    private static final Timestamp START_TIME = Timestamp.valueOf("2021-03-02 10:00:00");
    private static final Timestamp END_TIME = Timestamp.valueOf("2021-03-02 11:00:00");
    private static final Long ID = 1L;
    @Mock
    TimeSlotRepository timeSlotRepository;

    @InjectMocks
    TimeSlotService timeSlotService;

    @BeforeEach
    public void setMockOutputs() {

        lenient().when(timeSlotRepository.findById(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(1L)) {
                TimeSlot ts = new TimeSlot();
                ts.setStartDateTime(START_TIME);
                ts.setEndDateTime(END_TIME);
                ts.setTimeSlotID(ID);
                return Optional.of(ts);
            } else {
                throw new Exception("Timeslot not found...");
            }
        });

        lenient().when(timeSlotRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            TimeSlot ts = new TimeSlot();
            ts.setStartDateTime(START_TIME);
            ts.setEndDateTime(END_TIME);
            ts.setTimeSlotID(ID);
            List<TimeSlot> tslist = new ArrayList<>();
            tslist.add(ts);
            return tslist;
        });

        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> invocation.getArgument(0);
        lenient().when(timeSlotRepository.save(any(TimeSlot.class))).thenAnswer(returnParameterAsAnswer);
    }

    @Test
    public void testCreateTimeslot() {
        TimeSlotDto timeslotDto = new TimeSlotDto();
        timeslotDto.setStartDateTime(START_TIME);
        timeslotDto.setEndDateTime(END_TIME);

        TimeSlotDto returnedTimeslot = timeSlotService.createTimeslot(timeslotDto);
        assertEquals(START_TIME, returnedTimeslot.getStartDateTime());
        assertEquals(END_TIME, returnedTimeslot.getEndDateTime());
    }

    @Test
    public void testGetTimeslot() {
        TimeSlotDto timeslotDto = new TimeSlotDto();
        timeslotDto.setStartDateTime(START_TIME);
        timeslotDto.setEndDateTime(END_TIME);
        try {
            TimeSlotDto foundByID = timeSlotService.getTimeslotByID(1L);
            assertEquals(foundByID.getEndDateTime(), END_TIME);
            assertEquals(foundByID.getStartDateTime(), START_TIME);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetTimeslotNonExistent() {
        TimeSlotDto timeslotDto = new TimeSlotDto();
        timeslotDto.setStartDateTime(START_TIME);
        timeslotDto.setEndDateTime(END_TIME);
        try {
            TimeSlotDto foundByID = timeSlotService.getTimeslotByID(6L);
            fail("Should not have found anything");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Timeslot not found...");
        }
    }

    @Test
    public void testGetAllTimeslot() {
        List<TimeSlotDto> timeslots = timeSlotService.getAllTimeslots();
        assertEquals(1, timeslots.size()); // List contains a single element.
        TimeSlotDto timeslot = (TimeSlotDto) timeslots.toArray()[0];
        assertEquals(ID, timeslot.getID()); // List contains the only comment.
    }

}
