package Controllers.DiagnosisRepairBookings;

/**
 *
 * @author jackilrajnicant
 */
public class CustomerAccount {
    
    private int customerID;
    private int typeID;
    private String firstName;
    private String lastName;
    private String addressLine1;
    private String addressLine2;
    private String county;    
    private String postCode;
    private String phoneNumber;
    private String companyName;

    public CustomerAccount()
    {
        
    }
    
    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }
    
    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }    

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }
    
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }     
    
    public int getCustomerID()
    {
        return customerID;
    }
    
    public String getFirstName()
    {
        return firstName;
    }
    
    public String getLastName()
    {
        return lastName;
    }
    
    public String getAddressLine1()
    {
        return addressLine1;
    }
    
    public String getAddressLine2()
    {
        return addressLine2;
    }   
    
    public String getPostCode()
    {
        return postCode;
    }
    
    public String getPhoneNumber()
    {
        return phoneNumber;
    }
    
    public String getCompanyName()
    {
        return companyName;
    }
    
     
}
