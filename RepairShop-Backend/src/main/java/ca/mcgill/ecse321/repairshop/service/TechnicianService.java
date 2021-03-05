


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

//import ca.mcgill.ecse321.repairshop.controller.TimeSlotService;
import ca.mcgill.ecse321.repairshop.dto.TechnicianDto;
import ca.mcgill.ecse321.repairshop.dto.TimeSlotDto;
import ca.mcgill.ecse321.repairshop.model.Technician;
import ca.mcgill.ecse321.repairshop.model.TimeSlot;
import ca.mcgill.ecse321.repairshop.repository.TechnicianRepository;
import static  ca.mcgill.ecse321.repairshop.service.TimeSlotService.timeslotToDTO;

@Service
public class TechnicianService {
	
	@Autowired
	TechnicianRepository technicianRepository;
	
	//@Autowired
	//private TimeSlotService timeService;
	
	@Transactional
	public TechnicianDto createTechnician(String email, String password, String phone, String name, String address) throws Exception{
		
		if(email == null || password == null) {
			throw new Exception("Email or password cannot be empty.");
		}
		if(technicianRepository.findTechnicianByEmail(email) != null) {
			throw new Exception("Email is already taken.");
		}
		
		/*
		//convert dto work-hours to TimeSlot work-hours
		List<TimeSlot> timeSlots = new ArrayList<>();
		for(int i = 0; i < workHours.size(); i++) {
			timeSlots.add(DtoToTimeslot(workHours.get(i)));
		}
		*/
		
		List<TimeSlot> timeSlots = new ArrayList<>();
		Technician tech = new Technician();
		tech.setEmail(email);
		tech.setPassword(password);
		tech.setPhoneNumber(phone);
		tech.setName(name);
		tech.setAddress(address);
		tech.setTimeslots(timeSlots);
		
		technicianRepository.save(tech);
		return technicianToDTO(tech);
	}
	
	
	@Transactional
	public TechnicianDto changePassword(String email, String newPassword) throws Exception{
		
		if(email == null || newPassword == null) {
			throw new Exception("Email or new password cannot be empty.");
		}
		if(technicianRepository.findTechnicianByEmail(email) == null) {
			throw new Exception("Technician not found.");
		}
		
		Technician tech = technicianRepository.findTechnicianByEmail(email);
		tech.setPassword(newPassword);
		technicianRepository.save(tech);
		return technicianToDTO(tech);
	}
	

	
	@Transactional
	public TechnicianDto getTechnician(String email) throws Exception{
		
		if(email == null) {
			throw new Exception("Email cannot be empty.");
		}
		if(technicianRepository.findTechnicianByEmail(email) == null) {
			throw new Exception("Technician not found.");
		}
		
		Technician tech = technicianRepository.findTechnicianByEmail(email);
		return technicianToDTO(tech);
	}
	
	
	@Transactional 
	public void deleteTechnician(String email) throws Exception{
		if(email == null) {
			throw new Exception("Email cannot be empty.");
		}
		if(technicianRepository.findTechnicianByEmail(email) == null) {
			throw new Exception("Technician not found.");
		}
		
		technicianRepository.deleteByEmail(email);
	}
	
	
	
	@Transactional
	public TechnicianDto technicianToDTO(Technician tech) {
		TechnicianDto techDTO = new TechnicianDto();
		techDTO.setAddress(tech.getAddress());
		techDTO.setPhoneNumber(tech.getPhoneNumber());
		techDTO.setName(tech.getName());
		techDTO.setEmail(tech.getEmail());
		techDTO.setSetPassword(tech.getPassword());
		
		List<TimeSlot> timeSlots = tech.getTimeslots();
		ArrayList<TimeSlotDto> timeDtos = new ArrayList<>();
		for(int i = 0; i < timeSlots.size(); i++) {
//			timeDtos.add(timeslotToDTO(timeSlots.get(i)));
		}
		techDTO.setTimeSlots(timeDtos);
		
		
		return techDTO;
		
	}

	@Transactional
	public List<TechnicianDto> getAllTechnicians() {
		
		List<Technician> technicians = technicianRepository.findAll();
		List<TechnicianDto> techDtos = new ArrayList<>();
		for (Technician tech : technicians) {
			techDtos.add(technicianToDTO(tech));
		}
		return techDtos;
		
	}
	
	
	//THESE ARE REQUIRED TO RUN TECHNICIAN SERVICE METHODS.
	//COMMENTED BECAUSE NOT SURE IF THESE WERE ALREADY IMPLEMENTED IN TIMESLOT SERVICES
	
	/*
	 public TimeSlotDto timeslotToDTO(TimeSlot timeslot) {
	     TimeSlotDto dto = new TimeSlotDto();
	     dto.setEndDateTime(timeslot.getEndDateTime());
	     dto.setStartDateTime(timeslot.getStartDateTime());
	     return dto;
	 }
	 
	 
	 private TimeSlot DtoToTimeslot(TimeSlotDto dto) {
		 TimeSlot time = new TimeSlot();
		 time.setStartDateTime(dto.getStartDateTime());
		 time.setEndDateTime(dto.getEndDateTime());
		 return time;
	 }
	 
	*/
	
}




