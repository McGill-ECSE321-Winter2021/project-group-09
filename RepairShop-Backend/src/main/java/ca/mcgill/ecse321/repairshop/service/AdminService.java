package ca.mcgill.ecse321.repairshop.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import ca.mcgill.ecse321.repairshop.service.utilities.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.repairshop.dto.AdminDto;
import ca.mcgill.ecse321.repairshop.model.Admin;
import ca.mcgill.ecse321.repairshop.repository.AdminRepository;


@Service
public class AdminService {

	@Autowired
	AdminRepository adminRepository;

	@Autowired
	TokenProvider tokenProvider;
	
	@Transactional
	public AdminDto createAdmin(String email, String password, String phone, String name, String address) throws Exception {
		
		if (email == null || password == null || phone == null || name == null || address == null) {
			throw new Exception("Admin must have a name, email, password, address, and phone in order to be registered.");
		}
		if (adminRepository.findAdminByEmail(email) != null) {
			throw new Exception("Cannot create admin because email is already taken.");
		}
		
		Admin admin = new Admin();
		admin.setEmail(email);
		admin.setPassword(password);
		admin.setPhoneNumber(phone);
		admin.setName(name);
		admin.setAddress(address);
		adminRepository.save(admin);
		return adminToDto(admin);
	}

	@Transactional 
	public String deleteAdmin(String email) throws Exception{
		if(email == null) {
			throw new Exception("Email cannot be empty.");
		}
		if(adminRepository.findAdminByEmail(email) == null) {
			throw new Exception("Admin not found.");
		}
		
		adminRepository.deleteAdminByEmail(email);
		return "Admin account with email " + email + " was successfully deleted.";
	}
	
	@Transactional
	public AdminDto getAdmin(String email) throws Exception{
		
		if(email == null) {
			throw new Exception("Email cannot be empty.");
		}
		if(adminRepository.findAdminByEmail(email) == null) {
			throw new Exception("Admin not found.");
		}
		
		Admin admin = adminRepository.findAdminByEmail(email);
		return adminToDto(admin);
	}
	
	@Transactional
	public List<AdminDto> getAllAdmins() {
		List<Admin> admins = adminRepository.findAll();
		List<AdminDto> adminDtos = new ArrayList<>();
		
		for (Admin admin: admins) {
			adminDtos.add(adminToDto(admin));
		}
		return adminDtos;
	}
	
	@Transactional
	public AdminDto changeAddress(String email, String newAddress) throws Exception{
		
		if(email == null || newAddress == null) {
			throw new Exception("Email or new address cannot be empty.");
		}
		if(adminRepository.findAdminByEmail(email) == null) {
			throw new Exception("Admin not found.");
		}
		
		Admin admin = adminRepository.findAdminByEmail(email);
		admin.setAddress(newAddress);
		adminRepository.save(admin);
		return adminToDto(admin);
	}
	
	@Transactional
	public AdminDto changePhone(String email, String newPhone) throws Exception{
		
		if(email == null || newPhone == null) {
			throw new Exception("Email or new phone cannot be empty.");
		}
		if(adminRepository.findAdminByEmail(email) == null) {
			throw new Exception("Admin not found.");
		}
		
		Admin admin = adminRepository.findAdminByEmail(email);
		admin.setPhoneNumber(newPhone);
		adminRepository.save(admin);
		return adminToDto(admin);
	}
	
	@Transactional
	public AdminDto changePassword(String email, String newPassword) throws Exception{
		
		if (email == null || newPassword == null) {
			throw new Exception("Email or new password cannot be empty.");
		}
		if(adminRepository.findAdminByEmail(email) == null) {
			throw new Exception("Admin not found.");
		}
		
		Admin admin = adminRepository.findAdminByEmail(email);
		admin.setPassword(newPassword);
		adminRepository.save(admin);
		return adminToDto(admin);
	}
	
	@Transactional
	public AdminDto changeName(String email, String newName) throws Exception{
		
		if (email == null || newName == null) {
			throw new Exception("Email or new name cannot be empty.");
		}
		if(adminRepository.findAdminByEmail(email) == null) {
			throw new Exception("Admin not found.");
		}
		
		Admin admin = adminRepository.findAdminByEmail(email);
		admin.setName(newName);
		adminRepository.save(admin);
		return adminToDto(admin);
	}

	/**
	 * Convert Admin to AdminDto
	 * @param admin (Admin)
	 * @return adminDTO (AdminDto)
	 */
	public static AdminDto adminToDto(Admin admin) {
		AdminDto adminDTO = new AdminDto();
		
		adminDTO.setAddress(admin.getAddress());
		adminDTO.setPhoneNumber(admin.getPhoneNumber());
		adminDTO.setName(admin.getName());
	    adminDTO.setEmail(admin.getEmail());
		adminDTO.setPassword(admin.getPassword());
		adminDTO.setToken(admin.getToken());
		
		return adminDTO;
	}

}
