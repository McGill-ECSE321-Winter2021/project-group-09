package ca.mcgill.ecse321.repairshop.service;

import ca.mcgill.ecse321.repairshop.dto.ReminderDto;
import ca.mcgill.ecse321.repairshop.model.Customer;
import ca.mcgill.ecse321.repairshop.model.Reminder;
import ca.mcgill.ecse321.repairshop.model.ReminderType;
import ca.mcgill.ecse321.repairshop.repository.ReminderRepository;
import static ca.mcgill.ecse321.repairshop.service.CustomerService.customerToDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReminderService {

    @Autowired
    private ReminderRepository reminderRepository;

    /** Finds all reminders for a given customer and returns them as a list of reminderDtos
     * @param customer for whom to get the reminders
     * @return a list of reminderDtos
     */
    @Transactional
    public List<ReminderDto> getRemindersByCustomer(Customer customer) throws Exception {

        List<Reminder> reminders = reminderRepository.findByCustomer(customer);

        // Check if any were found
        if (reminders != null) return reminders.stream().map(this::reminderToDto).collect(Collectors.toList());
        else throw new Exception("Could not find reminders for the specified customer");

    }

    /** Creates a reminder object
     * @param dateTime of the reminder (a timestamp that includes date and time)
     * @param reminderType the type of the reminder
     * @param customer associated to the appointment
     * @return the reminder object
     */
    public ReminderDto createReminder(Timestamp dateTime, ReminderType reminderType, Customer customer) throws Exception {

        if (dateTime == null) throw new Exception("The Timestamp is mandatory");
        if (reminderType == null) throw new Exception("The ReminderType is mandatory");
        if (customer == null) throw new Exception("The Customer is mandatory");

        Reminder reminder = new Reminder();
        reminder.setDateTime(dateTime);
        reminder.setReminderType(reminderType);
        reminder.setCustomer(customer);

        reminderRepository.save(reminder);

        return reminderToDto(reminder);

    }

    /** Helper method to convert Reminder to ReminderDto
     * @param reminder to convert to dto
     * @return reminderDto object
     */
    public ReminderDto reminderToDto(Reminder reminder) {
        ReminderDto reminderDto = new ReminderDto();
        reminderDto.setDateTime(reminder.getDateTime());
        reminderDto.setReminderType(reminder.getReminderType());
        reminderDto.setCustomerDto(customerToDTO(reminder.getCustomer()));
        return reminderDto;
    }


}
