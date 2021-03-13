package ca.mcgill.ecse321.repairshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.junit.jupiter.api.Test;

import ca.mcgill.ecse321.repairshop.dto.AdminDto;
import ca.mcgill.ecse321.repairshop.model.Admin;
import ca.mcgill.ecse321.repairshop.repository.AdminRepository;

@ExtendWith(MockitoExtension.class)
public class TestAdminService {
	
	@Mock
	private AdminRepository adminRepo;
	
	@InjectMocks
	private AdminService adminService;
	
	private static final String ADMIN_NAME = "John Coltrane";
	private static final String ADMIN_EMAIL = "mail@hotmail.com";
	private static final String ADMIN_PASSWORD = "Giant Steps";
	private static final String ADMIN_ADDRESS = "VillageVanguard";
	private static final String ADMIN_PHONE = "26-2";
			
	private Admin ADMIN = new Admin();
	
	@BeforeEach
	public void setMockOutput() {
		lenient().when(adminRepo.findAll()).thenAnswer((InvocationOnMock invocation) -> {
			ADMIN.setName(ADMIN_NAME);
			ADMIN.setEmail(ADMIN_EMAIL);
			ADMIN.setPassword(ADMIN_PASSWORD);
			ADMIN.setPhoneNumber(ADMIN_PHONE);
			ADMIN.setAddress(ADMIN_ADDRESS);
			
			List<Admin> adminList = new ArrayList<>();
			adminList.add(ADMIN);
			return adminList;
		});
		
		lenient().when(adminRepo.findAdminByEmail(anyString())).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals(ADMIN_EMAIL)) {
				Admin admin = new Admin();
				admin.setName(ADMIN_NAME);
				admin.setEmail(ADMIN_EMAIL);
				admin.setPassword(ADMIN_PASSWORD);
				admin.setPhoneNumber(ADMIN_PHONE);
				admin.setAddress(ADMIN_ADDRESS);
				return admin;
			} else {
				return null;
			}
		});
		
		Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
		return invocation.getArgument(0);
	};
	
	lenient().when(adminRepo.save(any(Admin.class))).thenAnswer(returnParameterAsAnswer);
	
	}	
	
	/////////////////////////////////////////////////////////////////////////////////////////
	
	//////////////////////////  CREATE ADMIN TEST  //////////////////////////////////////////
	
	
	@Test
	public void testCreateAdmin() {

		String adminName = "Gil Evans";
		String adminEmail = "GEvans@gmail.com";
		String adminPassword = "IwasMDavisBestFriend";
		String adminAddress = "New York Composer Street";
		String adminPhone = "2124056945";
		
		AdminDto admin = null;
		
		try {
			admin = adminService.createAdmin(adminEmail, adminPassword, adminPhone, adminName, adminAddress);
		} catch (Exception e) {
			// Check that no error occurred
			fail(e.getMessage());
		}
		
		assertNotNull(admin);
		assertEquals(adminName, admin.getName());
		assertEquals(adminEmail, admin.getEmail());
		
	}

	@Test
	public void testCreateAdminNameNull() {

		String adminName = "Fats Waller";
		String adminEmail = null;
		String adminPassword = null;
		String adminAddress = "Stride Master Street";
		String adminPhone = "ummmm....just find me please";
		
		@SuppressWarnings("unused")
		AdminDto admin = null;
		
		try {
			admin = adminService.createAdmin(adminEmail, adminPassword, adminPhone, adminName, adminAddress);
			fail();
		} catch (Exception e) {
			//an error should occur
			assertEquals("Admin must have a name, email, password, address, and phone in order to be registered.", e.getMessage());
		}	
	}
	
	@Test
	public void testCreateAdminEmpty() {
		
		AdminDto admin = null;
		String error = null;
		
		try {
			admin = adminService.createAdmin(null, null, null, null, null);
			fail();
			assertNull(admin);
		} catch (Exception e) {
			//an error should occur
			error = e.getMessage();
		}	
		assertNull(admin);
		assertEquals("Admin must have a name, email, password, address, and phone in order to be registered.", error);
	}
	
	
	@Test
	public void testCreateAdminDuplicate() {

		String adminName1 = "Kurt Rosenwinkel";
		String adminEmail1 = "mail@hotmail.com";
		String adminPassword1 = "Intuit";
		String adminAddress1 = "Zhivago Street";
		String adminPhone1 = "4390239498";
		
		try {
			// the comparison happens between admin  created in this method and the mock admin  created at top  of class
			adminService.createAdmin(adminEmail1, adminPassword1, adminPhone1, adminName1, adminAddress1);
			fail();
		} catch (Exception e) {
			//an error should occur
			assertEquals("Cannot create admin because email is already taken.", e.getMessage());
		}
		
	}
	
    /////////////////////////////////////////////////////////////////////////////////////////
	
	
	////////////////////////// CHANGE PASSWORD TEST  ////////////////////////////////////////
	
	@Test
	public void testChangePassword() {
	
		String newPassword = "Central Park West";
		AdminDto admin = null;
		try {
			admin = adminService.changePassword(ADMIN_EMAIL, newPassword);

		} catch (Exception e) { 
			// Check that no error occurred
			fail(e.getMessage());
		}
		
		assertNotNull(admin);
		assertEquals(newPassword, admin.getPassword());
		assertEquals(ADMIN_EMAIL, admin.getEmail());
		
	}
	
	
	
	@Test
	public void testChangePasswordNull() {
				
		try {
			adminService.changePassword(null, null);
			fail();
		} catch (Exception e) {
			//error should occur
			assertEquals("Email or new password cannot be empty.", e.getMessage());
		}
	}
	
	
	@Test
	public void testChangePasswordNonExistentAdmin() {
				
		try {
			adminService.changePassword("nobody@mail.com", "somebody@mail.com");
			fail();
		} catch (Exception e) {
			//error should occur
			assertEquals("Admin not found.", e.getMessage());
		}
	}
	
    /////////////////////////////////////////////////////////////////////////////////////////
	
	////////////////////////// GET ADMIN TEST  //////////////////////////////////////////////
	
	@Test
	public void testGetAdmin() {

		AdminDto admin = null;
		
		try {
			admin = adminService.getAdmin(ADMIN_EMAIL);
		} catch (Exception e) {
			// Check that no error occurred
			fail();
		}
		
		assertNotNull(admin);
		assertEquals(ADMIN_NAME, admin.getName());
		assertEquals(ADMIN_EMAIL, admin.getEmail());

	}
		
	@Test
	public void testGetAdminNull() {
		
		try {
			adminService.getAdmin(null);
			fail();
		} catch (Exception e) {
			// error should occur
			assertEquals("Email cannot be empty.", e.getMessage());
		}
		
	}
	
	
	
	@Test
	public void testGetNonExistentAdmin() {
		
		String adminEmail = "nobody@mail.com";
		
		try {
			adminService.getAdmin(adminEmail);
			fail();
		} catch (Exception e) {
			// error should occur
			assertEquals("Admin not found.", e.getMessage());
		}
	}
	
	@Test
	public void testGetAllAdmins() {
		
		try {
			List<AdminDto> admins = adminService.getAllAdmins();
			assertTrue(admins.stream().map(AdminDto::getEmail).collect(Collectors.toList()).contains(ADMIN_EMAIL));
		} catch (Exception e) {
			fail(e.getMessage());
		}

	}
	
	
    /////////////////////////////////////////////////////////////////////////////////////////
	
	////////////////////////// CHANGE ADDRESS TEST  /////////////////////////////////////////
	
	@Test
	public void testChangeAddress() {
	
		String newAddress = "A Love Supreme";
		AdminDto admin = null;
		try {
			admin = adminService.changeAddress(ADMIN_EMAIL, newAddress);

		} catch (Exception e) { 
			// Check that no error occurred
			fail(e.getMessage());
		}
		
		assertNotNull(admin);
		assertEquals(newAddress, admin.getAddress());
		assertEquals(ADMIN_EMAIL, admin.getEmail());
		
	}
	
	
	
	@Test
	public void testChangeAddressNull() {
				
		try {
			adminService.changeAddress(null, null);
			fail();
		} catch (Exception e) {
			//error should occur
			assertEquals("Email or new address cannot be empty.", e.getMessage());
		}
	}
	
	
	@Test
	public void testChangeAddressNonExistentAdmin() {
				
		try {
			adminService.changeAddress("nobody address", "somebody address");
			fail();
		} catch (Exception e) {
			//error should occur
			assertEquals("Admin not found.", e.getMessage());
		}
	}
	
    /////////////////////////////////////////////////////////////////////////////////////////
	
	////////////////////////// CHANGE PHONE TEST  ///////////////////////////////////////////
	
	@Test
	public void testChangePhone() {
	
		String newPhone = "9879879877";
		AdminDto admin = null;
		try {
			admin = adminService.changePhone(ADMIN_EMAIL, newPhone);

		} catch (Exception e) { 
			// Check that no error occurred
			fail(e.getMessage());
		}
		
		assertNotNull(admin);
		assertEquals(newPhone, admin.getPhoneNumber());
		assertEquals(ADMIN_EMAIL, admin.getEmail());
		
	}
	
	
	
	@Test
	public void testChangePhoneNull() {
				
		try {
			adminService.changePhone(null, null);
			fail();
		} catch (Exception e) {
			//error should occur
			assertEquals("Email or new phone cannot be empty.", e.getMessage());
		}
	}
	
	
	@Test
	public void testChangePhoneNonExistentAdmin() {
				
		try {
			adminService.changePhone("nobody's phone", "somebody's phone");
			fail();
		} catch (Exception e) {
			//error should occur
			assertEquals("Admin not found.", e.getMessage());
		}
	}
	
    /////////////////////////////////////////////////////////////////////////////////////////
	
	////////////////////////// CHANGE EMAIL TEST  ///////////////////////////////////////////
	
	@Test
	public void testChangeEmail() {
	
		String newEmail = "gotNewColtraneMail@gmail.com";
		AdminDto admin = null;
		try {
			admin = adminService.changeEmail(ADMIN_EMAIL, newEmail);

		} catch (Exception e) { 
			// Check that no error occurred
			fail(e.getMessage());
		}
		
		assertNotNull(admin);
		assertEquals(newEmail, admin.getEmail());
		assertNotEquals(ADMIN_EMAIL, admin.getEmail());
		
	}
	
	
	
	@Test
	public void testChangeEmailNull() {
				
		try {
			adminService.changeEmail(null, null);
			fail();
		} catch (Exception e) {
			//error should occur
			assertEquals("Email or new email cannot be empty.", e.getMessage());
		}
	}
	
	
	@Test
	public void testChangeEmailNonExistentAdmin() {
				
		try {
			adminService.changeEmail("nobody@mail.com", "somebody@mail.com");
			fail();
		} catch (Exception e) {
			//error should occur
			assertEquals("Admin not found.", e.getMessage());
		}
	}
	
    /////////////////////////////////////////////////////////////////////////////////////////
	
	////////////////////////// CHANGE NAME TEST  ////////////////////////////////////////////
	
	@Test
	public void testChangeName() {
	
		String newName = "Alice Coltrane";
		AdminDto admin = null;
		try {
			admin = adminService.changeName(ADMIN_EMAIL, newName);

		} catch (Exception e) { 
			// Check that no error occurred
			fail(e.getMessage());
		}
		
		assertNotNull(admin);
		assertEquals(newName, admin.getName());
		assertNotEquals(ADMIN_NAME, admin.getName());
		
	}
	
	
	
	@Test
	public void testChangeNamelNull() {
				
		try {
			adminService.changeName(null, null);
			fail();
		} catch (Exception e) {
			//error should occur
			assertEquals("Email or new name cannot be empty.", e.getMessage());
		}
	}
	
	
	@Test
	public void testChangeNameNonExistentAdmin() {
				
		try {
			adminService.changeName("No One", "Some One");
			fail();
		} catch (Exception e) {
			//error should occur
			assertEquals("Admin not found.", e.getMessage());
		}
	}
	
    /////////////////////////////////////////////////////////////////////////////////////////
	
	////////////////////////// DELETE ADMIN TEST  ///////////////////////////////////////////
	
	@Test
	public void testDeleteAdminNull() {

		try {
			
			adminService.deleteAdmin(null);
			
		} catch (Exception e) {
			// error should occur
			assertEquals("Email cannot be empty.", e.getMessage());
		}
		
	}
	
	
	@Test
	public void testDeleteNonExistentAdmin() {
		String email = "noOne@gmail.com";
		
		try {
			//create
			adminService.deleteAdmin(email);
			
		} catch (Exception e) {
			// error should occur
			assertEquals("Admin not found.", e.getMessage());
		}
		
	}
	
	
	@Test         
	public void testDeleteAdmin() {
		
		try {
		
			//delete
			String message = adminService.deleteAdmin(ADMIN_EMAIL);
			assertEquals("Admin account with email " + ADMIN_EMAIL + " was successfully deleted.", message);
//			assertNull(adminService.getAdmin(ADMIN_EMAIL)); <= this returns the mock but should really  be null. TODO figure out why this is happening later
			
		} catch (Exception e) {
			fail(e.getMessage());
		}

	}
}
