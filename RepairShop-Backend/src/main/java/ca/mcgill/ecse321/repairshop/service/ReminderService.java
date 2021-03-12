package ca.mcgill.ecse321.repairshop.service;

import ca.mcgill.ecse321.repairshop.dto.ReminderDto;
import ca.mcgill.ecse321.repairshop.model.Customer;
import ca.mcgill.ecse321.repairshop.model.Reminder;
import ca.mcgill.ecse321.repairshop.model.ReminderType;
import ca.mcgill.ecse321.repairshop.repository.CustomerRepository;
import ca.mcgill.ecse321.repairshop.repository.ReminderRepository;
import static ca.mcgill.ecse321.repairshop.service.CustomerService.customerToDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReminderService {

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    /** Finds all reminders for a given customer and returns them as a list of reminderDtos
     * @param email for the customer for whom to get the reminders
     * @return a list of reminderDtos
     */
    @Transactional
    public List<ReminderDto> getRemindersByCustomerEmail(String email) throws Exception {

        Customer customer = customerRepository.findCustomerByEmail(email);
        if (customer == null) throw new Exception("A valid customer email is required");

        List<Reminder> reminders;
        reminders = reminderRepository.findByCustomer(customer);

        // Check if any were found
        if (reminders != null) {
            List<ReminderDto> reminderDtos = new ArrayList<>();
            for (Reminder reminder : reminderRepository.findAll()) { reminderDtos.add(reminderToDTO(reminder)); }
            return reminderDtos;
        } else throw new Exception("Could not find reminders for the specified customer");

    }

    /** Creates a reminder object
     * @param dateTime of the reminder (a timestamp that includes date and time)
     * @param type the type of the reminder
     * @param email of the customer associated to the appointment
     * @return the reminder object
     */
    public ReminderDto createReminder(String dateTime, String type, String email) throws Exception {

        if (dateTime == null || dateTime.equals("")) throw new Exception("The Timestamp is mandatory");
        if (type == null || type.equals("")) throw new Exception("The ReminderType is mandatory");
        if (email == null || email.equals("")) throw new Exception("The customer email is mandatory");

        Timestamp timestamp;
        ReminderType reminderType;
        Customer customer;

        try {
            timestamp = Timestamp.valueOf(dateTime);
        } catch (Exception e) {
            throw new Exception("The provided Timestamp is invalid");
        }

        try {
            reminderType = ReminderType.valueOf(type);
        } catch (Exception e) {
            throw new Exception("The provided ReminderType is invalid");
        }

        customer = customerRepository.findCustomerByEmail(email);
        if (customer == null) throw new Exception("The provided customer email does not exist");

        Reminder reminder = new Reminder();
        reminder.setDateTime(timestamp);
        reminder.setReminderType(reminderType);
        reminder.setCustomer(customer);

        reminderRepository.save(reminder);

        return reminderToDTO(reminder);

    }

    /** Helper method to convert Reminder to ReminderDto
     * @param reminder to convert to dto
     * @return reminderDto object
     */
    public static ReminderDto reminderToDTO(Reminder reminder) {
        ReminderDto reminderDto = new ReminderDto();
        reminderDto.setReminderID(reminder.getReminderID());
        reminderDto.setDateTime(reminder.getDateTime());
        reminderDto.setReminderType(reminder.getReminderType());
        reminderDto.setCustomerDto(customerToDTO(reminder.getCustomer()));
        return reminderDto;
    }


}
