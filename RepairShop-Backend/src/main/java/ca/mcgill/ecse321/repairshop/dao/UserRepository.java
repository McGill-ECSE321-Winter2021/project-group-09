package ca.mcgill.ecse321.repairshop.dao;

import ca.mcgill.ecse321.repairshop.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findUserByEmail(String email);

    User findUserByName(String name);

}