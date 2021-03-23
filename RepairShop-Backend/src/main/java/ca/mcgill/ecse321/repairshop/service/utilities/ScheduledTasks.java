package ca.mcgill.ecse321.repairshop.service.utilities;

import ca.mcgill.ecse321.repairshop.dto.ReminderDto;
import ca.mcgill.ecse321.repairshop.model.Appointment;
import ca.mcgill.ecse321.repairshop.model.Customer;
import ca.mcgill.ecse321.repairshop.model.ReminderType;
import ca.mcgill.ecse321.repairshop.model.Technician;
import ca.mcgill.ecse321.repairshop.repository.AppointmentRepository;
import ca.mcgill.ecse321.repairshop.repository.CustomerRepository;
import ca.mcgill.ecse321.repairshop.repository.TechnicianRepository;
import ca.mcgill.ecse321.repairshop.service.ReminderService;
import ca.mcgill.ecse321.repairshop.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

import static ca.mcgill.ecse321.repairshop.service.utilities.SystemTime.getSystemDate;

@Component
public class ScheduledTasks {

    @Autowired
    private ReminderService reminderService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private TechnicianRepository techRepository;

    /**
     * Everyday at 6 am, this method will be called to send all today's:<br/>
     * - Service Reminders<br/>
     * - Upcoming Appointment
     */
    @Scheduled(cron = "0 0 6 * * ?")
    public void sendAllTodayReminder() {

        String today = getSystemDate();

        List<ReminderDto> allReminderDtos = reminderService.getAllReminders();

        for (ReminderDto reminderDto : allReminderDtos) {
            String reminderDate = reminderDto.getDateTime().toString().substring(0, 10);

            if (reminderDate.equals(today)) {

                String customerEmail = reminderDto.getCustomerDto().getEmail();
                String customerName = reminderDto.getCustomerDto().getName();
                Timestamp appointmentDateTime = reminderDto.getAppointmentDateTime();
                ReminderType reminderType = reminderDto.getReminderType();
                String serviceName = reminderDto.getServiceName();
                String price = null;

                try {
                    price = String.valueOf(serviceService.getServiceByName(serviceName).getPrice());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                emailService.sendReminderEmail(customerEmail, customerName, appointmentDateTime, reminderType, serviceName, price);
            }
        }
    }
    
    
    
    @Scheduled(cron = "0 0 6 * * ?")	//on 6 am every day
    public void deletePastAppointments() {
    	
    	List<Appointment> appointmentList = appointmentRepository.findAll();
    	
    	for(Appointment appointment : appointmentList) {
    		
    		
    		if(SystemTime.getCurrentDateTime().compareTo(appointment.getTimeSlot().getEndDateTime()) > 0) {
    			//remove appointment from customer
        		Customer customer = appointment.getCustomer();
        		List<Appointment> customerApps = customer.getAppointments();
                customerApps.removeIf(app -> app.getAppointmentID().equals(appointment.getAppointmentID()));
                customer.setAppointments(customerApps);
                customerRepository.save(customer);
                
                //remove appointment from technician
        		Technician tech = appointment.getTechnician();
        		List<Appointment> techApps = tech.getAppointments();
                techApps.removeIf(app -> app.getAppointmentID().equals(appointment.getAppointmentID()));
                tech.setAppointments(techApps);
                techRepository.save(tech);
                
                //delete the appointment
                appointmentRepository.delete(appointment);
    		}
    		
    		
    	}
    }
    
    
    
    
    
    
    
    
    
    
    
    
}