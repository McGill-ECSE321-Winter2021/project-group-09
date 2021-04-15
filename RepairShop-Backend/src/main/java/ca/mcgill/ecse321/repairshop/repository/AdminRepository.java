package ca.mcgill.ecse321.repairshop.repository;

import ca.mcgill.ecse321.repairshop.model.Admin;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<Admin, String> {
	
	/**
	 * Find an admin by email
	 * @param email
	 * @return admin (Admin)
	 */
	Admin findAdminByEmail(String email);
	
	/**
	 * Delete the admin with the provided email
	 * @param email
	 */
	void deleteAdminByEmail(String email);

	/**
	 * Find an admin by token
	 * @param token
	 * @return admin
	 */
	Admin findAdminByToken(String token);
	
	/**
	 * Find all admins
	 */
    List<Admin> findAll();	

}

