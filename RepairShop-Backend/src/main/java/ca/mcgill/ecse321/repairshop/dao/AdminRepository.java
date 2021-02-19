package ca.mcgill.ecse321.repairshop.dao;

import ca.mcgill.ecse321.repairshop.model.Admin;
import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<Admin, Long> {

}
