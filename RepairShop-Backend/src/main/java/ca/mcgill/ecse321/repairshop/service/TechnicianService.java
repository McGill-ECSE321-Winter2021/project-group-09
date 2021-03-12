


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

import ca.mcgill.ecse321.repairshop.dto.AppointmentDto;
//import ca.mcgill.ecse321.repairshop.controller.TimeSlotService;
import ca.mcgill.ecse321.repairshop.dto.TechnicianDto;
import ca.mcgill.ecse321.repairshop.dto.TimeSlotDto;
import ca.mcgill.ecse321.repairshop.model.Appointment;
import ca.mcgill.ecse321.repairshop.model.Reminder;
import ca.mcgill.ecse321.repairshop.model.Technician;
import ca.mcgill.ecse321.repairshop.model.TimeSlot;
import ca.mcgill.ecse321.repairshop.repository.AppointmentRepository;
import ca.mcgill.ecse321.repairshop.repository.TechnicianRepository;
import ca.mcgill.ecse321.repairshop.repository.TimeSlotRepository;

import static  ca.mcgill.ecse321.repairshop.service.TimeSlotService.timeslotToDTO;

@Service
public class TechnicianService {
	
	@Autowired
	TechnicianRepository technicianRepository;
	
	@Autowired
	AppointmentRepository appRepo;
	
	
	/**
	 * Method to create a technician account
	 * @param email
	 * @param password
	 * @param phone
	 * @param name
	 * @param address
	 * @return a technician dto corresponding to the technician object just created
	 * @throws Exception if email/password is null or a technician already exists with given email
	 * 
	 */
	@Transactional
	public TechnicianDto createTechnician(String email, String password, String phone, String name, String address) throws Exception{
		
		if(email == null || password == null) {
			throw new Exception("Email or password cannot be empty.");
		}
		if(technicianRepository.findTechnicianByEmail(email) != null) {
			throw new Exception("Email is already taken.");
		}
		
		
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
	
	
	
	/**
	 * Method to change password
	 * @param email
	 * @param newPassword
	 * @return a technician dto corresponding to the technician object that was just updated
	 * @throws Exception if email/new password is null or if no technician exists with given email
	 * 
	 */
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
	

	
	/**
	 * Method to get a technician by email
	 * @param email
	 * @return the technician with the given email
	 * @throws Exception if email is null of if no technician exists with given email
	 */
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
	
	
	
	/**
	 * Method to delete a technician by email
	 * @param email
	 * @throws Exception
	 * Deletes the technician account with the given email
	 * 
	 */
	@Transactional 
	public String deleteTechnician(String email) throws Exception{
		if(email == null) {
			throw new Exception("Email cannot be empty.");
		}
		
		Technician tech = technicianRepository.findTechnicianByEmail(email);
		
		if(tech == null) {
			throw new Exception("Technician not found.");
		}
				
		//delete technicians's work hours
		tech.setTimeslots(null);
		
		technicianRepository.deleteByEmail(email);
		return "Technician account with email " + email + " deleted.";
	}
	
	
	/**
	 * Method to convert Technician to TechnicianDto
	 * @param tech
	 * @return a technician dto corresponding to the technician domain object provided
	 * 
	 */
	@Transactional
	public static TechnicianDto technicianToDTO(Technician tech) {
		TechnicianDto techDTO = new TechnicianDto();
		techDTO.setAddress(tech.getAddress());
		techDTO.setPhoneNumber(tech.getPhoneNumber());
		techDTO.setName(tech.getName());
		techDTO.setEmail(tech.getEmail());
		techDTO.setSetPassword(tech.getPassword());
		
		List<TimeSlot> timeSlots = tech.getTimeslots();
		if(timeSlots != null) {
			ArrayList<TimeSlotDto> timeDtos = new ArrayList<>();
			for(int i = 0; i < timeSlots.size(); i++) {
				timeDtos.add(timeslotToDTO(timeSlots.get(i)));
			}
			techDTO.setTimeSlots(timeDtos);
		}
		
		return techDTO;
		
	}

	
	/**
	 * Method to get all existing technicians
	 * @return a list of all the existing technicians as dtos
	 * 
	 */
	@Transactional
	public List<TechnicianDto> getAllTechnicians() {
		
		List<Technician> technicians = technicianRepository.findAll();
		List<TechnicianDto> techDtos = new ArrayList<>();
		for (Technician tech : technicians) {
			techDtos.add(technicianToDTO(tech));
		}
		return techDtos;
		
	}
	
	
	
	/**
	 * Method to get the work hours of a technician by email
	 * @param email
	 * @return a list of timeslots that correspond to the technicain's work hours
	 * @throws Exception if email is null of if no technician exists with given email
	 * 
	 */
	@Transactional
	public List<TimeSlotDto> getWorkHours(String email) throws Exception{
		
		if(email == null) {
			throw new Exception("Email cannot be empty.");
		}
		
		Technician tech = technicianRepository.findTechnicianByEmail(email);
		List<TimeSlotDto> dtos = new ArrayList<>();
		
		
		if(tech == null) {
			throw new Exception("Technician not found.");
		} else {
			List<TimeSlot> timeSlots = tech.getTimeslots();
			for(int i = 0; i < timeSlots.size(); i++) {
				dtos.add(timeslotToDTO(timeSlots.get(i)));
			}
		}
		
		return dtos;
		
	}
	
	
	
	
	//USE CASES
	
	
	@Transactional
	public List<TimeSlotDto> viewTechnicianSchedule(String email) throws Exception{
		
		if(email == null) {
			throw new Exception("Email cannot be empty.");
		}
		
		List<TimeSlotDto> schedule = new ArrayList<>();
		Technician tech = technicianRepository.findTechnicianByEmail(email);
		if(tech == null) {
			throw new Exception("Technician not found.");
		}
		List<Appointment> techAppointments = tech.getAppointments();
		
		for(int i = 0; i < techAppointments.size(); i++) {
			Appointment appointment = techAppointments.get(i);
			TimeSlot timeSlot = appointment.getTimeSlot();
			schedule.add(timeslotToDTO(timeSlot));
		}
		
		return schedule;
		
	}
	
	
	
	@Transactional
	public String addTechnicianWorkHours(String email, List<TimeSlotDto> dtos) throws Exception{
		
		if(email == null) {
			throw new Exception("Email cannot be empty.");
		}

		Technician tech = technicianRepository.findTechnicianByEmail(email);
		if(tech == null) {
			throw new Exception("Technician not found.");
		}
		
		List<TimeSlot> workHours = new ArrayList<>();
		for(int i = 0; i < dtos.size(); i++) {
			TimeSlotDto dto = dtos.get(i);
			TimeSlot timeSlot = TimeSlotService.DtoToTimeSlot(dto);
			workHours.add(timeSlot);
		}
		tech.setTimeslots(workHours);
		technicianRepository.save(tech);
		return "Work hours for technician " + email + " successfully added.";
	}
	
	
	
	
	@Transactional
	public List<AppointmentDto> viewAppointments(String email) throws Exception{
		
		if(email == null) {
			throw new Exception("Email cannot be empty.");
		}
		
		List<AppointmentDto> appDtos = new ArrayList<>();
		Technician tech = technicianRepository.findTechnicianByEmail(email);
		if(tech == null) {
			throw new Exception("Technician not found.");
		}
		List<Appointment> techAppointments = tech.getAppointments();
		
		for(int i = 0; i < techAppointments.size(); i++) {
			AppointmentDto thisAppDto = appointmentToDTO(techAppointments.get(i));		//TODO Convert to Dto
			appDtos.add(thisAppDto);
		}
		
		return appDtos;
	}
	
	
	//TODO Remove this. Should have already been implemented in AppointmentService
	
	private AppointmentDto appointmentToDTO(Appointment app) {
			
		AppointmentDto dto = new AppointmentDto();
		dto.setCustomer(CustomerService.customerToDTO(app.getCustomer()));
		dto.setTechnician(technicianToDTO(app.getTechnician()));
		dto.setTimeSlot(TimeSlotService.timeslotToDTO(app.getTimeSlot()));
		dto.setService(ServiceService.serviceToDto(app.getService()));
		return dto;
			
	} 
	
	
	
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




