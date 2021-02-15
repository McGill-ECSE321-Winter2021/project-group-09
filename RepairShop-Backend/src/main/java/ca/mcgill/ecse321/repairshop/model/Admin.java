package ca.mcgill.ecse321.repairshop.model;

public class Admin extends User {

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //Admin Attributes
    private Long adminID;

    //Admin Associations
    private RepairShop repairShop;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public Admin(String aEmail, String aPassword, String aPhoneNumber, String aName, String aAddress, Long aAdminID, RepairShop aRepairShop) {
        super(aEmail, aPassword, aPhoneNumber, aName, aAddress);
        adminID = aAdminID;
        boolean didAddRepairShop = setRepairShop(aRepairShop);
        if (!didAddRepairShop) {
            throw new RuntimeException("Unable to create admin due to repairShop. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
        }
    }

    //------------------------
    // INTERFACE
    //------------------------

    public boolean setAdminID(Long aAdminID) {
        boolean wasSet = false;
        adminID = aAdminID;
        wasSet = true;
        return wasSet;
    }

    public Long getAdminID() {
        return adminID;
    }

    /* Code from template association_GetOne */
    public RepairShop getRepairShop() {
        return repairShop;
    }

    /* Code from template association_SetOneToMany */
    public boolean setRepairShop(RepairShop aRepairShop) {
        boolean wasSet = false;
        if (aRepairShop == null) {
            return wasSet;
        }

        RepairShop existingRepairShop = repairShop;
        repairShop = aRepairShop;
        if (existingRepairShop != null && !existingRepairShop.equals(aRepairShop)) {
            existingRepairShop.removeAdmin(this);
        }
        repairShop.addAdmin(this);
        wasSet = true;
        return wasSet;
    }

    public void delete() {
        RepairShop placeholderRepairShop = repairShop;
        this.repairShop = null;
        if (placeholderRepairShop != null) {
            placeholderRepairShop.removeAdmin(this);
        }
        super.delete();
    }


    public String toString() {
        return super.toString() + "[" +
                "adminID" + ":" + getAdminID() + "]" + System.getProperties().getProperty("line.separator") +
                "  " + "repairShop = " + (getRepairShop() != null ? Integer.toHexString(System.identityHashCode(getRepairShop())) : "null");
    }
}