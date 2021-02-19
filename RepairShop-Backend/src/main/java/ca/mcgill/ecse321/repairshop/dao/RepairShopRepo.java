package ca.mcgill.ecse321.repairshop.dao;

import ca.mcgill.ecse321.repairshop.model.RepairShop;
import org.springframework.data.repository.CrudRepository;

public interface RepairShopRepo extends CrudRepository<RepairShop, Long> {
}