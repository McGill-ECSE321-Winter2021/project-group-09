package ca.mcgill.ecse321.repairshop.service;


import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import ca.mcgill.ecse321.repairshop.model.*;
import ca.mcgill.ecse321.repairshop.repository.*;
import ca.mcgill.ecse321.repairshop.service.utilities.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.repairshop.dto.AppointmentDto;
import ca.mcgill.ecse321.repairshop.dto.TechnicianDto;
import ca.mcgill.ecse321.repairshop.dto.TimeSlotDto;

import static ca.mcgill.ecse321.repairshop.service.TimeSlotService.timeslotToDTO;
import static ca.mcgill.ecse321.repairshop.service.utilities.ValidationHelperMethods.getUpdatedHours;

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

	@Autowired
	AppointmentRepository appointmentRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	AppointmentService appointmentService;

	@Autowired
	EmailService emailService;

	@Autowired
	ReminderService reminderService;


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

		// Delete schedule and cancel associated appointments
		deleteSchedule(email);

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

		if (email == null || email.equals("")) throw new Exception("Email cannot be empty.");

		Technician technician = technicianRepository.findTechnicianByEmail(email);
		if (technician == null) throw new Exception("Technician not found.");

//		List<Appointment> appointments = technician.getAppointments();

		// Appointments are removed from technician when cancelled
//		for (Appointment appointment : appointments) {
//			cancelTechAppointment(appointment.getAppointmentID());
//		}
//
//		// Update hours
//		List<TimeSlot> hours = new ArrayList<>();
//		hours.add(null);
//		hours.remove(0);
////		addTechnicianWorkHours(email, hours);
//
//		Technician updatedTech = technicianRepository.findTechnicianByEmail(email);
//		updatedTech.setTimeslots(hours);
//		technicianRepository.save(updatedTech);

//		for (Appointment appointment : appointments) {
//			appointmentService.cancelAppointment(appointment.getAppointmentID());
//		}
//
//		technician.setTimeslots(Collections.emptyList());
//		technicianRepository.save(technician);

		for (TimeSlot hours : technician.getTimeslots()) {
			deleteSpecificWorkHours(email, hours.getStartDateTime(), hours.getEndDateTime());
		}
		
		return email + "'s schedule has been removed.";
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
	public String deleteSpecificWorkHours(String email, Timestamp startTimeSlot, Timestamp endTimeSlot) throws Exception {

		if (email == null || email.equals("")) throw new Exception("Email cannot be empty.");

		Technician technician = technicianRepository.findTechnicianByEmail(email);
		if (technician == null) throw new Exception("Technician not found.");

		List<TimeSlot> workHours = technician.getTimeslots();
		List<Appointment> appointments = technician.getAppointments();
		List<Appointment> finalAppointments = new ArrayList<>(appointments);

		// Remove all appointments within timeslot and the timeslot itself
		for (TimeSlot hours : workHours) {
			// Find target timeslot
			if (hours.getStartDateTime().equals(startTimeSlot) && hours.getEndDateTime().equals(endTimeSlot)) {

				// Check for appointments in those work hours
				for (Appointment appointment : appointments) {
					TimeSlot adjustedApp = getUpdatedHours(appointment.getTimeSlot(), hours.getStartDateTime());
					if (!hours.getStartDateTime().after(adjustedApp.getStartDateTime()) && !hours.getEndDateTime().before(adjustedApp.getEndDateTime())) {
						cancelTechAppointment(appointment.getAppointmentID());
						finalAppointments.remove(appointment);
					}
				}

				//remove it from technician's list
				technician.setAppointments(finalAppointments);

				workHours.remove(hours);
				technician.setTimeslots(workHours);
				technicianRepository.save(technician);

				return "Requested work hours were removed.";
			}
		}

		return "Could not find provided work hours";
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


	/**
	 * Deletes an appointment by ID
	 *
	 * @param appointmentID ID of appointment
	 */
	@Transactional
	public void cancelTechAppointment(Long appointmentID) {
		Optional<Appointment> appointment = appointmentRepository.findById(appointmentID);
		if (appointment.isPresent()) {
			Optional<Technician> tech = technicianRepository.findById(appointment.get().getTechnician().getEmail());
			Optional<Customer> customer = customerRepository.findById(appointment.get().getCustomer().getEmail());
			if (customer.isPresent() && tech.isPresent()) {

				//Remove timeslot from timeslot repository
				timeSlotRepository.deleteById(appointment.get().getTimeSlot().getTimeSlotID());

				//delete the appointment and remove it from customer's list
				appointmentRepository.delete(appointment.get());
				List<Appointment> cusApps = customer.get().getAppointments();
				cusApps.removeIf(app -> app.getAppointmentID().equals(appointmentID));
				customer.get().setAppointments(cusApps);
				customerRepository.save(customer.get());

				//Send APPOINTMENT CANCELLED email
				emailService.appointmentCancelledEmail(customer.get().getEmail(), customer.get().getName(),
						appointment.get().getTimeSlot().getStartDateTime(), appointment.get().getService().getName());

				for (Reminder currReminder : customer.get().getReminders()) {  //Remove all reminders related to this cancelled appointment
					if (currReminder.getReminderType().equals(ReminderType.ServiceReminder) //same //REMINDER TYPE
							&& currReminder.getServiceName().equals(appointment.get().getService().getName()) //same SERVICE NAME
							&& currReminder.getAppointmentDateTime().toString().equals(appointment.get().getTimeSlot().getStartDateTime().toString())) { //same APPT STARTDATETIME

						try {
							reminderService.deleteReminderById(currReminder.getReminderID()); //delete the reminder
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}

			} else {
				throw new EntityNotFoundException("Cannot find associated customer and technician of appointment.");
			}
		} else {
			throw new EntityNotFoundException("Cannot find the appointment by ID.");
		}

	}

}




