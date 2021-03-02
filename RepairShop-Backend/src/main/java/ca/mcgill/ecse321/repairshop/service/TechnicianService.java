package ca.mcgill.ecse321.repairshop.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Id;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.istack.NotNull;

import ca.mcgill.ecse321.repairshop.controller.TimeSlotService;
import ca.mcgill.ecse321.repairshop.dto.TechnicianDto;
import ca.mcgill.ecse321.repairshop.dto.TimeSlotDto;
import ca.mcgill.ecse321.repairshop.model.Technician;
import ca.mcgill.ecse321.repairshop.model.TimeSlot;
import ca.mcgill.ecse321.repairshop.repository.TechnicianRepository;


@Service
public class TechnicianService {
	
	@Autowired
	TechnicianRepository technicianRepository;
	
	@Autowired
	private TimeSlotService timeService;
	
	@Transactional
	public Technician createTechnician(String email, String password, String phone, String name, String address) throws Exception{
		
		if(email == null || password == null) {
			throw new Exception("Email or password cannot be empty.");
		}
		if(technicianRepository.findTechnicianByEmail(email) != null) {
			throw new Exception("Email is already taken.");
		}
		
		Technician tech = new Technician();
		tech.setEmail(email);
		tech.setPassword(password);
		tech.setPhoneNumber(phone);
		tech.setName(name);
		tech.setAddress(address);
		
		technicianRepository.save(tech);
		return tech;
	}
	
	
	@Transactional
	public Technician changePassword(String email, String newPassword) {
		
		if(email == null || newPassword == null) {
			throw new Exception("Email or new password cannot be empty.");
		}
		if(technicianRepository.findTechnicianByEmail(email) == null) {
			throw new Exception("Technician not found.");
		}
		
		Technician tech = technicianRepository.findTechnicianByEmail(email);
		tech.setPassword(newPassword);
		technicianRepository.save(tech);
		return tech;
	}
	

	
	@Transactional
	public Technician getTechnician(String email) {
		
		if(email == null) {
			throw new Exception("Email cannot be empty.");
		}
		if(technicianRepository.findTechnicianByEmail(email) == null) {
			throw new Exception("Technician not found.");
		}
		
		Technician tech = technicianRepository.findTechnicianByEmail(email);
		return tech;
	}
	
	
	@Transactional
	public TechnicianDto technicianToDTO(Technician tech) {
		TechnicianDto techDTO = new TechnicianDto();
		techDTO.setAddress(tech.getAddress());
		techDTO.setPhoneNumber(tech.getPhoneNumber());
		techDTO.setName(tech.getName());
		techDTO.setEmail(tech.getEmail());
		
		
		List<TimeSlot> timeSlots = tech.getTimeslots();
		ArrayList<TimeSlotDto> timeDto = new ArrayList<>();
		for(TimeSlot timeSlot : timeSlots) {
			timeDto.add(timeslotToDTO(timeSlot));
		}
		techDTO.setTimeSlots(timeDto);
		
		//TODO (?)
		//Add a list of appointments to TechnicianDTO
		
		return techDTO;
		
	}

	@Transactional
	public List<Technician> getAllTechnicians() {
		 return technicianRepository.findAll();
	}
	
	
	/*
	
	//TODO
	//this needs to be reviewed
	
	@Transactional
	public List<TimeSlot> viewSchedule(String email){
		
		Technician tech = technicianRepository.findTechnicianByEmail(email);
		List<TimeSlot> timeSlots = tech.getTimeslots();
		return timeSlots;
		
	}
	
	*/

}
