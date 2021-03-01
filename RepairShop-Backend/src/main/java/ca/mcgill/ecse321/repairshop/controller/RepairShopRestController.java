package ca.mcgill.ecse321.repairshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.repairshop.service.RepairShopService;

@CrossOrigin(origins = "*")
@RestController
public class RepairShopRestController {

	@Autowired
	private RepairShopService service;
}
