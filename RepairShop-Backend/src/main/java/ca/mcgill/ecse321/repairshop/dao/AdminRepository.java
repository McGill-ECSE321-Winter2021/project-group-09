package ca.mcgill.ecse321.repairshop.dao;

import ca.mcgill.ecse321.repairshop.model.User;
import org.springframework.data.repository.CrudRepository;

public interface AdminRepository<Admin extends User> extends CrudRepository<Admin, Long> {

    Admin findAdminByAdminId(Long adminId);

}
