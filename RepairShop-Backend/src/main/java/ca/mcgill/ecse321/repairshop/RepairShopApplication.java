package ca.mcgill.ecse321.repairshop;

import ca.mcgill.ecse321.repairshop.model.Admin;
import ca.mcgill.ecse321.repairshop.repository.AdminRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;


@RestController
@SpringBootApplication
@EnableScheduling
public class RepairShopApplication {

    @Autowired
    AdminRepository adminRepository;

    public static void main(String[] args) {
        SpringApplication.run(RepairShopApplication.class, args);
    }

    @Bean
    InitializingBean sendDatabase() {
        return () -> {
            if (!adminRepository.findById("${spring.mail.username}").isPresent()) {
                Admin rootAdmin = new Admin();
                rootAdmin.setEmail("${spring.mail.username}");
                rootAdmin.setPassword("${spring.mail.password}");
                rootAdmin.setName("root");
                adminRepository.save(rootAdmin);
            }
        };
    }
}
