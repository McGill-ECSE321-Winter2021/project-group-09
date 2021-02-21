package ca.mcgill.ecse321.repairshop.model;

import javax.persistence.Entity;

@Entity
public enum ReminderType {
	
	OilChange,
	Confirmation,
	Maintenance, 
	RegularCheckups

}
