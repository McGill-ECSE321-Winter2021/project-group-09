package ca.mcgill.ecse321.repairshop.model;

public class TimeSlot
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TimeSlot Attributes
  private String startDateTime;
  private String endDateTime;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TimeSlot(String aStartDateTime, String aEndDateTime)
  {
    startDateTime = aStartDateTime;
    endDateTime = aEndDateTime;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setStartDateTime(String aStartDateTime)
  {
    boolean wasSet = false;
    startDateTime = aStartDateTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setEndDateTime(String aEndDateTime)
  {
    boolean wasSet = false;
    endDateTime = aEndDateTime;
    wasSet = true;
    return wasSet;
  }

  public String getStartDateTime()
  {
    return startDateTime;
  }

  public String getEndDateTime()
  {
    return endDateTime;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "startDateTime" + ":" + getStartDateTime()+ "," +
            "endDateTime" + ":" + getEndDateTime()+ "]";
  }
}