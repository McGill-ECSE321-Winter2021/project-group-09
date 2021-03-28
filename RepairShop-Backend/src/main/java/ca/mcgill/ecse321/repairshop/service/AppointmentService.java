package ca.mcgill.ecse321.repairshop.service;

import ca.mcgill.ecse321.repairshop.dto.AppointmentDto;
import ca.mcgill.ecse321.repairshop.model.*;
import ca.mcgill.ecse321.repairshop.repository.*;
import ca.mcgill.ecse321.repairshop.service.exceptions.TimeConstraintException;
import ca.mcgill.ecse321.repairshop.service.utilities.EmailService;
import ca.mcgill.ecse321.repairshop.service.utilities.SystemTime;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ca.mcgill.ecse321.repairshop.service.CustomerService.customerToDto;
import static ca.mcgill.ecse321.repairshop.service.ServiceService.serviceToDTO;
import static ca.mcgill.ecse321.repairshop.service.TechnicianService.technicianToDto;
import static ca.mcgill.ecse321.repairshop.service.TimeSlotService.timeslotToDTO;
import static ca.mcgill.ecse321.repairshop.service.utilities.ValidationHelperMethods.*;

@org.springframework.stereotype.Service
public class AppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    TechnicianRepository technicianRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    TimeSlotRepository timeSlotRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    ReminderService reminderService;


    /**
     * Helper method to determine if an appointment can be booked with certain parameters
     *
     * @param timeSlot   for the target start and end time of the appointment
     * @param technician to check for the appointment
     * @param business   to check for conflicting holidays
     * @return a boolean for whether an appointment can be booked then
     */
    public static boolean isBookable(TimeSlot timeSlot, Technician technician, Business business) {

        if (technician == null) return false;
        // Get technician's work hours
        List<TimeSlot> workHours = technician.getTimeslots();
        List<TimeSlot> adjustedHours = new ArrayList<>();
        for (TimeSlot hours : workHours) {
            adjustedHours.add(getUpdatedHours(hours,timeSlot.getStartDateTime()));
        }

        // Get Technician's appointments
        List<Appointment> appointments = technician.getAppointments();
        // Get their corresponding timeslots
        List<TimeSlot> appointmentTimeslots = new ArrayList<>();

        for (Appointment appointment : appointments) {
            appointmentTimeslots.add(appointment.getTimeSlot());
        }

        // Get all holidays (There will always only be one business)
        List<TimeSlot> allHolidays = business.getHolidays();

        // Check if it can be booked at that time
        // Need to check if there is a timeslot that can be booked within the technician's work hours
        // but not overlapping with other appointments the technician has, and not during holidays
        // Note: using !time.after(time2) to simulate "before" but to include the time if it is equal

        // Within technician's work hours
        boolean withinHours = false;

        for (TimeSlot hours : adjustedHours) {
            if (!hours.getStartDateTime().after(timeSlot.getStartDateTime()) && !hours.getEndDateTime().before(timeSlot.getEndDateTime())) {
                withinHours = true;
                break;
            }
        }

        if (!withinHours) return false;

        // Does not overlap with the technician's other appointments. If it does, return false (not including end time)
        for (TimeSlot app : appointmentTimeslots) {
            if (!timeSlot.getStartDateTime().after(app.getEndDateTime()) && timeSlot.getEndDateTime().after(app.getStartDateTime()))
                return false;
        }

        // Does not overlap with holidays. If it does, return false (not including end time)
        for (TimeSlot holiday : allHolidays) {
            if (!timeSlot.getStartDateTime().after(holiday.getEndDateTime()) && timeSlot.getEndDateTime().after(holiday.getStartDateTime()))
                return false;
        }

        // Passed all checks, so can be booked
        return true;
    }

    /**
     * Helper method to convert Appointment to AppointmentDto
     *
     * @param appointment to convert to dto
     * @return appointmentDto object
     */
    public static AppointmentDto appointmentToDto(Appointment appointment) {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setAppointmentID(appointment.getAppointmentID());
        appointmentDto.setTimeSlotDto(timeslotToDTO(appointment.getTimeSlot()));
        appointmentDto.setServiceDto(serviceToDTO(appointment.getService()));
        appointmentDto.setTechnicianDto(technicianToDto(appointment.getTechnician()));
        appointmentDto.setCustomerDto(customerToDto(appointment.getCustomer()));
        return appointmentDto;
    }

    /**
     * Method to book an appointment given a valid timeslot
     *
     * @param startTime when the appointment will start
     * @param serviceName    the name of the appointment's service
     * @param customerEmail  the email of the customer for whom to book the appointment
     * @return an AppointmentDto for the bookedAppointment
     * @throws Exception for invalid timestamp, service name or technician's email
     */
    @Transactional
    public AppointmentDto createAppointment(Timestamp startTime, String serviceName, String customerEmail) throws Exception {

        // Validate all inputs

        if (startTime == null) throw new Exception("The Timestamp is mandatory");
        if (serviceName == null || serviceName.equals("")) throw new Exception("The service name is mandatory");
        if (customerEmail == null || customerEmail.equals("")) throw new Exception("The customer is mandatory");

        Service service;
        Technician technician = null;
        Customer customer;
        Business business;

        try {
            if (startTime.before(SystemTime.getCurrentDateTime())) throw new Exception("Time has passed");
        } catch (Exception e) {
            throw new Exception("The provided Timestamp is invalid");
        }

        service = serviceRepository.findServiceByName(serviceName);
        if (service == null) throw new Exception("The provided service name is invalid");

        customer = customerRepository.findCustomerByEmail(customerEmail);
        if (customer == null) throw new Exception("The provided customer email is invalid");

        business = businessRepository.findAll().get(0); // Should always be one business

        // Create timeslot
        // the end time is the start time + service duration * 30 minutes * 60 seconds * 1000 milliseconds
        // (the service duration is the number of blocks of 30 minutes)
        int durationInMillis = service.getDuration() * 30 * 60 * 1000;
        Timestamp endTime = new Timestamp(startTime.getTime() + durationInMillis);
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setStartDateTime(startTime);
        timeSlot.setEndDateTime(endTime);

        // Going to use technician with the least appointments already booked
        // if a technician has 0 appointments, go with that one
        List<Technician> allTechnicians = technicianRepository.findAll();
        int numAppointments = Integer.MAX_VALUE;

        for (Technician tempTech : allTechnicians) {
            int tempNumApps = tempTech.getAppointments().size();
            // Make sure that the technician is still available
            if (numAppointments > tempNumApps && isBookable(timeSlot, tempTech, business)) {
                numAppointments = tempNumApps;
                technician = tempTech;
                if (tempNumApps == 0) break;
            }
        }

        // if no technician is available
        if (technician == null) throw new Exception("The appointment cannot be booked");

        Appointment appointment = new Appointment();
        appointment.setTimeSlot(timeSlot);
        appointment.setService(service);
        appointment.setTechnician(technician);
        appointment.setCustomer(customer);

        timeSlotRepository.save(timeSlot);
        appointmentRepository.save(appointment);

        emailService.sendConfirmationEmail(customerEmail, customer.getName(), startTime, serviceName, Double.toString(service.getPrice()));

        //Upcoming Appointment Reminder (10 days before appointment date)
        reminderService.createReminder(SystemTime.addOrSubtractDays(startTime, -10), startTime, serviceName, ReminderType.UpcomingAppointment.toString(), customerEmail);

        //Service Reminder ( 180 days after appointment date)
        boolean hasServiceReminder = false;
        for (Reminder currReminder : customer.getReminders()) {  //If there's already a reminder of the same service, change update the dateTime of the reminder
            if (currReminder.getReminderType().equals(ReminderType.ServiceReminder) && currReminder.getServiceName().equals(serviceName)) {
                currReminder.setDateTime(SystemTime.addOrSubtractDays(startTime, 180));
                hasServiceReminder = true;
                break;
            }
        }
        //If no service reminder yet, create one
        if (!hasServiceReminder)
            reminderService.createReminder(SystemTime.addOrSubtractDays(startTime, 180),
                    startTime, serviceName, ReminderType.ServiceReminder.toString(), customerEmail);
        return appointmentToDto(appointment);

    }

    /** Method to return all times that an appointment for a given service can be created for one week
     * @param startDateString The date to start checking for possible appointments (uses YYYY-MM-DD format)
     * @param serviceName The name of the service for the appointment
     * @return a list of Timestamps for all available appointment start times
     */
    @Transactional
    public List<TimeSlot> getPossibleAppointments(String startDateString, String serviceName) throws Exception {

        if (serviceName == null || serviceName.equals("")) throw new Exception("The service name is mandatory");

        LocalDate startDate;
        Service service;
        Business business;

        if (startDateString == null || startDateString.equals("")) startDate = LocalDate.now();
        else {
            try {
                startDate = LocalDate.parse(startDateString);
                if (startDate.compareTo(LocalDate.now()) < 0) throw new Exception("Time has passed");
            } catch (Exception e) {
                throw new Exception("The provided start date is invalid");
            }
        }

        service = serviceRepository.findServiceByName(serviceName);
        if (service == null) throw new Exception("The provided service name is invalid");

        business = businessRepository.findAll().get(0); // Should always be one business

        List<Technician> technicians = technicianRepository.findAll();

        List<TimeSlot> allTimeSlots = new ArrayList<>();
        LocalDateTime tempDateTime;
        // If the target date is today, use the next 30 minute block, otherwise start at 0th hour
        if (startDate.equals(LocalDate.now())) {
            LocalDateTime today = LocalDateTime.now();
            tempDateTime = startDate.atTime(today.getHour(), today.getMinute(), 0);
            tempDateTime = tempDateTime.plusMinutes((today.getMinute() < 30) ? 30 - today.getMinute() : 60 - today.getMinute());
        } else tempDateTime = startDate.atTime(0, 0, 0);

        int durationInMillis = service.getDuration() * 30 * 60 * 1000; // service duration is an int for the number of 30 minute blocks

        TimeSlot tempTimeSlot = new TimeSlot();
        Timestamp startTime, endTime;

        // loop through all possible start times -> each half hour in a full week = 336 blocks of 30 minutes
        for (int i = 0; i < 336; i++) {

            startTime = Timestamp.valueOf(tempDateTime);
            endTime = new Timestamp(startTime.getTime() + durationInMillis);
            tempTimeSlot.setStartDateTime(startTime);
            tempTimeSlot.setEndDateTime(endTime);

            // Check each technician -> if one is available, add timeslot
            for (Technician technician : technicians) {
                if (isBookable(tempTimeSlot, technician, business)) {
                    TimeSlot timeSlotToAdd = new TimeSlot();
                    timeSlotToAdd.setStartDateTime(tempTimeSlot.getStartDateTime());
                    timeSlotToAdd.setEndDateTime(tempTimeSlot.getEndDateTime());
                    allTimeSlots.add(timeSlotToAdd);
                    break;
                }
            }

            tempDateTime = tempDateTime.plusMinutes(30);
        }

        return allTimeSlots;

    }

    /**
     * Deletes an appointment by ID
     *
     * @param appointmentID ID of appointment
     */
    @Transactional
    public void cancelAppointment(Long appointmentID) {
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
                //remove it from technician's list
                List<Appointment> techApps = tech.get().getAppointments();
                techApps.removeIf(app -> app.getAppointmentID().equals(appointmentID));
                tech.get().setAppointments(techApps);
                technicianRepository.save(tech.get());

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

    /**
     * Deletes an appointment by ID for a customer (checks date)
     *
     * @param appointmentID ID of appointment
     */
    public void cancelCustomerAppointment(Long appointmentID) {

        Optional<Appointment> appointment = appointmentRepository.findById(appointmentID);
        if (appointment.isPresent()) {
            if (SystemTime.addOrSubtractDays(appointment.get().getTimeSlot().getStartDateTime(), -7).compareTo(SystemTime.getCurrentDateTime()) < 0)
                throw new TimeConstraintException("Can only cancel 1 week in advance.");
            else cancelAppointment(appointmentID);
        } else throw new EntityNotFoundException("Cannot find the appointment by ID.");

    }
}
