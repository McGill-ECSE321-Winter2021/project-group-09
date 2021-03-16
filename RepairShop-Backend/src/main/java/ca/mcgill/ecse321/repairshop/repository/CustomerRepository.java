package ca.mcgill.ecse321.repairshop.repository;

import ca.mcgill.ecse321.repairshop.model.Customer;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, String> {

    Customer findCustomerByEmail(String email);

    
    List<Customer> findAll();	
    
    void deleteByEmail(String email);

    Customer findCustomerByToken(String token);

}

