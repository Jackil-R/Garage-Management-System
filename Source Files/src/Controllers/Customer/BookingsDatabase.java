package Controllers.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookingsDatabase {
    Connection connection = null;
    ResultSet result = null;
    PreparedStatement ps = null;

    public BookingsDatabase(Connection connect) {
        connection = connect;
    }
    
    public String getDate(int vehicleID) {
        String queryStatement = "SELECT Time_Start FROM Bookings WHERE VehiclesID = ?";
        String bookingDateString = "";
        try {           
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, vehicleID);
            result = ps.executeQuery();
            bookingDateString = result.getString(1);
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
        return bookingDateString;
    }
    
    public String getTimeStart(int bookingID) {
        String queryStatement = "SELECT Time_Start FROM Bookings WHERE ID = ?";
        String timeStart = "";
        try {           
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, bookingID);
            result = ps.executeQuery();
            timeStart = result.getString(1);  
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
        return timeStart;
    }
    
    public String getTimeEnd(int vehicleID) {
        String queryStatement = "SELECT Time_End FROM Bookings WHERE VehiclesID = ?";
        String timeEnd = "";
        try {           
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, vehicleID);
            result = ps.executeQuery();
            timeEnd = result.getString(1);
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
        return timeEnd;
    }
    
    public String getTimeEndFromID(int bookingID) {
        String queryStatement = "SELECT Time_End FROM Bookings WHERE ID = ?";
        String timeEnd = "";
        try {           
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, bookingID);
            result = ps.executeQuery();
            timeEnd = result.getString(1);
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
        return timeEnd;
    }
    
    public String getTimeStartFromVehicleID(int vehicleID) {
        String queryStatement = "SELECT Time_Start FROM Bookings WHERE VehiclesID = ?";
        String timeStart = "";
        try {           
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, vehicleID);
            result = ps.executeQuery();
            timeStart = result.getString(1);
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
        return timeStart;
    }
    
    public int getBookingTypeIDByVehicleID(int vehicleID) {
        String queryStatement = "SELECT Booking_TypeID FROM Bookings WHERE VehiclesID = ?";
        int bookingTypeID = -1;
        try {           
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, vehicleID);
            result = ps.executeQuery();
            bookingTypeID = result.getInt(1);
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
        return bookingTypeID;
    }

    public int getBookingTypeIDByID(int bookingID) {
        String queryStatement = "SELECT Booking_TypeID FROM Bookings WHERE ID = ?";
        int bookingTypeID = -1;
        try {           
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, bookingID);
            result = ps.executeQuery();
            bookingTypeID = result.getInt(1);
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
        return bookingTypeID;
    }
    
    public String getBookingName(int bookingTypeID) {
        String queryStatement = "SELECT Name FROM Booking_Type WHERE ID = ?";
        String bookingName = "";
        try {           
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, bookingTypeID);
            result = ps.executeQuery();
            bookingName = result.getString(1);
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
        return bookingName;
    }
    
    public int getBookingPrice(int bookingTypeID) {
        String queryStatement = "SELECT Price FROM Booking_Type WHERE ID = ?";
        int bookingPrice = 0;
        try {           
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, bookingTypeID);
            result = ps.executeQuery();
            bookingPrice = result.getInt(1);
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
        return bookingPrice;
    }
    
    public int getTotalBookings() {
        String queryStatement = "SELECT MAX(ID) FROM Bookings";
        
        int numBookings = -1;
        
        try {
            ps = connection.prepareStatement(queryStatement);
            result = ps.executeQuery();
            numBookings = Integer.parseInt(result.getString(1));
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
        return numBookings;  
    }
    
    public String getDateFromID(int ID) {
        String queryStatement = "SELECT Date FROM Bookings WHERE ID = ?";
        String bookingDateString = "";
        try {           
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, ID);
            result = ps.executeQuery();
            bookingDateString = result.getString(1);
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
        return bookingDateString;
    }
    
    public int getVehicleID(int bookingID) {
        String queryStatement = "SELECT VehiclesID FROM Bookings WHERE ID = ?";
        int vehicleID = -1;
        try {           
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, bookingID);
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
    
    public boolean getBookingExists(int bookingID) {
        String queryStatement = "SELECT Bay_Number FROM Bookings WHERE ID = ?";
        boolean exists = false;
        try {           
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, bookingID);
            result = ps.executeQuery();
            int temp = result.getInt(1); //if this does not cause an exception, then the ID is valid
            exists = true;
        }
        catch(Exception e) {
            exists = false;
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
        return exists;
    }
    
    public ArrayList<Integer> getBookingIDsFromVehicleID(int vehicleID) {
       String queryStatement = "SELECT ID FROM Bookings WHERE VehiclesID = ?";
       ArrayList<Integer> bookingIDs = new ArrayList<Integer>();
        
        try {           
            ps = connection.prepareStatement(queryStatement);
            ps.setInt(1, vehicleID);
            result = ps.executeQuery();
            while(result.next()) {
                bookingIDs.add(result.getInt(1));
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
        return bookingIDs;
    }

}
