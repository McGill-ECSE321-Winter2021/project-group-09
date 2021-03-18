package ca.mcgill.ecse321.repairshop;

import ca.mcgill.ecse321.repairshop.model.Admin;
import ca.mcgill.ecse321.repairshop.model.Business;
import ca.mcgill.ecse321.repairshop.repository.AdminRepository;
import ca.mcgill.ecse321.repairshop.repository.BusinessRepository;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;


@RestController
@SpringBootApplication
@EnableScheduling
public class RepairShopApplication {

    @Value("${spring.mail.username}")
    private String rootemail;

    @Value("${spring.mail.password}")
    private String rootpassword;

    @Autowired
    AdminRepository adminRepository;
    
    @Autowired
    BusinessRepository businessRepository;

    public static void main(String[] args) {
        SpringApplication.run(RepairShopApplication.class, args);
    }

    /**
     * When we start the application we want to make sure there is a business and one root admin.
     * You need an admin to create more admin / technicians.
     */
    @Bean
    InitializingBean sendDatabase() {
        return () -> {
            if (!adminRepository.findById(rootemail).isPresent()) {
                Admin rootAdmin = new Admin();
                rootAdmin.setEmail(rootemail);
                rootAdmin.setPassword(rootpassword);
                rootAdmin.setName("root");
                adminRepository.save(rootAdmin);
            }
            if (businessRepository.findAll().size() == 0) {
            	Business business = new Business();
            	businessRepository.save(business);
            }
        };
    }
}
