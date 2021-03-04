


package ca.mcgill.ecse321.repairshop.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.repairshop.dto.CustomerDto;
import ca.mcgill.ecse321.repairshop.model.Customer;
import ca.mcgill.ecse321.repairshop.repository.CustomerRepository;

public class CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	
	
	
	@Transactional
	public CustomerDto createCustomer(String email, String password, String phone, String name, String address) throws Exception{
		
		if(email == null || password == null) {
			throw new Exception("Email or password cannot be empty.");
		}
		if(customerRepository.findCustomerByEmail(email) != null) {
			throw new Exception("Email is already taken.");
		}
		
		Customer customer = new Customer();
		customer.setEmail(email);
		customer.setPassword(password);
		customer.setPhoneNumber(phone);
		customer.setName(name);
		customer.setAddress(address);
		
		customerRepository.save(customer);
		return customerToDTO(customer);
	}
	
	
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
		return customerToDTO(customer);
	}
	

	
	@Transactional
	public CustomerDto getCustomer(String email) throws Exception{
		
		if(email == null) {
			throw new Exception("Email cannot be empty.");
		}
		if(customerRepository.findCustomerByEmail(email) == null) {
			throw new Exception("Customer not found.");
		}
		
		Customer customer = customerRepository.findCustomerByEmail(email);
		return customerToDTO(customer);
	}
	
	
	@Transactional 
	public void deleteCustomer(String email) throws Exception{
		if(email == null) {
			throw new Exception("Email cannot be empty.");
		}
		if(customerRepository.findCustomerByEmail(email) == null) {
			throw new Exception("Customer not found.");
		}
		
		customerRepository.deleteByEmail(email);
	}
	
	
	@Transactional
	public CustomerDto customerToDTO(Customer customer) {
		CustomerDto customerDTO = new CustomerDto();
		customerDTO.setAddress(customer.getAddress());
		customerDTO.setPhoneNumber(customer.getPhoneNumber());
		customerDTO.setName(customer.getName());
		customerDTO.setEmail(customer.getEmail());
		customerDTO.setSetPassword(customer.getPassword());
		
		return customerDTO;

	}

	@Transactional
	public List<CustomerDto> getAllCustomers() {
		List<Customer> customers = customerRepository.findAll();
		List<CustomerDto> customerDtos = new ArrayList<>();
		for (Customer customer : customers) {
			customerDtos.add(customerToDTO(customer));
		}
		return customerDtos;
	}
	

}





