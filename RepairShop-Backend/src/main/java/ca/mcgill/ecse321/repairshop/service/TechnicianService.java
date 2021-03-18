package ca.mcgill.ecse321.repairshop.service;


import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import ca.mcgill.ecse321.repairshop.model.Business;
import ca.mcgill.ecse321.repairshop.repository.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.repairshop.dto.AppointmentDto;
import ca.mcgill.ecse321.repairshop.dto.TechnicianDto;
import ca.mcgill.ecse321.repairshop.dto.TimeSlotDto;
import ca.mcgill.ecse321.repairshop.model.Appointment;
import ca.mcgill.ecse321.repairshop.model.Technician;
import ca.mcgill.ecse321.repairshop.model.TimeSlot;
import ca.mcgill.ecse321.repairshop.repository.AppointmentRepository;
import ca.mcgill.ecse321.repairshop.repository.TechnicianRepository;
import ca.mcgill.ecse321.repairshop.repository.TimeSlotRepository;

import static ca.mcgill.ecse321.repairshop.service.TimeSlotService.timeslotToDTO;

@Service
public class TechnicianService {


	@Autowired
	TechnicianRepository technicianRepository;

	@Autowired
	AppointmentRepository appRepo;
	
	@Autowired
	TimeSlotRepository timeSlotRepository;

	@Autowired
	BusinessRepository businessRepository;


	/**
	 * Method to create a technician account
	 * @param email of technician
	 * @param password of technician
	 * @param phone of technician
	 * @param name of technician
	 * @param address of technician
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

		// add a repair spot for the technician
		Business business = businessRepository.findAll().get(0); // should always be one business
		business.setNumberOfRepairSpots(business.getNumberOfRepairSpots() + 1);

		return technicianToDto(tech);
	}



	/**
	 * Method to change password
	 * @param email of technician
	 * @param newPassword of technician
	 * @return a technician dto corresponding to the technician object that was just updated (TechnicianDto)
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
		return technicianToDto(tech);
	}



	/**
	 * Method to get a technician by email
	 * @param email of technician
	 * @return the technician with the given email (TechnicianDto)
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
		return technicianToDto(tech);
	}



	/**
	 * Method to delete a technician by email
	 * @param email of technician
	 * @throws Exception if the email is empty
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

		//delete technician's work hours
		tech.setTimeslots(null);

		technicianRepository.deleteTechnicianByEmail(email);

		Business business = businessRepository.findAll().get(0); // should always be one business
		business.setNumberOfRepairSpots(business.getNumberOfRepairSpots() - 1);

		return "Technician account with email " + email + " deleted.";
	}


	/**
	 * Method to convert Technician to TechnicianDto
	 * @param tech (Technician)
	 * @return a technician dto corresponding to the technician domain object provided
	 *
	 */
	public static TechnicianDto technicianToDto(Technician tech) {
		TechnicianDto techDto = new TechnicianDto();
		techDto.setAddress(tech.getAddress());
		techDto.setPhoneNumber(tech.getPhoneNumber());
		techDto.setName(tech.getName());
		techDto.setEmail(tech.getEmail());
		techDto.setSetPassword(tech.getPassword());
		techDto.setToken(tech.getToken());

		List<TimeSlot> timeSlots = tech.getTimeslots();
		if(timeSlots != null) {
			ArrayList<TimeSlotDto> timeDtos = new ArrayList<>();
			for (TimeSlot timeSlot : timeSlots) {
				timeDtos.add(timeslotToDTO(timeSlot));
			}
			techDto.setTimeSlots(timeDtos);
		}

		return techDto;

	}


	/**
	 * Method to get all existing technicians
	 * @return a list of all the existing technicians as dtos (List<TechnicianDto>)
	 *
	 */
	@Transactional
	public List<TechnicianDto> getAllTechnicians() {

		List<Technician> technicians = technicianRepository.findAll();
		List<TechnicianDto> techDtos = new ArrayList<>();
		for (Technician tech : technicians) {
			techDtos.add(technicianToDto(tech));
		}
		return techDtos;

	}



	/**
	 * Method to get the work hours of a technician by email
	 * @param email of technician
	 * @return a list of timeslots that correspond to the technician's work hours
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
			for (TimeSlot timeSlot : timeSlots) {
				dtos.add(timeslotToDTO(timeSlot));
			}
		}

		return dtos;

	}




	//USE CASES

	/**
	 * View a technician's schedule.
	 * @param email of Technician
	 * @param weekStartDate starting date of the work schedule to look at from
	 * @return list of timeslots (List<TimeSlotDto>)
	 * @throws Exception if email is empty or technician cannot be found
	 */
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

