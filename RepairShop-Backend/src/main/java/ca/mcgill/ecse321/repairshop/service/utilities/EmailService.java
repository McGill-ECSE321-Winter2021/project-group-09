


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
        msg.setText("Hi " + name + ", \n\nYou have successfully created an account with the Group 9 Repair Shop.\n" +
                "Your password is: " + newPassword + ".\n Please keep it safe, and do not share it with anyone." +
                "You are now ready to log in! \n\n Group 9 Repair Shop");
        javaMailSender.send(msg);
    }

    /**
     * Sends a successful booking confirmation email to the recipient.
     *
     * @param recipientEmail      email of the recipient (String)
     * @param recipientName       name of the recipient (String)
     * @param appointmentDateTime when is the appointment (Timestamp)
     * @param serviceName         Name of the service
     * @param price               price of the service
     */
    public void sendConfirmationEmail(String recipientEmail, String recipientName,
                                      Timestamp appointmentDateTime, String serviceName, String price) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(systemEmail);
        msg.setTo(recipientEmail);
        String serviceNameStr = serviceName.substring(0, 1).toUpperCase() + serviceName.substring(1);
        msg.setSubject("Auto Repair Shop 9: Booking Confirmation");
        msg.setText("Hello " + recipientName + ", \n\nThank you for choosing Auto Repair Shop 9! Your booking is now confirmed.\n\n" +
                "Here are the booking details:\n\n" + appointmentDateTime + "\nService:\n" + serviceNameStr
                + "\nPrice:\n" + price + "$\n\n " + "Note: 7 days notice is required to cancel an appointment." +
                "\n\n Your Amazing Group 9 Auto Repair Shop team");
        javaMailSender.send(msg);
    }


    /**
     * Sends an upcomingAppointment reminder or serviceReminder<br/>
     *
     * @param recipientEmail      email of the recipient (String)
     * @param recipientName       name of the recipient (String)
     * @param appointmentDateTime when is the appointment (Timestamp)
     * @param typeOfReminder      Type of reminder (ReminderType)
     * @param serviceName         Name of the service
     * @param price               price of the service
     */
    public void sendReminderEmail(String recipientEmail, String recipientName,
                                  Timestamp appointmentDateTime, ReminderType typeOfReminder, String serviceName, String price) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(systemEmail);
        msg.setTo(recipientEmail);

        String appointmentDate = appointmentDateTime.toString().substring(0, 10);
        String appointmentTime = appointmentDateTime.toString().substring(11, 16);
        String serviceNameStr = serviceName.substring(0, 1).toUpperCase() + serviceName.substring(1);

        if (typeOfReminder.equals(ReminderType.UpcomingAppointment)) { // 10 days before appointment
            msg.setSubject("Auto Repair Shop 9: Appointment Reminder");
            msg.setText("Hello " + recipientName + ", \n\nYou have an upcoming appointment on " + appointmentDate + " at " + appointmentTime +
                    "\n\nHere are the booking details:\n\n" + appointmentDate + " at " + appointmentTime + "\nService:\n" + serviceNameStr
                    + "\nPrice:\n" + price + "$\n\n " + "Note: 7 days notice is required to cancel an appointment." +
                    "\n\n Your Amazing Group 9 Auto Repair Shop team");


        } else { //ReminderType.ServiceReminder

            String article = "a";
            if (serviceName.charAt(0) == 'a' || serviceName.charAt(0) == 'e' ||
                    serviceName.charAt(0) == 'i' || serviceName.charAt(0) == 'o' || serviceName.charAt(0) == 'u') {
                article = "an";
            }
            msg.setSubject("Auto Repair Shop 9: " + "Time for a " + serviceName);
            msg.setText("Hello " + recipientName + ", \n\nIt has been a few months since your car received " + article + " " + serviceName + ".\n"
                    + "You can book your next appointment now using our website or our application!\n\n" + "Have a great day!" +
                    "\n\n Your Amazing Group 9 Auto Repair Shop team");
        }
        javaMailSender.send(msg);
    }

    /**
     * Sends a cancel confirmation email.
     *
     * @param recipientEmail      email of the recipient (String)
     * @param recipientName       name of the recipient (String)
     * @param appointmentDateTime when is the appointment (Timestamp)
     * @param serviceName         name of the service (String)
     */
    public void appointmentCancelledEmail(String recipientEmail, String recipientName,
                                          Timestamp appointmentDateTime, String serviceName) {
        String appointmentDate = appointmentDateTime.toString().substring(0, 10);
        String appointmentTime = appointmentDateTime.toString().substring(11, 16);
        String serviceNameStr = serviceName.substring(0, 1).toUpperCase() + serviceName.substring(1);
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(systemEmail);
        msg.setTo(recipientEmail);
        msg.setSubject("Auto Repair Shop 9: Cancelled Appointment");
        msg.setText("Dear " + recipientName + ", \n\nThe following appointment has been cancelled:\n\n" + appointmentDate + " at " + appointmentTime + "\nService:\n" + serviceNameStr
                + "\n\n Your Amazing Group 9 Auto Repair Shop team");
        javaMailSender.send(msg);
    }


}

