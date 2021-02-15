public class Service
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Service Attributes
  private Long serviceID;
  private int duration;
  private String name;
  private String price;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Service(Long aServiceID, int aDuration, String aName, String aPrice)
  {
    serviceID = aServiceID;
    duration = aDuration;
    name = aName;
    price = aPrice;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setServiceID(Long aServiceID)
  {
    boolean wasSet = false;
    serviceID = aServiceID;
    wasSet = true;
    return wasSet;
  }

  public boolean setDuration(int aDuration)
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

  public Long getServiceID()
  {
    return serviceID;
  }

  public int getDuration()
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
            "serviceID" + ":" + getServiceID()+ "," +
            "duration" + ":" + getDuration()+ "," +
            "name" + ":" + getName()+ "," +
            "price" + ":" + getPrice()+ "]";
  }
}