package ca.mcgill.ecse321.repairshop.service;

import ca.mcgill.ecse321.repairshop.dto.ReminderDto;
import ca.mcgill.ecse321.repairshop.model.Customer;
import ca.mcgill.ecse321.repairshop.model.Reminder;
import ca.mcgill.ecse321.repairshop.model.ReminderType;
import ca.mcgill.ecse321.repairshop.repository.CustomerRepository;
import ca.mcgill.ecse321.repairshop.repository.ReminderRepository;
import static ca.mcgill.ecse321.repairshop.service.CustomerService.customerToDto;
import ca.mcgill.ecse321.repairshop.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReminderService {

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ServiceRepository serviceRepository;
    
    

    /**
     * Finds all reminders for a given customer and returns them as a list of reminderDtos
     *
     * @param email for the customer for whom to get the reminders
     * @return a list of reminderDtos
     */
    @Transactional
    public List<ReminderDto> getRemindersByCustomerEmail(String email) throws Exception {

        Customer customer = customerRepository.findCustomerByEmail(email);
        if (customer == null) throw new Exception("A valid customer email is required");

        List<Reminder> reminders = reminderRepository.findByCustomer(customer);

        // Check if any were found
        if (reminders != null) {
            List<ReminderDto> reminderDtos = new ArrayList<>();
            for (Reminder reminder : reminders) {
                reminderDtos.add(reminderToDTO(reminder));
            }
            return reminderDtos;
        } else throw new Exception("Could not find reminders for the specified customer");

    }
    

    /**
     * Creates a reminder object
     *
     * @param dateTime            of the reminder (a timestamp that includes date and time)
     * @param appointmentDateTime date and time of the appointment (String)
     * @param serviceName         name of the service
     * @param type     the type of the reminder
     * @param email               of the customer associated to the appointment
     * @return the reminder object
     */
    public ReminderDto createReminder(Timestamp dateTime, Timestamp appointmentDateTime, String serviceName, String type, String email) throws Exception {

        if (dateTime == null || appointmentDateTime == null) throw new Exception("The Timestamps are mandatory");
        if (type == null || type.equals("")) throw new Exception("The ReminderType is mandatory");
        if (email == null || email.equals("")) throw new Exception("The customer email is mandatory");
        if (serviceName == null || serviceName.equals("")) throw new Exception("The service name is mandatory");

        ReminderType reminderType;
        Customer customer;

        try {
            reminderType = ReminderType.valueOf(type);
        } catch (Exception e) {
            throw new Exception("The provided ReminderType is invalid");
        }

        customer = customerRepository.findCustomerByEmail(email);
        if (customer == null) throw new Exception("The provided customer email does not exist");
        if(serviceRepository.findServiceByName(serviceName) == null) throw new Exception("The provided service name does not exist");
        Reminder reminder = new Reminder();
        reminder.setDateTime(dateTime);
        reminder.setReminderType(reminderType);
        reminder.setCustomer(customer);
        reminder.setAppointmentDateTime(appointmentDateTime);
        reminder.setServiceName(serviceName);
        reminderRepository.save(reminder);

        return reminderToDTO(reminder);

    }
    
    

    /**
     * Gets all reminders from reminderRepository
     *
     * @return List of reminderDto objects : List<ReminderDto>
     */
    @Transactional
    public List<ReminderDto> getAllReminders() {
        List<Reminder> reminders = reminderRepository.findAll();
        List<ReminderDto> reminderDtos = new ArrayList<>();
        for (Reminder reminder : reminders) {
            reminderDtos.add(reminderToDTO(reminder));
        }
        return reminderDtos;
    }
    
    

    /**
     * Delete a reminder by its ID from the reminderRepository
     *
     * @param reminderID ID of the reminder
     * @return String that indicates if the delete was successful
     * @throws Exception if reminder is not found
     */
    @Transactional
    public String deleteReminderById(Long reminderID) throws Exception {
        Optional<Reminder> reminder = reminderRepository.findById(reminderID);

        if (reminder.isPresent()) {
            reminderRepository.deleteById(reminderID);
            return "Reminder with ID " + reminderID + " is deleted.";
        } else {
            throw new Exception("Reminder not found...");
        }
    }
    
    

    /**
     * Helper method to convert Reminder to ReminderDto
     *
     * @param reminder to convert to dto
     * @return reminderDto object
     */
    public static ReminderDto reminderToDTO(Reminder reminder) {
        ReminderDto reminderDto = new ReminderDto();
        reminderDto.setReminderID(reminder.getReminderID());
        reminderDto.setDateTime(reminder.getDateTime());
        reminderDto.setReminderType(reminder.getReminderType());
        reminderDto.setCustomerDto(customerToDto(reminder.getCustomer()));
        reminderDto.setServiceName(reminder.getServiceName());
        reminderDto.setAppointmentDateTime(reminder.getAppointmentDateTime());
        return reminderDto;
    }

}
