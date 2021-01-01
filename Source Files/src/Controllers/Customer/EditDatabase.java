package Controllers.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EditDatabase {
    Connection connection = null;
    ResultSet result = null;
    PreparedStatement ps = null;

    public EditDatabase(Connection connect) {
        connection = connect;
    }
    
    public ArrayList<String> loadTextFieldsFromID(int customerID) {
//        String queryStatement = "SELECT Company, First_Name, Last_Name, Address_Line1, Address_Line2, County, Postal_Code, Phone_Number FROM CUSTOMER WHERE ID = "+ customerID;
        String queryStatement = "SELECT Company, First_Name, Last_Name, Address_Line1, Address_Line2, County, Postal_Code, Phone_Number FROM CUSTOMER WHERE ID = ?";
        
        ArrayList<String> textFields = new ArrayList<String>();
        
        try {           
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, customerID);
            result = ps.executeQuery();
            while(result.next()) {
                textFields.add(result.getString("Company"));
                textFields.add(result.getString("First_Name"));
                textFields.add(result.getString("Last_Name"));
                textFields.add(result.getString("Address_Line1"));
                textFields.add(result.getString("Address_Line2"));
                textFields.add(result.getString("County"));
                textFields.add(result.getString("Postal_Code"));
                textFields.add(result.getString("Phone_Number"));
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
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {}
            } 
        }
        return textFields;
     }
    
    public boolean commitCustomerChanges(ArrayList<String> newValues, int customerID) {
        String queryStatement = "UPDATE CUSTOMER SET Company = ?, First_Name = ?, Last_Name = ?, Address_Line1 = ?, Address_Line2 = ?, County = ?, Postal_Code = ?, Phone_Number = ? WHERE ID = ?"; 
        
        try {
            ps = connection.prepareStatement(queryStatement);
            ps.setString(1, newValues.get(0));
            ps.setString(2, newValues.get(1));
            ps.setString(3, newValues.get(2));
            ps.setString(4, newValues.get(3));
            ps.setString(5, newValues.get(4));
            ps.setString(6, newValues.get(5));
            ps.setString(7, newValues.get(6));
            ps.setString(8, newValues.get(7));
            ps.setInt(9, customerID);
            ps.executeUpdate();
        }
        catch(SQLException e) {
            System.out.println(e);
            return false;
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
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {}
            } 
        }
        return true;      
    }
    
    public int registerNewCustomer(ArrayList<String> fields, boolean isBusiness) {
        boolean resultOfInsert = false;
        String nullString = "";
        String queryStatement;
        
        int customerID = getNewCustomerID();

        if(isBusiness) {
            queryStatement = "INSERT INTO Customer(ID, TypeID, Company, First_Name, Last_Name, Address_Line1, Address_Line2, Postal_Code, County, Phone_Number, Registered) VALUES(?,?,?,?,?,?,?,?,?,?,?)"; 
          
            try {
                ps = connection.prepareStatement(queryStatement);
                ps.setInt(1, customerID);
                ps.setInt(2, 1);
                ps.setString(3, fields.get(0));
                ps.setString(4, nullString);
                ps.setString(5, nullString);
                ps.setString(6, fields.get(1));
                ps.setString(7, fields.get(2));
                ps.setString(8, fields.get(3));
                ps.setString(9, fields.get(4));
                ps.setString(10, fields.get(5));   
                ps.setInt(11, 1);
                ps.executeUpdate();
                resultOfInsert = true;
            }
            catch(SQLException e) {
                System.out.println(e);
                return 0;
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
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {}
                } 
            }
        }
        else {
            queryStatement = "INSERT INTO Customer(ID, TypeID, Company, First_Name, Last_Name, Address_Line1, Address_Line2, Postal_Code, County, Phone_Number, Registered) VALUES(?,?,?,?,?,?,?,?,?,?,?)"; 
            try {
                ps = connection.prepareStatement(queryStatement);
                ps.setInt(1, customerID);
                ps.setInt(2, 2);
                ps.setString(3, nullString);
                ps.setString(4, fields.get(0));
                ps.setString(5, fields.get(1));
                ps.setString(6, fields.get(2));
                ps.setString(7, fields.get(3));
                ps.setString(8, fields.get(4));
                ps.setString(9, fields.get(5));
                ps.setString(10, fields.get(6));    
                ps.setInt(11, 1);
                
                ps.executeUpdate();
                resultOfInsert = true;
            }
            catch(SQLException e) {
                System.out.println(e);
                return 0;
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
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {}
                } 
            }
        
        }
        return customerID;
    }
    
    private int getNewCustomerID() {
        String queryStatement = "SELECT MAX(ID) FROM Customer";
        
        int newID = -1;
        
        try {
            ps = connection.prepareStatement(queryStatement);
            result = ps.executeQuery();
            newID = Integer.parseInt(result.getString(1));
        }
        catch(SQLException | NumberFormatException e) {
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
        return newID + 1;  
    }
    
    public boolean deleteCustomer(int customerID) {
        boolean success = false;
        String nonQueryStatement = "PRAGMA foreign_keys = ON";
        String queryStatement = "DELETE FROM Customer WHERE ID = ?";
        try {
            ps = connection.prepareStatement(nonQueryStatement);
            ps.executeUpdate();
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, customerID);
            ps.executeUpdate();
            success = true;
        }
        catch(SQLException | NumberFormatException e) {
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
        return success;
    }
    
    public boolean deregisterCustomer(int customerID) {
        String queryStatement = "UPDATE CUSTOMER SET Registered = ? WHERE ID = ?"; 
        
        try {
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, 0);
            ps.setInt(2, customerID);
            ps.executeUpdate();
        }
        catch(SQLException e) {
            System.out.println(e);
            return false;
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
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {}
            } 
        }
        return true;      
    }

}
