package ca.mcgill.ecse321.repairshop.model;

import java.util.*;

// line 17 "model.ump"
// line 87 "model.ump"
public class Technician extends User
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Technician Associations
  private List<TimeSlot> workshifts;
  private RepairShop repairShop;
  private List<Appointment> appointments;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Technician(String aEmail, String aPassword, String aPhoneNumber, String aName, String aAddress, RepairShop aRepairShop)
  {
    super(aEmail, aPassword, aPhoneNumber, aName, aAddress);
    workshifts = new ArrayList<TimeSlot>();
    boolean didAddRepairShop = setRepairShop(aRepairShop);
    if (!didAddRepairShop)
    {
      throw new RuntimeException("Unable to create technician due to repairShop. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    appointments = new ArrayList<Appointment>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public TimeSlot getWorkshift(int index)
  {
    TimeSlot aWorkshift = workshifts.get(index);
    return aWorkshift;
  }

  public List<TimeSlot> getWorkshifts()
  {
    List<TimeSlot> newWorkshifts = Collections.unmodifiableList(workshifts);
    return newWorkshifts;
  }

  public int numberOfWorkshifts()
  {
    int number = workshifts.size();
    return number;
  }

  public boolean hasWorkshifts()
  {
    boolean has = workshifts.size() > 0;
    return has;
  }

  public int indexOfWorkshift(TimeSlot aWorkshift)
  {
    int index = workshifts.indexOf(aWorkshift);
    return index;
  }
  /* Code from template association_GetOne */
  public RepairShop getRepairShop()
  {
    return repairShop;
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
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfWorkshifts()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addWorkshift(TimeSlot aWorkshift)
  {
    boolean wasAdded = false;
    if (workshifts.contains(aWorkshift)) { return false; }
    workshifts.add(aWorkshift);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeWorkshift(TimeSlot aWorkshift)
  {
    boolean wasRemoved = false;
    if (workshifts.contains(aWorkshift))
    {
      workshifts.remove(aWorkshift);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addWorkshiftAt(TimeSlot aWorkshift, int index)
  {  
    boolean wasAdded = false;
    if(addWorkshift(aWorkshift))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWorkshifts()) { index = numberOfWorkshifts() - 1; }
      workshifts.remove(aWorkshift);
      workshifts.add(index, aWorkshift);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveWorkshiftAt(TimeSlot aWorkshift, int index)
  {
    boolean wasAdded = false;
    if(workshifts.contains(aWorkshift))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWorkshifts()) { index = numberOfWorkshifts() - 1; }
      workshifts.remove(aWorkshift);
      workshifts.add(index, aWorkshift);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addWorkshiftAt(aWorkshift, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOneToMany */
  public boolean setRepairShop(RepairShop aRepairShop)
  {
    boolean wasSet = false;
    if (aRepairShop == null)
    {
      return wasSet;
    }

    RepairShop existingRepairShop = repairShop;
    repairShop = aRepairShop;
    if (existingRepairShop != null && !existingRepairShop.equals(aRepairShop))
    {
      existingRepairShop.removeTechnician(this);
    }
    repairShop.addTechnician(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfAppointments()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addAppointment(Appointment aAppointment)
  {
    boolean wasAdded = false;
    if (appointments.contains(aAppointment)) { return false; }
    appointments.add(aAppointment);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeAppointment(Appointment aAppointment)
  {
    boolean wasRemoved = false;
    if (appointments.contains(aAppointment))
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

  public void delete()
  {
    workshifts.clear();
    RepairShop placeholderRepairShop = repairShop;
    this.repairShop = null;
    if(placeholderRepairShop != null)
    {
      placeholderRepairShop.removeTechnician(this);
    }
    appointments.clear();
    super.delete();
  }

}