package Controllers.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDatabase {
    static Connection connection = null;
    static ResultSet result = null;
    static PreparedStatement ps = null;
    
    public AccountDatabase(Connection connect) {
        connection = connect;
    }
    
    public int getCurrentOutstanding(int customerID) {
        int balance = 0;
        String queryStatement = "SELECT Outstanding_Balance FROM Accounts WHERE ID = ?";
        
        try {
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, customerID);
            result = ps.executeQuery();
            balance = result.getInt(1);  
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
        
        return balance;
    }
    
    public int getCurrentOutstandingUsingCustomerID(int customerID) {
        int balance = 0;
        String queryStatement = "SELECT Outstanding_Balance FROM Accounts WHERE CustomerID = ?";
        
        try {
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, customerID);
            result = ps.executeQuery();
            balance = result.getInt(1);  
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
        
        return balance;
    }
    
    public boolean createBalanceAccountForCustomer(int customerID) {   
        String queryStatement = "INSERT INTO Accounts(ID, CustomerID, Outstanding_Balance) VALUES (?, ?, ?)";

        try {
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, customerID);
            ps.setInt(2, customerID);
            ps.setInt(3, 0);
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
    
    public boolean updateCustomerBalance(int customerID, int newBalance) {  
        String queryStatement = "UPDATE Accounts SET Outstanding_Balance = ? WHERE CustomerID = ?";
        
        try {
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, newBalance);
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
    
    public int getAccountIDFromCustomerID(int customerID) {
        int accountID = 0;
        String queryStatement = "SELECT ID FROM Accounts WHERE CustomerID = ?";
        
        try {
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, customerID);
            result = ps.executeQuery();
            accountID = result.getInt(1);  
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
        
        return accountID;
    }
    
    public void setOutstandingBalance(int newAmount, int billID) {  
        String queryStatement = "UPDATE Accounts SET Outstanding_Balance = ? WHERE ID = ?";
        
        try {
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, newAmount);
            ps.setInt(2, billID);
            ps.executeUpdate();
        }
        catch(SQLException e) {
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
    }
}

