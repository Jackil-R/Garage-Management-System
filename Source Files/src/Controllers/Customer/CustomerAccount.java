package Controllers.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerAccount {
    static Connection connection = null;
    static ResultSet result = null;
    static PreparedStatement ps = null;
    
    private int customerID;
    private int typeOfCustomer;
    private String firstName;
    private String lastName;
    private String addressLine1;
    private String addressLine2;
    private String county;
    private String postCode;
    private String phoneNumber;
    private String companyName;
    
    public CustomerAccount() {
       
    }
    
    public CustomerAccount(int ID, int type) { //type=1 if company
        customerID = ID;
        
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        /* *** * *** * *** * *** * *** * */
        
        setFirstName();
        setLastName();
        setTypeOfCustomer(); //1 is business, 2 is individual
        setAddressLine1();
        setAddressLine2();
        setCounty();
        setPostCode();
        setPhoneNumber();
        setCompanyName();
        
        /*Now closing the connection to the database */
        try {
            connection.close();
        } catch (SQLException ex) {
            System.out.println("CustomerAccount catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */ 
    }

    public static void setConnection(Connection connection) {
        CustomerAccount.connection = connection;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void setTypeOfCustomer(int typeOfCustomer) {
        this.typeOfCustomer = typeOfCustomer;
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

    public void setCounty(String county) {
        this.county = county;
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

    public int getCustomerID() {
        return customerID;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public int getTypeOfCustomer() {
        return typeOfCustomer;
    }
    
    public String getAddressLine1() {
        return addressLine1;
    }
    
    public String getAddressLine2() {
        return addressLine2;
    }
    
    public String getCounty() {
        return county;
    }
    
    public String getPostCode() {
        return postCode;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setFirstName() {
        String queryStatement = "SELECT First_Name FROM CUSTOMER WHERE ID = ?";        
        firstName = stringQuery(queryStatement);
    }
    
    public void setLastName() {
        String queryStatement = "SELECT Last_Name FROM CUSTOMER WHERE ID = ?";
        lastName = stringQuery(queryStatement);
    }
    
    public void setTypeOfCustomer() {
        String queryStatement = "SELECT TypeID FROM CUSTOMER WHERE ID = ?";
        typeOfCustomer = intQuery(queryStatement);
    }
    
    public void setAddressLine1() {
        String queryStatement = "SELECT Address_Line1 FROM CUSTOMER WHERE ID = ?";
        addressLine1 = stringQuery(queryStatement);
    }
    
    public void setAddressLine2() {
        String queryStatement = "SELECT Address_Line2 FROM CUSTOMER WHERE ID = ?";
        addressLine2 = stringQuery(queryStatement);
    }
    
    public void setCounty() {
        String queryStatement = "SELECT County FROM CUSTOMER WHERE ID = ?";
        county = stringQuery(queryStatement);
    }
    
    public void setPostCode() {
        String queryStatement = "SELECT Postal_Code FROM CUSTOMER WHERE ID = ?";
        postCode = stringQuery(queryStatement);
    }
    
    public void setPhoneNumber() {
        String queryStatement = "SELECT Phone_Number FROM CUSTOMER WHERE ID = ?";
        phoneNumber = stringQuery(queryStatement);
    }
    
    public void setCompanyName() {
        String queryStatement = "SELECT Company FROM CUSTOMER WHERE ID = ?";
        companyName = stringQuery(queryStatement);
    }
    
    public String getFullName() {
        return firstName+ " " + lastName;
    }
    
    private String stringQuery(String queryStatement) {
        String stringTerm = "";
        try {
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, customerID);
            result = ps.executeQuery();
            stringTerm = result.getString(1);
        }
        catch(Exception e) {
            System.out.println(e);
        }
        finally {
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException e) {}
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {}
            }       
        }
        return stringTerm;
    }
    
    private int intQuery(String queryStatement) {
        int intTerm = -1;
        try {
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, customerID);
            result = ps.executeQuery();
            intTerm = result.getInt(1);
        }
        catch(Exception e) {
            System.out.println(e);
        }
        finally {
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException e) {}
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {}
            }       
        }
        return intTerm;
    }
}
