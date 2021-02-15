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
  private Long reminderID;
  private String dateTime;
  private String type;
  private ReminderType reminderType;

  //Reminder Associations
  private Customer customer;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Reminder(Long aReminderID, String aDateTime, String aType, ReminderType aReminderType, Customer aCustomer)
  {
    reminderID = aReminderID;
    dateTime = aDateTime;
    type = aType;
    reminderType = aReminderType;
    boolean didAddCustomer = setCustomer(aCustomer);
    if (!didAddCustomer)
    {
      throw new RuntimeException("Unable to create reminder due to customer. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setReminderID(Long aReminderID)
  {
    boolean wasSet = false;
    reminderID = aReminderID;
    wasSet = true;
    return wasSet;
  }

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

  public Long getReminderID()
  {
    return reminderID;
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
  public Customer getCustomer()
  {
    return customer;
  }
  /* Code from template association_SetOneToMany */
  public boolean setCustomer(Customer aCustomer)
  {
    boolean wasSet = false;
    if (aCustomer == null)
    {
      return wasSet;
    }

    Customer existingCustomer = customer;
    customer = aCustomer;
    if (existingCustomer != null && !existingCustomer.equals(aCustomer))
    {
      existingCustomer.removeReminder(this);
    }
    customer.addReminder(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Customer placeholderCustomer = customer;
    this.customer = null;
    if(placeholderCustomer != null)
    {
      placeholderCustomer.removeReminder(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "reminderID" + ":" + getReminderID()+ "," +
            "dateTime" + ":" + getDateTime()+ "," +
            "type" + ":" + getType()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "reminderType" + "=" + (getReminderType() != null ? !getReminderType().equals(this)  ? getReminderType().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "customer = "+(getCustomer()!=null?Integer.toHexString(System.identityHashCode(getCustomer())):"null");
  }
}