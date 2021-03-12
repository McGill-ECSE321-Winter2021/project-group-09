package ca.mcgill.ecse321.repairshop.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.mcgill.ecse321.repairshop.repository.AdminRepository;

@ExtendWith(MockitoExtension.class)
public class TestAdminService {
	
	@Mock
	private AdminRepository adminRepository;
	
	@InjectMocks
	private AdminService admin;
}
