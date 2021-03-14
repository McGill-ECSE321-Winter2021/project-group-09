


package ca.mcgill.ecse321.repairshop.service.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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

}

