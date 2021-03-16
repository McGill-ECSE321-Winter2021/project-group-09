package ca.mcgill.ecse321.repairshop.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.repairshop.dto.AppointmentDto;
import ca.mcgill.ecse321.repairshop.dto.CustomerDto;
import ca.mcgill.ecse321.repairshop.model.Appointment;
import ca.mcgill.ecse321.repairshop.model.Customer;
import ca.mcgill.ecse321.repairshop.model.Reminder;
import ca.mcgill.ecse321.repairshop.repository.AppointmentRepository;
import ca.mcgill.ecse321.repairshop.repository.CustomerRepository;
import ca.mcgill.ecse321.repairshop.repository.ReminderRepository;



@Service
public class CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private AppointmentRepository appointmentRepository;
	
	@Autowired
	private ReminderRepository reminderRepository;
	
	
	
	/**
	 * Method to create a customer account
	 * @param email of customer
	 * @param password of customer
	 * @param phone of customer
	 * @param name of customer
	 * @param address of customer
	 * @return a customer dto corresponding to the customer object just created
	 * @throws Exception if email/password is null or a customer already exists with given email
	 *
	 */
	@Transactional
	public CustomerDto createCustomer(String email, String password, String phone, String name, String address) throws Exception{
		
		if(email == null || password == null) {
			throw new Exception("Email or password cannot be empty.");
		}
		if(customerRepository.findCustomerByEmail(email) != null) {
			throw new Exception("Email is already taken.");
		}
		
		List<Appointment> apps = new ArrayList<>();
		List<Reminder> reminders = new ArrayList<>();
		
		Customer customer = new Customer();
		customer.setEmail(email);
		customer.setPassword(password);
		customer.setPhoneNumber(phone);
		customer.setName(name);
		customer.setAddress(address);
		customer.setAppointments(apps);
		customer.setReminders(reminders);
		
		customerRepository.save(customer);
		return customerToDto(customer);
	}
	
	

	/**
	 * Method to change password
	 * @param email of customer
	 * @param newPassword of customer
	 * @return a customer dto corresponding to the customer object that was just updated
	 * @throws Exception if email/new password is null or if no customer exists with given email
	 *
	 */
	@Transactional
	public CustomerDto changePassword(String email, String newPassword) throws Exception{
		
		if(email == null || newPassword == null) {
			throw new Exception("Email or new password cannot be empty.");
		}
		if(customerRepository.findCustomerByEmail(email) == null) {
			throw new Exception("Customer not found.");
		}
		
		Customer customer = customerRepository.findCustomerByEmail(email);
		customer.setPassword(newPassword);
		customerRepository.save(customer);
		return customerToDto(customer);
	}
	

	
	/**
	 * Method to get a customer by email
	 * @param email of customer
	 * @return the customer with the given email
	 * @throws Exception if email is null or if no customer exists with given email
	 *
	 */
	@Transactional
	public CustomerDto getCustomer(String email) throws Exception{
		
		if(email == null) {
			throw new Exception("Email cannot be empty.");
		}
		if(customerRepository.findCustomerByEmail(email) == null) {
			throw new Exception("Customer not found.");
		}
		
		Customer customer = customerRepository.findCustomerByEmail(email);
		return customerToDto(customer);
	}
	
	

	/**
	 * Method to delete a customer by email
	 * @param email of customer
	 * @throws Exception if email is null or if no customer exists with given email
	 * Deletes the customer account corresponding to the email provided
	 *
	 */
	@Transactional
	public String deleteCustomer(String email) throws Exception{
		if(email == null) {
			throw new Exception("Email cannot be empty.");
		}
		
		Customer customer = customerRepository.findCustomerByEmail(email);
		
		if(customer == null) {
			throw new Exception("Customer not found.");
		}
		
		//delete customer's appointments
		List<Appointment> customerApps = customer.getAppointments();
		for (Appointment customerApp : customerApps) {
			appointmentRepository.deleteById(customerApp.getAppointmentID());
		}
		
		//delete customer's reminders
		List<Reminder> customerReminders = customer.getReminders();
		for (Reminder customerReminder : customerReminders) {
			reminderRepository.deleteById(customerReminder.getReminderID());
		}
		
		customerRepository.deleteByEmail(email);
		return "Customer account with email " + email + " deleted.";
		
		
	}
	
	
	/**
	 * Method to convert a Customer to CustomerDto
	 * @param customer (Customer)
	 * @return a customer Dto corresponding to the customer domain object provided
	 *
	 */
	public static CustomerDto customerToDto(Customer customer) {
		CustomerDto customerDto = new CustomerDto();
		customerDto.setAddress(customer.getAddress());
		customerDto.setPhoneNumber(customer.getPhoneNumber());
		customerDto.setName(customer.getName());
		customerDto.setEmail(customer.getEmail());
		customerDto.setPassword(customer.getPassword());
		customerDto.setToken(customer.getToken());
		
		return customerDto;

	}


	/**
	 * Method to get all existing customers
	 * @return a list of all the existing customers as dtos
	 *
 	 */
	@Transactional
	public List<CustomerDto> getAllCustomers() {
		List<Customer> customers = customerRepository.findAll();
		List<CustomerDto> customerDtos = new ArrayList<>();
		for (Customer customer : customers) {
			customerDtos.add(customerToDto(customer));
		}
		return customerDtos;
	}
	
	
	
	
	
	
	//USE CASES


	/**
	 * View all the appointments of a customer by using their email.
	 * @param email of customer
	 * @return list of appointments (List<AppointmentDto>)
	 * @throws Exception if email is empty or the customer's email can't be found
	 */
	@Transactional
	public List<AppointmentDto> viewAppointments(String email) throws Exception{
		if(email == null) {
			throw new Exception("Email cannot be empty.");
		}
		
		
		List<AppointmentDto> appDtos = new ArrayList<>();
		Customer customer = customerRepository.findCustomerByEmail(email);
		if(customer == null) {
			throw new Exception("Customer not found.");
		}
		List<Appointment> customerAppointments = customer.getAppointments();

		for (Appointment customerAppointment : customerAppointments) {
			AppointmentDto thisAppDto = AppointmentService.appointmentToDto(customerAppointment);
			appDtos.add(thisAppDto);
		}
		
		return appDtos;
	}

}
