package ca.mcgill.ecse321.repairshop.repository;

import ca.mcgill.ecse321.repairshop.model.RepairShop;
import org.springframework.data.repository.CrudRepository;

public interface RepairShopRepository extends CrudRepository<RepairShop, Long> {
	
	RepairShop findRepairShopByRepairShopID(Long repairShopID);
	
}


