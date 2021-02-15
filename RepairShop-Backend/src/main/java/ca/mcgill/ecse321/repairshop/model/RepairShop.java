import java.util.*;

// line 2 "model.ump"
// line 141 "model.ump"
public class RepairShop
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //RepairShop Attributes
  private Long repairShopID;

  //RepairShop Associations
  private Business business;
  private List<Customer> customers;
  private List<Technician> technicians;
  private List<Admin> admin;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public RepairShop(Long aRepairShopID)
  {
    repairShopID = aRepairShopID;
    customers = new ArrayList<Customer>();
    technicians = new ArrayList<Technician>();
    admin = new ArrayList<Admin>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setRepairShopID(Long aRepairShopID)
  {
    boolean wasSet = false;
    repairShopID = aRepairShopID;
    wasSet = true;
    return wasSet;
  }

  public Long getRepairShopID()
  {
    return repairShopID;
  }
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
  public Customer addCustomer(String aEmail, String aPassword, String aPhoneNumber, String aName, String aAddress, Long aCustomerID)
  {
    return new Customer(aEmail, aPassword, aPhoneNumber, aName, aAddress, aCustomerID, this);
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
  public Technician addTechnician(String aEmail, String aPassword, String aPhoneNumber, String aName, String aAddress, Long aTechnicianID)
  {
    return new Technician(aEmail, aPassword, aPhoneNumber, aName, aAddress, aTechnicianID, this);
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
  public Admin addAdmin(String aEmail, String aPassword, String aPhoneNumber, String aName, String aAddress, Long aAdminID)
  {
    return new Admin(aEmail, aPassword, aPhoneNumber, aName, aAddress, aAdminID, this);
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
    
  }


  public String toString()
  {
    return super.toString() + "["+
            "repairShopID" + ":" + getRepairShopID()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "business = "+(getBusiness()!=null?Integer.toHexString(System.identityHashCode(getBusiness())):"null");
  }
}