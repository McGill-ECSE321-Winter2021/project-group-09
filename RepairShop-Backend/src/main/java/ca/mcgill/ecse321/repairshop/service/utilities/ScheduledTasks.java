package ca.mcgill.ecse321.repairshop.service.utilities;

import ca.mcgill.ecse321.repairshop.dto.ReminderDto;
import ca.mcgill.ecse321.repairshop.model.ReminderType;
import ca.mcgill.ecse321.repairshop.service.ReminderService;
import ca.mcgill.ecse321.repairshop.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static ca.mcgill.ecse321.repairshop.service.utilities.SystemTime.getSystemDate;

@Component
public class ScheduledTasks {

    @Autowired
    private ReminderService reminderService;

    @Autowired
    private ServiceService serviceService;

    //TODO: Remove this later. This is to test the Scheduled class3
    @Scheduled(cron = "*/5 * * * * ?")
    public void testMethod() {
        System.out.println("Hi! :)  I am testMethod() in ScheduleTasks. I am called every 5 seconds. The CURRENT TIME is :: " + new Date());


    }


    /**
     * Everyday at 6 am, this method will be called to send all today's:<br/>
     * - Service Reminders<br/>
     * - Upcoming Appointment
     */
    @Scheduled(cron = "0 0 6 * * ?")
    public void sendAllTodayReminder() {

        String today = getSystemDate();

        List<ReminderDto> allReminderDtos = reminderService.getAllReminders();

        for (ReminderDto reminderDto : allReminderDtos) {
            String reminderDate = reminderDto.getDateTime().toString().substring(0, 10);

            if (reminderDate.equals(today)) {
                EmailService emailService = new EmailService();

                String customerEmail = reminderDto.getCustomerDto().getEmail();
                String customerName = reminderDto.getCustomerDto().getName();
                Timestamp appointmentDateTime = reminderDto.getAppointmentDateTime();
                ReminderType reminderType = reminderDto.getReminderType();
                String serviceName = reminderDto.getServiceName();
                String price = null;

                try {
                    price = String.valueOf(serviceService.getServiceByName(serviceName).getPrice());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                emailService.sendReminderEmail(customerEmail, customerName, appointmentDateTime, reminderType, serviceName, price);
            }
        }
    }
}