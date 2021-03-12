package ca.mcgill.ecse321.repairshop;

import ca.mcgill.ecse321.repairshop.model.ReminderType;
import ca.mcgill.ecse321.repairshop.service.utilities.EmailService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Timestamp;

@RestController
@SpringBootApplication
@EnableScheduling
public class RepairShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(RepairShopApplication.class, args);
		EmailService email = new EmailService();


		Timestamp START_TIME = Timestamp.valueOf("2021-12-02 10:00:00");

		//email.accountCreationEmail("sandylao289@gmail.com", "Sandy", "sfasdf");
		email.sendReminderAndConfirmationEmail("sandylao289@gmail.com","Sandy",Timestamp.valueOf("2021-12-02 10:00:00"),
				ReminderType.Confirmation,"debugging", "70");

	}

	@RequestMapping("/")
	public String greeting(){
		return "Hello world!";
	}



}