		for (Appointment appointment : techAppointments) {
			Date appDate = new Date(appointment.getTimeSlot().getStartDateTime().getTime());
			if (datesOfWeek.contains(appDate.toString())) {
				TimeSlot timeSlot = appointment.getTimeSlot();
				schedule.add(timeslotToDTO(timeSlot));
			}
		}

		return schedule;

	}
	
	/**
	 * Delete a technician's schedule
	 * @param email of technician
	 * @return whether the work schedule was removed successfully
	 * @throws Exception if email is empty or technician cannot be found
	 */
	@Transactional 
	public String deleteSchedule(String email) throws Exception{
		
		if(email == null) {
			throw new Exception("Email cannot be empty.");
		}

		Technician technician = technicianRepository.findTechnicianByEmail(email);
		
		if(technician == null) {
			throw new Exception("Technician not found.");
		}
		
		// removing all the appointments
		for (int appointmentIndex = 0; appointmentIndex < technician.getAppointments().size(); appointmentIndex++) {
			technician.getAppointments().remove(technician.getAppointments().get(appointmentIndex));
		}
		// removing all the timeslots
		for (int timeSlotIndex = 0; timeSlotIndex < technician.getTimeslots().size(); timeSlotIndex++) {
			technician.getTimeslots().remove(technician.getTimeslots().get(timeSlotIndex));
		}

		technicianRepository.save(technician);
		
		return "All work hour timeslots and associated appointments for technician " + email + " were successfully removed.";
	}

	/**
	 * Delete specific set of work hours from the technician's schedule (and their appointments within the work hours)
	 * @param email of technician
	 * @param startTimeSlot is beginning timeslot time to be removed
	 * @param endTimeSlot is the end timeslot time to be removed
	 * @return whether the specific work schedule was removed successfully
	 * @throws Exception if email is empty or technician cannot be found
	 */
	@Transactional 
	public String deleteSpecificWorkHours(String email, java.sql.Timestamp startTimeSlot, java.sql.Timestamp endTimeSlot) throws Exception{
		
		if (email == null) {
			throw new Exception("Email cannot be empty.");
		}

		Technician technician = technicianRepository.findTechnicianByEmail(email);
		if (technician == null) {
			throw new Exception("Technician not found.");
		}

		for (int timeSlotIndex = 0; timeSlotIndex < technician.getTimeslots().size(); timeSlotIndex++) {
			if ((startTimeSlot.equals(technician.getTimeslots().get(timeSlotIndex).getStartDateTime())) 
					&& (endTimeSlot.equals(technician.getTimeslots().get(timeSlotIndex).getEndDateTime()))) {
				for (int appointmentIndex = 0; appointmentIndex < technician.getAppointments().size(); appointmentIndex++) {
					if (technician.getAppointments().get(appointmentIndex).getTimeSlot().getStartDateTime().equals(startTimeSlot) 
							&& technician.getAppointments().get(appointmentIndex).getTimeSlot().getEndDateTime().equals(endTimeSlot)) {
						technician.getAppointments().remove(technician.getAppointments().get(appointmentIndex));
					}
				}
				technician.getTimeslots().remove(technician.getTimeslots().get(timeSlotIndex));
				technicianRepository.save(technician); // saving the newly removed timeslot change to the technician
				
			}
			else {
				return "Time slot not found.";
			}
		}
	
		return "Requested TimeSlot and associate Appointements within the requested TimeSlot were removed.";
	}
	


	/**
	 * Add technician's work schedule.
	 * @param email of technician
	 * @param dtos list of work hours of the technician to add (List<TimeSlotDto>)
	 * @return whether the work hours were added successfully
	 * @throws Exception if email is empty or technician cannot be found
	 */
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
		for (TimeSlotDto dto : dtos) {
			TimeSlot timeSlot = TimeSlotService.DtoToTimeSlot(dto);
			workHours.add(timeSlot);
		}
		tech.setTimeslots(workHours);
		technicianRepository.save(tech);
		return "Work hours for technician " + email + " successfully added.";
	}


	/**
	 * View appointments of a technician.
	 * @param email of the technician
	 * @return list of appointments of a technician (List<AppointmentDto>)
	 * @throws Exception if email is empty or technician cannot be found
	 */
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

		for (Appointment techAppointment : techAppointments) {
			AppointmentDto thisAppDto = AppointmentService.appointmentToDto(techAppointment);
			appDtos.add(thisAppDto);
		}

		return appDtos;
	}

}




