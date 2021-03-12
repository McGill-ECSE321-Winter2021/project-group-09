package ca.mcgill.ecse321.repairshop.service.utilities;

import ca.mcgill.ecse321.repairshop.model.ReminderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class EmailService {

    private final String systemEmail = "${spring.mail.username}";
    @Autowired
    private JavaMailSender javaMailSender;

    public void accountCreationEmail(String recipientEmail, String name, String newPassword) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(systemEmail);
        msg.setTo(recipientEmail);
        msg.setSubject("Account Created - Group 9 Auto Repair Shop");
        msg.setText("Hi " + name + ", \n You have successfully created an account with the Group 9 Repair Shop.\n" +
                "Your password is: " + newPassword + ".\n Please keep it safe, and do not share it with anyone." +
                "You are now ready to log in! \n\n Group 9 Repair Shop");
        javaMailSender.send(msg);
    }

    /**
     * Send an reminder or confirmation to the recipient.
     *
     * @param recipientEmail      email of the recipient (String)
     * @param recipientName       name of the recipient (String)
     * @param appointmentDateTime when is the appointment (Timestamp)
     * @param typeOfReminder      Type of reminder (ReminderType)
     * @param serviceName         Name of the service
     * @param price               price of the service
     */
    public void sendReminderAndConfirmationEmail(String recipientEmail, String recipientName,
                                                 Timestamp appointmentDateTime, ReminderType typeOfReminder, String serviceName, String price) {
        System.out.println("SENDING.............");

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(systemEmail);
        msg.setTo(recipientEmail);

        if (typeOfReminder.equals(ReminderType.Confirmation)) { // BOOKING CONFIRMATION
            msg.setSubject("Auto Repair Shop 9: Booking Confirmation ");
            msg.setText("Hello " + recipientName + ", \nThank you for choosing Auto Repair Shop 9! Your booking is now confirmed.\n" +
                    "Here are the booking details:\n" + appointmentDateTime + "\nService:\n" + typeOfReminder.toString()
                    + "\nPrice:\n" + price + "$\n\n " + "Note: 7 days notice is required to cancel an appointment." +
                    "\n\n Your Amazing Group 9 Auto Repair Shop team");

        } else { //OTHER TYPE OF SERVICE REMINDER
            msg.setSubject("Auto Repair Shop 9: " + typeOfReminder.toString() + " Reminder");
            msg.setText("Hello " + recipientName + ", \n" + "It has been a few months since your car received a " + typeOfReminder.toString() + ".\n"
                    + "You can book your next appointment now using our website our application!\n" + "Have a great day!" +
                    "\n\n Your Amazing Group 9 Auto Repair Shop team");

        }

        javaMailSender.send(msg);
        System.out.println("SENT!!!!!!!!!!!!!!!!!!!!!");
    }


}