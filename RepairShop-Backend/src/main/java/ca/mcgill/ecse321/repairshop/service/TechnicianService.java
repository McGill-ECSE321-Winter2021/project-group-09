package ca.mcgill.ecse321.repairshop.service;


import java.sql.Date;
import java.sql.Timestamp;
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
import ca.mcgill.ecse321.repairshop.model.Technician;
import ca.mcgill.ecse321.repairshop.model.TimeSlot;
import ca.mcgill.ecse321.repairshop.repository.AppointmentRepository;
import ca.mcgill.ecse321.repairshop.repository.TechnicianRepository;
import ca.mcgill.ecse321.repairshop.repository.TimeSlotRepository;
import ca.mcgill.ecse321.repairshop.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static ca.mcgill.ecse321.repairshop.service.TimeSlotService.timeslotToDTO;

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
		
		technicianRepository.deleteTechnicianByEmail(email);
		return "Technician account with email " + email + " deleted.";
	}
	
	
	/**
	 * Method to convert Technician to TechnicianDto
	 * @param tech
	 * @return a technician dto corresponding to the technician domain object provided
	 * 
	 */
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
	public List<TimeSlotDto> viewTechnicianSchedule(String email, String weekStartDate) throws Exception{
		
		if(email == null) {
			throw new Exception("Email cannot be empty.");
		}
				
		//get dates of the week
		Date startDate = Date.valueOf(weekStartDate);
		List<String> datesOfWeek = new ArrayList<>();
		for(int i = 0; i < 7; i++) {
			Date thisDate = new Date(startDate.getTime() + (86400000*i));
			datesOfWeek.add(thisDate.toString());
		}
		
		List<TimeSlotDto> schedule = new ArrayList<>();
		Technician tech = technicianRepository.findTechnicianByEmail(email);
		if(tech == null) {
			throw new Exception("Technician not found.");
		}
		List<Appointment> techAppointments = tech.getAppointments();
		
		for(int i = 0; i < techAppointments.size(); i++) {
			Appointment appointment = techAppointments.get(i);
			Date appDate = new Date(appointment.getTimeSlot().getStartDateTime().getTime());
			if(datesOfWeek.contains(appDate.toString())) {
				TimeSlot timeSlot = appointment.getTimeSlot();
				schedule.add(timeslotToDTO(timeSlot));
			}
		}
		
		return schedule;
		
	}
	
	@Transactional // TODO check if this is what is needed for this method: this method supposed to remove absolutely all the timeslots and appointments from technicians shedule
	public String deleteFullTechnicianWorkSchedule(String email) throws Exception{
		
		if(email == null) {
			throw new Exception("Email cannot be empty.");
		}

		Technician technician = technicianRepository.findTechnicianByEmail(email);
		
		if(technician == null) {
			throw new Exception("Technician not found.");
		}
		
		// removing all the appointments
		for (Appointment appointmentToRemove: technician.getAppointments()) {
			technician.getAppointments().remove(appointmentToRemove);
		}
		// removing all the timeslots
		for (TimeSlot timeSlotToRemove: technician.getTimeslots()) {
			technician.getTimeslots().remove(timeSlotToRemove);
		}

		technicianRepository.save(technician);
		
		return "All work hour timeslots and associated appointments for technician " + email + " were successfully removed.";
	}
	
	@Transactional // TODO check if this is what is needed for this method: method supposed to remove the specific timeSlot and appointment from technician schedule
	public String deleteSpecificTechnicianTimeSlotAndAppointmentsFromSchedule(String email, TimeSlot slotToDelete) throws Exception{
		boolean removedAppointments = false;
		boolean removedTimeSlot = false;
		
		if (email == null) {
			throw new Exception("Email cannot be empty.");
		}

		Technician technician = technicianRepository.findTechnicianByEmail(email);
		if (technician == null) {
			throw new Exception("Technician not found.");
		}
		// First removing all the appointments within the chosen work time slot
		for (Appointment appointmentToDelete: technician.getAppointments()) {
			if (appointmentToDelete.getTimeSlot().equals(slotToDelete)) {
				technician.getAppointments().remove(appointmentToDelete);
				removedAppointments = true;
			}
		}
		// Second removing the chosen timeslot itself
		for (TimeSlotDto timeSlot: getWorkHours(email)) {
			if (timeSlot.getStartDateTime().equals(slotToDelete.getStartDateTime()) 
					&& timeSlot.getEndDateTime().equals(slotToDelete.getEndDateTime())) {
				technician.getTimeslots().remove(slotToDelete);
				removedTimeSlot = true;
			}
		}
		technicianRepository.save(technician); // saving the newly removed timeslot change to the technician
		// TODO perhaps the return should be of all the timeslots that are still not removed
		return "Requested TimeSlot was removed: " + removedTimeSlot + " . " + 
				"Associate Appointements within the requested TimeSlot were also removed: " + removedAppointments;
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
		dto.setCustomerDto(CustomerService.customerToDTO(app.getCustomer()));
		dto.setTechnicianDto(technicianToDTO(app.getTechnician()));
		dto.setTimeSlotDto(TimeSlotService.timeslotToDTO(app.getTimeSlot()));
		dto.setServiceDto(ServiceService.serviceToDTO(app.getService()));
		return dto;
			
	} 
	
	








}




