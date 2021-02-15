package ca.mcgill.ecse321.repairshop.model;

import java.util.*;

// line 2 "model.ump"
// line 132 "model.ump"
public class RepairShop
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //RepairShop Associations
  private Business business;
  private List<Customer> customers;
  private List<Technician> technicians;
  private List<Admin> admin;
  private List<Appointment> appointments;
  private List<Reminder> reminders;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public RepairShop()
  {
    customers = new ArrayList<Customer>();
    technicians = new ArrayList<Technician>();
    admin = new ArrayList<Admin>();
    appointments = new ArrayList<Appointment>();
    reminders = new ArrayList<Reminder>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public Business getBusiness()
  {
    return business;
  }

  public boolean hasBusiness()
  {
    boolean has = business != null;
    return has;
  }
  /* Code from template association_GetMany */
  public Customer getCustomer(int index)
  {
    Customer aCustomer = customers.get(index);
    return aCustomer;
  }

  public List<Customer> getCustomers()
  {
    List<Customer> newCustomers = Collections.unmodifiableList(customers);
    return newCustomers;
  }

  public int numberOfCustomers()
  {
    int number = customers.size();
    return number;
  }

  public boolean hasCustomers()
  {
    boolean has = customers.size() > 0;
    return has;
  }

  public int indexOfCustomer(Customer aCustomer)
  {
    int index = customers.indexOf(aCustomer);
    return index;
  }
  /* Code from template association_GetMany */
  public Technician getTechnician(int index)
  {
    Technician aTechnician = technicians.get(index);
    return aTechnician;
  }

  public List<Technician> getTechnicians()
  {
    List<Technician> newTechnicians = Collections.unmodifiableList(technicians);
    return newTechnicians;
  }

  public int numberOfTechnicians()
  {
    int number = technicians.size();
    return number;
  }

  public boolean hasTechnicians()
  {
    boolean has = technicians.size() > 0;
    return has;
  }

  public int indexOfTechnician(Technician aTechnician)
  {
    int index = technicians.indexOf(aTechnician);
    return index;
  }
  /* Code from template association_GetMany */
  public Admin getAdmin(int index)
  {
    Admin aAdmin = admin.get(index);
    return aAdmin;
  }

  public List<Admin> getAdmin()
  {
    List<Admin> newAdmin = Collections.unmodifiableList(admin);
    return newAdmin;
  }

  public int numberOfAdmin()
  {
    int number = admin.size();
    return number;
  }

  public boolean hasAdmin()
  {
    boolean has = admin.size() > 0;
    return has;
  }

  public int indexOfAdmin(Admin aAdmin)
  {
    int index = admin.indexOf(aAdmin);
    return index;
  }
  /* Code from template association_GetMany */
  public Appointment getAppointment(int index)
  {
    Appointment aAppointment = appointments.get(index);
    return aAppointment;
  }

  public List<Appointment> getAppointments()
  {
    List<Appointment> newAppointments = Collections.unmodifiableList(appointments);
    return newAppointments;
  }

  public int numberOfAppointments()
  {
    int number = appointments.size();
    return number;
  }

  public boolean hasAppointments()
  {
    boolean has = appointments.size() > 0;
    return has;
  }

  public int indexOfAppointment(Appointment aAppointment)
  {
    int index = appointments.indexOf(aAppointment);
    return index;
  }
  /* Code from template association_GetMany */
  public Reminder getReminder(int index)
  {
    Reminder aReminder = reminders.get(index);
    return aReminder;
  }

  public List<Reminder> getReminders()
  {
    List<Reminder> newReminders = Collections.unmodifiableList(reminders);
    return newReminders;
  }

  public int numberOfReminders()
  {
    int number = reminders.size();
    return number;
  }

  public boolean hasReminders()
  {
    boolean has = reminders.size() > 0;
    return has;
  }

  public int indexOfReminder(Reminder aReminder)
  {
    int index = reminders.indexOf(aReminder);
    return index;
  }
  /* Code from template association_SetOptionalOneToOne */
  public boolean setBusiness(Business aNewBusiness)
  {
    boolean wasSet = false;
    if (business != null && !business.equals(aNewBusiness) && equals(business.getRepairShop()))
    {
      //Unable to setBusiness, as existing business would become an orphan
      return wasSet;
    }

    business = aNewBusiness;
    RepairShop anOldRepairShop = aNewBusiness != null ? aNewBusiness.getRepairShop() : null;

    if (!this.equals(anOldRepairShop))
    {
      if (anOldRepairShop != null)
      {
        anOldRepairShop.business = null;
      }
      if (business != null)
      {
        business.setRepairShop(this);
      }
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCustomers()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Customer addCustomer(String aEmail, String aPassword, String aPhoneNumber, String aName, String aAddress)
  {
    return new Customer(aEmail, aPassword, aPhoneNumber, aName, aAddress, this);
  }

  public boolean addCustomer(Customer aCustomer)
  {
    boolean wasAdded = false;
    if (customers.contains(aCustomer)) { return false; }
    RepairShop existingRepairShop = aCustomer.getRepairShop();
    boolean isNewRepairShop = existingRepairShop != null && !this.equals(existingRepairShop);
    if (isNewRepairShop)
    {
      aCustomer.setRepairShop(this);
    }
    else
    {
      customers.add(aCustomer);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeCustomer(Customer aCustomer)
  {
    boolean wasRemoved = false;
    //Unable to remove aCustomer, as it must always have a repairShop
    if (!this.equals(aCustomer.getRepairShop()))
    {
      customers.remove(aCustomer);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addCustomerAt(Customer aCustomer, int index)
  {  
    boolean wasAdded = false;
    if(addCustomer(aCustomer))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCustomers()) { index = numberOfCustomers() - 1; }
      customers.remove(aCustomer);
      customers.add(index, aCustomer);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCustomerAt(Customer aCustomer, int index)
  {
    boolean wasAdded = false;
    if(customers.contains(aCustomer))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCustomers()) { index = numberOfCustomers() - 1; }
      customers.remove(aCustomer);
      customers.add(index, aCustomer);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addCustomerAt(aCustomer, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfTechnicians()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Technician addTechnician(String aEmail, String aPassword, String aPhoneNumber, String aName, String aAddress)
  {
    return new Technician(aEmail, aPassword, aPhoneNumber, aName, aAddress, this);
  }

  public boolean addTechnician(Technician aTechnician)
  {
    boolean wasAdded = false;
    if (technicians.contains(aTechnician)) { return false; }
    RepairShop existingRepairShop = aTechnician.getRepairShop();
    boolean isNewRepairShop = existingRepairShop != null && !this.equals(existingRepairShop);
    if (isNewRepairShop)
    {
      aTechnician.setRepairShop(this);
    }
    else
    {
      technicians.add(aTechnician);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeTechnician(Technician aTechnician)
  {
    boolean wasRemoved = false;
    //Unable to remove aTechnician, as it must always have a repairShop
    if (!this.equals(aTechnician.getRepairShop()))
    {
      technicians.remove(aTechnician);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addTechnicianAt(Technician aTechnician, int index)
  {  
    boolean wasAdded = false;
    if(addTechnician(aTechnician))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTechnicians()) { index = numberOfTechnicians() - 1; }
      technicians.remove(aTechnician);
      technicians.add(index, aTechnician);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveTechnicianAt(Technician aTechnician, int index)
  {
    boolean wasAdded = false;
    if(technicians.contains(aTechnician))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTechnicians()) { index = numberOfTechnicians() - 1; }
      technicians.remove(aTechnician);
      technicians.add(index, aTechnician);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addTechnicianAt(aTechnician, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfAdmin()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Admin addAdmin(String aEmail, String aPassword, String aPhoneNumber, String aName, String aAddress)
  {
    return new Admin(aEmail, aPassword, aPhoneNumber, aName, aAddress, this);
  }

  public boolean addAdmin(Admin aAdmin)
  {
    boolean wasAdded = false;
    if (admin.contains(aAdmin)) { return false; }
    RepairShop existingRepairShop = aAdmin.getRepairShop();
    boolean isNewRepairShop = existingRepairShop != null && !this.equals(existingRepairShop);
    if (isNewRepairShop)
    {
      aAdmin.setRepairShop(this);
    }
    else
    {
      admin.add(aAdmin);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeAdmin(Admin aAdmin)
  {
    boolean wasRemoved = false;
    //Unable to remove aAdmin, as it must always have a repairShop
    if (!this.equals(aAdmin.getRepairShop()))
    {
      admin.remove(aAdmin);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addAdminAt(Admin aAdmin, int index)
  {  
    boolean wasAdded = false;
    if(addAdmin(aAdmin))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAdmin()) { index = numberOfAdmin() - 1; }
      admin.remove(aAdmin);
      admin.add(index, aAdmin);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveAdminAt(Admin aAdmin, int index)
  {
    boolean wasAdded = false;
    if(admin.contains(aAdmin))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAdmin()) { index = numberOfAdmin() - 1; }
      admin.remove(aAdmin);
      admin.add(index, aAdmin);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addAdminAt(aAdmin, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfAppointments()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Appointment addAppointment(int aAppointmentID, TimeSlot aTimeSlot, Service aService)
  {
    return new Appointment(aAppointmentID, aTimeSlot, aService, this);
  }

  public boolean addAppointment(Appointment aAppointment)
  {
    boolean wasAdded = false;
    if (appointments.contains(aAppointment)) { return false; }
    RepairShop existingRepairShop = aAppointment.getRepairShop();
    boolean isNewRepairShop = existingRepairShop != null && !this.equals(existingRepairShop);
    if (isNewRepairShop)
    {
      aAppointment.setRepairShop(this);
    }
    else
    {
      appointments.add(aAppointment);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeAppointment(Appointment aAppointment)
  {
    boolean wasRemoved = false;
    //Unable to remove aAppointment, as it must always have a repairShop
    if (!this.equals(aAppointment.getRepairShop()))
    {
      appointments.remove(aAppointment);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addAppointmentAt(Appointment aAppointment, int index)
  {  
    boolean wasAdded = false;
    if(addAppointment(aAppointment))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAppointments()) { index = numberOfAppointments() - 1; }
      appointments.remove(aAppointment);
      appointments.add(index, aAppointment);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveAppointmentAt(Appointment aAppointment, int index)
  {
    boolean wasAdded = false;
    if(appointments.contains(aAppointment))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAppointments()) { index = numberOfAppointments() - 1; }
      appointments.remove(aAppointment);
      appointments.add(index, aAppointment);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addAppointmentAt(aAppointment, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfReminders()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Reminder addReminder(String aDateTime, String aType, Reminder.ReminderType aReminderType)
  {
    return new Reminder(aDateTime, aType, aReminderType, this);
  }

  public boolean addReminder(Reminder aReminder)
  {
    boolean wasAdded = false;
    if (reminders.contains(aReminder)) { return false; }
    RepairShop existingRepairShop = aReminder.getRepairShop();
    boolean isNewRepairShop = existingRepairShop != null && !this.equals(existingRepairShop);
    if (isNewRepairShop)
    {
      aReminder.setRepairShop(this);
    }
    else
    {
      reminders.add(aReminder);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeReminder(Reminder aReminder)
  {
    boolean wasRemoved = false;
    //Unable to remove aReminder, as it must always have a repairShop
    if (!this.equals(aReminder.getRepairShop()))
    {
      reminders.remove(aReminder);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addReminderAt(Reminder aReminder, int index)
  {  
    boolean wasAdded = false;
    if(addReminder(aReminder))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfReminders()) { index = numberOfReminders() - 1; }
      reminders.remove(aReminder);
      reminders.add(index, aReminder);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveReminderAt(Reminder aReminder, int index)
  {
    boolean wasAdded = false;
    if(reminders.contains(aReminder))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfReminders()) { index = numberOfReminders() - 1; }
      reminders.remove(aReminder);
      reminders.add(index, aReminder);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addReminderAt(aReminder, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    Business existingBusiness = business;
    business = null;
    if (existingBusiness != null)
    {
      existingBusiness.delete();
      existingBusiness.setRepairShop(null);
    }
    while (customers.size() > 0)
    {
      Customer aCustomer = customers.get(customers.size() - 1);
      aCustomer.delete();
      customers.remove(aCustomer);
    }
    
    while (technicians.size() > 0)
    {
      Technician aTechnician = technicians.get(technicians.size() - 1);
      aTechnician.delete();
      technicians.remove(aTechnician);
    }
    
    while (admin.size() > 0)
    {
      Admin aAdmin = admin.get(admin.size() - 1);
      aAdmin.delete();
      admin.remove(aAdmin);
    }
    
    while (appointments.size() > 0)
    {
      Appointment aAppointment = appointments.get(appointments.size() - 1);
      aAppointment.delete();
      appointments.remove(aAppointment);
    }
    
    while (reminders.size() > 0)
    {
      Reminder aReminder = reminders.get(reminders.size() - 1);
      aReminder.delete();
      reminders.remove(aReminder);
    }
    
  }

}