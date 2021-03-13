package ca.mcgill.ecse321.repairshop.model;

public enum ReminderType {
	OilChange,
	Confirmation,
	Maintenance, 
	RegularCheckups,
	AppointmentReminder  //TODO: add this in domain model customer will get a reminder exactly 10 days before
}
