package ca.mcgill.ecse321.repairshop.service.utilities;

import ca.mcgill.ecse321.repairshop.dto.ReminderDto;
import ca.mcgill.ecse321.repairshop.model.Appointment;
import ca.mcgill.ecse321.repairshop.model.Reminder;
import ca.mcgill.ecse321.repairshop.model.ReminderType;
import ca.mcgill.ecse321.repairshop.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static ca.mcgill.ecse321.repairshop.service.utilities.SystemTime.addOrSubtractDays;
import static ca.mcgill.ecse321.repairshop.service.utilities.SystemTime.systemDate;

@Component
public class ScheduledTasks {

    @Autowired
    private ReminderService reminderService;

    //TODO: Remove this later.
    @Scheduled(cron = "*/5 * * * * ?")
    public void testMethod() {
        System.out.println("Hi! :)  I am testMethod() in ScheduleTasks. I am called every 5 seconds. The CURRENT TIME is :: " + new Date());
    }


    /**
     * Everyday at 6 am: send today's service and appointment reminders.
     */
    @Scheduled(cron = "0 0 6 * * ? *")
    public void sendAllTodayReminder() {

        String today = systemDate();

        List<ReminderDto> allReminderDtos = reminderService.getAllReminders();

        for (ReminderDto reminderDto : allReminderDtos) {
            String reminderDate = reminderDto.getDateTime().toString().substring(0, 10);

            if (reminderDate.equals(today)) {
                EmailService emailService = new EmailService();

                String customerEmail = reminderDto.getCustomerDto().getEmail();
                String customerName = reminderDto.getCustomerDto().getName();
                Timestamp appointmentDateTime = null;
                String serviceName = null;
                String price = null;
                ReminderType reminderType = reminderDto.getReminderType();

                if(reminderType.equals(ReminderType.AppointmentReminder)){
                    appointmentDateTime = addOrSubtractDays(reminderDto.getDateTime(),10);
                    //TODO: Let's assume that an appointment reminder is always sent to the customer 10 days before

                   //TODO: continue this tomorrow serviceName, price
                }

                emailService.sendReminderAndConfirmationEmail(customerEmail, customerName,appointmentDateTime,reminderType,serviceName, price);
            }
        }
    }

    //TODO: remove this later
    public static void main(String[] args) {

/*        Timestamp startDateTime = Timestamp.valueOf("2021-10-15 00:07:00.00");
        System.out.println("Timestamp: " + startDateTime.toString());
        String day1 = startDateTime.toString().substring(0, 10);
        System.out.println("Extract only year/month/day: " + day1);

        Timestamp startDateTime2 = Timestamp.valueOf("2021-10-15 00:14:40.00");
        System.out.println("Timestamp: " + startDateTime2.toString());
        String day2 = startDateTime2.toString().substring(0, 10);
        System.out.println("Extract only year/month/day: " + startDateTime2.toString().substring(0, 10));

        if (day1.equals(day2)) System.out.println("SAME DAY");

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println("NOW= " + timestamp.toString().substring(0, 10));*/

        Timestamp startDateTime = Timestamp.valueOf("2021-10-15 00:07:00.00");
        System.out.println("Timestamp: " + startDateTime.toString());

        Timestamp resultAddDays = addOrSubtractDays(startDateTime,10);
        System.out.println("After adding days: "+resultAddDays);

        Timestamp resultSubtractDays = addOrSubtractDays(startDateTime,-10);
        System.out.println("After removing days: "+resultSubtractDays);

    }

}