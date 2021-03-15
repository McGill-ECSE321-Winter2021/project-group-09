package ca.mcgill.ecse321.repairshop.repository;

import ca.mcgill.ecse321.repairshop.model.Admin;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<Admin, String> {
	
	Admin findAdminByEmail(String email);
	
	void deleteAdminByEmail(String email);

	Admin findAdminByToken(String token);
	
    List<Admin> findAll();	

}

