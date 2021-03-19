package ca.mcgill.ecse321.repairshop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*")
@RestController
public class RepairShopController {

	@GetMapping("/")
	public ResponseEntity<?> getAll() {
			return new ResponseEntity<>("Hello World", HttpStatus.OK);
	}
}
