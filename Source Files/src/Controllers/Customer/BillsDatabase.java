package Controllers.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BillsDatabase {
    static Connection connection = null;
    static ResultSet result = null;
    static PreparedStatement ps = null;
    
    public BillsDatabase(Connection connect) {
        connection = connect;
    }
    
    public ArrayList<Integer> getBillID(int accountID) {
        String queryStatement = "SELECT ID FROM Bills WHERE AccountsID = ?";
        ArrayList<Integer> resultIDs = new ArrayList<Integer>();
        
        try {
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, accountID);
            result = ps.executeQuery();
            while(result.next()) {
                resultIDs.add(result.getInt(1));
            }
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
        
        return resultIDs;
    }
    
    public String getBillDate(int billID) {
        String queryStatement = "SELECT Date FROM Bills WHERE ID = ?";
        String date = "";
        try {           
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, billID);
            result = ps.executeQuery();
            date = result.getString(1);
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
        return date;
    }
    
    public String getBillTime(int billID) {
        String queryStatement = "SELECT Time FROM Bills WHERE ID = ?";
        String time = "";
        try {           
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, billID);
            result = ps.executeQuery();
            time = result.getString(1);
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
        return time;
    }
    
    public int/*Double*/ getBillAmount(int billID) {
        String queryStatement = "SELECT Amount_Due FROM Bills WHERE ID = ?";
//        Double amount = 0.00;
        int amount = 0;
        try {           
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, billID);
            result = ps.executeQuery();
//            amount = result.getDouble(1);
            amount = result.getInt(1);
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
        return amount;
    }
    
    public int getBillSettled(int billID) {
        String queryStatement = "SELECT Settled FROM Bills WHERE ID = ?";
        int settled = -1;
        try {           
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, billID);
            result = ps.executeQuery();
            settled = result.getInt(1);
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
        return settled;
    }
    
    public void setBillAsSettled(int billID) {  
        String queryStatement = "UPDATE Bills SET Settled = ? WHERE ID = ?";
        
        try {
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, 1);
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
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {}
            } 
        }
    }
    
    public int getBookingID(int billID) {
        String queryStatement = "SELECT BookingsID FROM Bills WHERE ID = ?";
        int bookingID = -1;
        try {           
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, billID);
            result = ps.executeQuery();
            bookingID = result.getInt(1);
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
        return bookingID;
    }
    
}
