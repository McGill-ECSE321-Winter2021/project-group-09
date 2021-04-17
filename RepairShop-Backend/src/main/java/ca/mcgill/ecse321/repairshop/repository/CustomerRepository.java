package ca.mcgill.ecse321.repairshop.repository;

import ca.mcgill.ecse321.repairshop.model.Customer;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, String> {

	/**
	 * Find a customer by email
	 * @param email
	 * @return customer (Customer)
	 */
    Customer findCustomerByEmail(String email);
    
    /**
     * @return all customers
     */
    List<Customer> findAll();	
    
    /**
     * Delete a customer by email
     * @param email
     */
    void deleteByEmail(String email);

    /**
     * Find a customer by token
     * @param token
     * @return
     */
    Customer findCustomerByToken(String token);

}

