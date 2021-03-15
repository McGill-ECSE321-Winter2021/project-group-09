package ca.mcgill.ecse321.repairshop;

import ca.mcgill.ecse321.repairshop.model.Admin;
import ca.mcgill.ecse321.repairshop.repository.AdminRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class RepairShopApplication {

    @Value("${spring.mail.username}")
    private String rootemail;

    @Value("${spring.mail.password}")
    private String rootpassword;

    @Autowired
    AdminRepository adminRepository;

    public static void main(String[] args) {
        SpringApplication.run(RepairShopApplication.class, args);
    }

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
        };
    }
}
