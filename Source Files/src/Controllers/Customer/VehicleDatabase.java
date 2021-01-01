package Controllers.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VehicleDatabase {
    Connection connection = null;
    ResultSet result = null;
    PreparedStatement ps = null;

    public VehicleDatabase(Connection connect) {
        connection = connect;
    }
    
    public ArrayList<Integer> getCustomerVehicles(int customerID) {
       String queryStatement = "SELECT ID FROM Vehicles WHERE CustomerID = ?";
       ArrayList<Integer> customerVehicles = new ArrayList<Integer>();
        
        try {           
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, customerID);
            result = ps.executeQuery();
            while(result.next()) {
                customerVehicles.add(result.getInt(1));
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
        return customerVehicles;
    }
    
    public ArrayList<Integer> getVehicleComponentIDs(int vehicleID) {
       String queryStatement = "SELECT Car_Type, MakeID, ModelID FROM Vehicles WHERE ID = ?";
       ArrayList<Integer> vehicleComponentIDs = new ArrayList<Integer>();
        
        try {           
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, vehicleID);
            result = ps.executeQuery();
            vehicleComponentIDs.add(result.getInt(1));
            vehicleComponentIDs.add(result.getInt(2));
            vehicleComponentIDs.add(result.getInt(3));
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
        return vehicleComponentIDs;
    }
    
    public String getVehicleName(int carTypeID) {
        String queryStatement = "SELECT Name FROM Car_Type WHERE ID = ?";
        String vehicleName = "";
        try {           
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, carTypeID);
            result = ps.executeQuery();
            vehicleName = result.getString(1);
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
        return vehicleName;
    }
    
    public String getMakeName(int makeID) {
        String queryStatement = "SELECT Name FROM Make WHERE ID = ?";
        String makeName = "";
        try {           
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, makeID);
            result = ps.executeQuery();
            makeName = result.getString(1);
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
        return makeName;
    }
    
    public String getModelName(int modelID) {
        String queryStatement = "SELECT Name FROM Model WHERE ID = ?";
        String modelName = "";
        try {           
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, modelID);
            result = ps.executeQuery();
            modelName = result.getString(1);
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
        return modelName;
    }
    
    public int getCustomerID(int vehicleID) {
        String queryStatement = "SELECT CustomerID FROM Vehicles WHERE ID = ?";
        int customerID = -1;
        try {           
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, vehicleID);
            result = ps.executeQuery();
            customerID = result.getInt(1);
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
        return customerID;
    }
    
    public String getMOTExpire(int vehicleID) {
        String queryStatement = "SELECT MOT_Expire FROM Vehicles WHERE ID = ?";
        String expiry = "";
        try {           
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, vehicleID);
            result = ps.executeQuery();
            expiry = result.getString(1);
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
        return expiry;
    }
    
    public String getLastService(int vehicleID) {
        String queryStatement = "SELECT Last_Service FROM Vehicles WHERE ID = ?";
        String lastService = "";
        try {           
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, vehicleID);
            result = ps.executeQuery();
            lastService = result.getString(1);
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
        return lastService;
    }
    
    public int getTotalVehicles() {
        String queryStatement = "SELECT MAX(ID) FROM Vehicles";
        
        int numVehicles = -1;
        
        try {
            ps = connection.prepareStatement(queryStatement);
            result = ps.executeQuery();
            numVehicles = Integer.parseInt(result.getString(1));
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
        return numVehicles;  
    }
    
    public String getVehicleRegistration(int vehicleID) {
        String queryStatement = "SELECT Registered FROM Vehicles WHERE ID = ?";
        String reg = "";
        try {           
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, vehicleID);
            result = ps.executeQuery();
            reg = result.getString(1);
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
        return reg;
    }
    
    public String getVehicleRegistrationNumber(int vehicleID) {
        String queryStatement = "SELECT Registration FROM Vehicles WHERE ID = ?";
        String reg = "";
        try {           
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, vehicleID);
            result = ps.executeQuery();
            reg = result.getString(1);
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
        return reg;
    }
    
    public int getVehicleID(int customerID) {
        String queryStatement = "SELECT ID FROM Vehicles WHERE CustomerID = ?";
        int vehicleID = -1;
        try {           
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, customerID);
            result = ps.executeQuery();
            vehicleID = result.getInt(1);
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
        return vehicleID;
    }
    
    public boolean deregisterVehicle(int vehicleID) {
        String queryStatement = "UPDATE VEHICLES SET Registered = ? WHERE ID = ?"; 
        
        try {
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, 0);
            ps.setInt(2, vehicleID);
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
        }
        return true;      
    }
    
}
