package Controllers.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDatabase {
    Connection connection = null;
    ResultSet result = null;
    PreparedStatement ps = null;

    public CustomerDatabase(Connection connect) {
        connection = connect;
    }
    
    public ArrayList<Integer> getIDs(String typeName, boolean customerIsBusiness) throws SQLException {
        String queryStatement = "SELECT ID FROM Customer WHERE Last_Name LIKE ?";
        
        if(customerIsBusiness)
                queryStatement = "SELECT ID FROM Customer WHERE Company LIKE ?";
        
        ArrayList<Integer> resultIDs = new ArrayList<Integer>();
        
        try {
            ps = connection.prepareStatement(queryStatement);
            ps.setString(1, "%"+typeName+"%");
            result = ps.executeQuery();
            while(result.next()) {
                resultIDs.add(result.getInt(1));
            }
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
        return resultIDs;
    }
    
    public int getExactID(String typeName, boolean customerIsBusiness) throws SQLException {
        String queryStatement = "SELECT ID FROM Customer WHERE Last_Name = ?";
        
        if(customerIsBusiness)
                queryStatement = "SELECT ID FROM Customer WHERE Company = ?";
        
        int resultID = -1;
        
        try {
            ps = connection.prepareStatement(queryStatement);
            ps.setString(1, typeName);
            result = ps.executeQuery();
            resultID = result.getInt(1);
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
        return resultID;
    }
    
    public String getFirstOrLastName(int customerID, boolean isLastName) {
        String queryStatement = "SELECT First_Name FROM CUSTOMER WHERE ID = ?";
        if(isLastName)
            queryStatement = "SELECT Last_Name FROM CUSTOMER WHERE ID = ?";

        String name = "";
        
        try {
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, customerID);
            result = ps.executeQuery();
            name = result.getString(1);  
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
        return name;
    }
    
    public String getBusinessName(int customerID) {
        String queryStatement = "SELECT Company FROM CUSTOMER WHERE ID = ?";
        
        String businessName = "";
        
        try {
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, customerID);
            result = ps.executeQuery();
            businessName = result.getString(1);
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
        return businessName;
    }    
    
    public int getIDFromFullName(String fullName) {
        //split names
        String[] splitNames = fullName.split(" ");
        String firstName = splitNames[0];
        String lastName = splitNames[1];
        
        String queryStatement = "SELECT ID FROM Customer WHERE First_Name = ? AND Last_Name = ?";
        
        int resultID = 0;
        
        try {
            ps = connection.prepareStatement(queryStatement);
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            result = ps.executeQuery();
            resultID = result.getInt(1);
            
        }
        catch(Exception e) {
            System.out.println(e);
        }
        finally  {
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
        return resultID;
    }
    
    public String getAddressLine1(int customerID) {
        String queryStatement = "SELECT Address_Line1 FROM CUSTOMER WHERE ID = ?";

        String ad1 = "";
        
        try {
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, customerID);
            result = ps.executeQuery();
            ad1 = result.getString(1);  
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
        return ad1;
    }
    
    public String getPostcode(int customerID) {
        String queryStatement = "SELECT Postal_Code FROM CUSTOMER WHERE ID = ?";

        String postcode = "";
        
        try {
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, customerID);
            result = ps.executeQuery();
            postcode = result.getString(1);  
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
        return postcode;
    }
    
    public ArrayList<Integer> getPostcodeIDs(String typeName) throws SQLException {
        String queryStatement = "SELECT ID FROM Customer WHERE Postal_Code LIKE ?";
        
        ArrayList<Integer> resultIDs = new ArrayList<>();
        
        try {
            ps = connection.prepareStatement(queryStatement);
            ps.setString(1, "%"+typeName+"%");
            result = ps.executeQuery();
            while(result.next()) {
                resultIDs.add(result.getInt(1));
            }
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
        return resultIDs;
    }
    
    public int getRegistrationStatus(int customerID) {
        String queryStatement = "SELECT Registered FROM CUSTOMER WHERE ID = ?";

        int registration = 0;
        
        try {
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, customerID);
            result = ps.executeQuery();
            registration = result.getInt(1);  
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
        return registration;
    }
}