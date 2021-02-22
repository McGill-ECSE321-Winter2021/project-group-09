package ca.mcgill.ecse321.repairshop.repository;

import ca.mcgill.ecse321.repairshop.model.Admin;
import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<Admin, String> {
	
	Admin findAdminByEmail(String email);

}

