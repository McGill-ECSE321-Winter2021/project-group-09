package ca.mcgill.ecse321.repairshop.model;

import java.util.*;

// line 56 "model.ump"
// line 159 "model.ump"
public class Business
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Business Attributes
  private String name;
  private String address;
  private String phoneNumber;
  private String email;
  private int numberOfRepairSpots;

  //Business Associations
  private List<TimeSlot> vacations;
  private RepairShop repairShop;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Business(String aName, String aAddress, String aPhoneNumber, String aEmail, int aNumberOfRepairSpots, RepairShop aRepairShop)
  {
    name = aName;
    address = aAddress;
    phoneNumber = aPhoneNumber;
    email = aEmail;
    numberOfRepairSpots = aNumberOfRepairSpots;
    vacations = new ArrayList<TimeSlot>();
    boolean didAddRepairShop = setRepairShop(aRepairShop);
    if (!didAddRepairShop)
    {
      throw new RuntimeException("Unable to create business due to repairShop. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setAddress(String aAddress)
  {
    boolean wasSet = false;
    address = aAddress;
    wasSet = true;
    return wasSet;
  }

  public boolean setPhoneNumber(String aPhoneNumber)
  {
    boolean wasSet = false;
    phoneNumber = aPhoneNumber;
    wasSet = true;
    return wasSet;
  }

  public boolean setEmail(String aEmail)
  {
    boolean wasSet = false;
    email = aEmail;
    wasSet = true;
    return wasSet;
  }

  public boolean setNumberOfRepairSpots(int aNumberOfRepairSpots)
  {
    boolean wasSet = false;
    numberOfRepairSpots = aNumberOfRepairSpots;
    wasSet = true;
    return wasSet;
  }

  public String getName()
  {
    return name;
  }

  public String getAddress()
  {
    return address;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  public String getEmail()
  {
    return email;
  }

  public int getNumberOfRepairSpots()
  {
    return numberOfRepairSpots;
  }
  /* Code from template association_GetMany */
  public TimeSlot getVacation(int index)
  {
    TimeSlot aVacation = vacations.get(index);
    return aVacation;
  }

  public List<TimeSlot> getVacations()
  {
    List<TimeSlot> newVacations = Collections.unmodifiableList(vacations);
    return newVacations;
  }

  public int numberOfVacations()
  {
    int number = vacations.size();
    return number;
  }

  public boolean hasVacations()
  {
    boolean has = vacations.size() > 0;
    return has;
  }

  public int indexOfVacation(TimeSlot aVacation)
  {
    int index = vacations.indexOf(aVacation);
    return index;
  }
  /* Code from template association_GetOne */
  public RepairShop getRepairShop()
  {
    return repairShop;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfVacations()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addVacation(TimeSlot aVacation)
  {
    boolean wasAdded = false;
    if (vacations.contains(aVacation)) { return false; }
    vacations.add(aVacation);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeVacation(TimeSlot aVacation)
  {
    boolean wasRemoved = false;
    if (vacations.contains(aVacation))
    {
      vacations.remove(aVacation);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addVacationAt(TimeSlot aVacation, int index)
  {  
    boolean wasAdded = false;
    if(addVacation(aVacation))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfVacations()) { index = numberOfVacations() - 1; }
      vacations.remove(aVacation);
      vacations.add(index, aVacation);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveVacationAt(TimeSlot aVacation, int index)
  {
    boolean wasAdded = false;
    if(vacations.contains(aVacation))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfVacations()) { index = numberOfVacations() - 1; }
      vacations.remove(aVacation);
      vacations.add(index, aVacation);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addVacationAt(aVacation, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOneToOptionalOne */
  public boolean setRepairShop(RepairShop aNewRepairShop)
  {
    boolean wasSet = false;
    if (aNewRepairShop == null)
    {
      //Unable to setRepairShop to null, as business must always be associated to a repairShop
      return wasSet;
    }
    
    Business existingBusiness = aNewRepairShop.getBusiness();
    if (existingBusiness != null && !equals(existingBusiness))
    {
      //Unable to setRepairShop, the current repairShop already has a business, which would be orphaned if it were re-assigned
      return wasSet;
    }
    
    RepairShop anOldRepairShop = repairShop;
    repairShop = aNewRepairShop;
    repairShop.setBusiness(this);

    if (anOldRepairShop != null)
    {
      anOldRepairShop.setBusiness(null);
    }
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    vacations.clear();
    RepairShop existingRepairShop = repairShop;
    repairShop = null;
    if (existingRepairShop != null)
    {
      existingRepairShop.setBusiness(null);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "," +
            "address" + ":" + getAddress()+ "," +
            "phoneNumber" + ":" + getPhoneNumber()+ "," +
            "email" + ":" + getEmail()+ "," +
            "numberOfRepairSpots" + ":" + getNumberOfRepairSpots()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "repairShop = "+(getRepairShop()!=null?Integer.toHexString(System.identityHashCode(getRepairShop())):"null");
  }
}