package ca.mcgill.ecse321.repairshop.model; 

public class Reminder
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum ReminderType { OilChange, Confirmation, Maintenance, RegularCheckups }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Reminder Attributes
  private String dateTime;
  private String type;
  private ReminderType reminderType;

  //Reminder Associations
  private RepairShop repairShop;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Reminder(String aDateTime, String aType, ReminderType aReminderType, RepairShop aRepairShop)
  {
    dateTime = aDateTime;
    type = aType;
    reminderType = aReminderType;
    boolean didAddRepairShop = setRepairShop(aRepairShop);
    if (!didAddRepairShop)
    {
      throw new RuntimeException("Unable to create reminder due to repairShop. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDateTime(String aDateTime)
  {
    boolean wasSet = false;
    dateTime = aDateTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setType(String aType)
  {
    boolean wasSet = false;
    type = aType;
    wasSet = true;
    return wasSet;
  }

  public boolean setReminderType(ReminderType aReminderType)
  {
    boolean wasSet = false;
    reminderType = aReminderType;
    wasSet = true;
    return wasSet;
  }

  public String getDateTime()
  {
    return dateTime;
  }

  public String getType()
  {
    return type;
  }

  public ReminderType getReminderType()
  {
    return reminderType;
  }
  /* Code from template association_GetOne */
  public RepairShop getRepairShop()
  {
    return repairShop;
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
      existingRepairShop.removeReminder(this);
    }
    repairShop.addReminder(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    RepairShop placeholderRepairShop = repairShop;
    this.repairShop = null;
    if(placeholderRepairShop != null)
    {
      placeholderRepairShop.removeReminder(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "dateTime" + ":" + getDateTime()+ "," +
            "type" + ":" + getType()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "reminderType" + "=" + (getReminderType() != null ? !getReminderType().equals(this)  ? getReminderType().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "repairShop = "+(getRepairShop()!=null?Integer.toHexString(System.identityHashCode(getRepairShop())):"null");
  }
}