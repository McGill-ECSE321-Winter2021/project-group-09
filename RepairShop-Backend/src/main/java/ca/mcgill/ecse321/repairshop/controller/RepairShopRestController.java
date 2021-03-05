package ca.mcgill.ecse321.repairshop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/test")
public class RepairShopRestController {

	@GetMapping("/test")
	public ResponseEntity<?> getAll() {

			return new ResponseEntity<>("Test hello", HttpStatus.OK);
	}
}
