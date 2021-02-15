package ca.mcgill.ecse321.repairshop.model;

public class Service
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Service Attributes
  private String duration;
  private String name;
  private String price;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Service(String aDuration, String aName, String aPrice)
  {
    duration = aDuration;
    name = aName;
    price = aPrice;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDuration(String aDuration)
  {
    boolean wasSet = false;
    duration = aDuration;
    wasSet = true;
    return wasSet;
  }

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setPrice(String aPrice)
  {
    boolean wasSet = false;
    price = aPrice;
    wasSet = true;
    return wasSet;
  }

  public String getDuration()
  {
    return duration;
  }

  public String getName()
  {
    return name;
  }

  public String getPrice()
  {
    return price;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "duration" + ":" + getDuration()+ "," +
            "name" + ":" + getName()+ "," +
            "price" + ":" + getPrice()+ "]";
  }
}