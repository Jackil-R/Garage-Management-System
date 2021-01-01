/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controllers.ScheduledMaintenanceBookings;

import Controllers.Customer.CustomerAccount;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Julians
 */
public class BookingHandler {
    
    Connection connection = null;
    Statement stmt = null;
    
    public BookingHandler(){
    connection = DBConnection.dbConnect(); 
    }
    
    public void close() throws SQLException{
    connection.close();
    }
    
    
    
    public void insertBooking(Booking b){
   
         try {           
      stmt = connection.createStatement();     
      String sql = "INSERT INTO Bookings (VehiclesID, Booking_TypeID, Time_Start, Time_End, Bay_Number, MechanicsID)"
              + "VALUES ('"+b.getVehicle().getID()+"','"+b.getBookingType()+"','"+parseToSQLDate(b.getStartDate())
              +"','"+parseToSQLDate(b.getEndDate())+"','"+b.getBayNumber()+"','"+b.getMechanic().getID()+"');";          
      stmt.executeUpdate(sql);  
     
      stmt.close();
        } catch (Exception e) {
                     e.printStackTrace();
        }     
         
         b.setID(getLastBookingID());
         createCustomerBill(b);
         
    }
    
    public int getLastBookingID(){
        int id = 0;
        
     try {           
      stmt = connection.createStatement();     
      String sql = "SELECT ID FROM Bookings ORDER BY ID DESC LIMIT 1;";          
      ResultSet set = stmt.executeQuery(sql); 
      id = set.getInt("ID");
     
      stmt.close();
        } catch (Exception e) {
                     e.printStackTrace();
        }     
         
     return id;
    }
    
      public void updateBooking(Booking b){
   
         try {           
      stmt = connection.createStatement();     
      String sql = "UPDATE Bookings SET VehiclesID='"+b.getVehicle().getID()+"', Booking_TypeID='"+b.getBookingType()+"', Time_Start='"+parseToSQLDate(b.getStartDate())+"', Time_End='"+parseToSQLDate(b.getEndDate())+"', Bay_Number='"
              +b.getBayNumber()+"', MechanicsID='"+b.getMechanic().getID()+ "' WHERE id = '"+b.getID()+"';";   
      stmt.executeUpdate(sql);    
      stmt.close();
        } catch (Exception e) {
                     e.printStackTrace();
        }     
         
    }
    
    
     public void deleteBooking(Booking b){
   
         try {           
      stmt = connection.createStatement();     
      String sql = "DELETE FROM Bookings WHERE id = '"+b.getID()+"';";          
      stmt.executeUpdate(sql);    
      stmt.close();
        } catch (Exception e) {
                     e.printStackTrace();
        }   
         
          try {           
      stmt = connection.createStatement();     
      String sql = "DELETE FROM Bills WHERE BookingsID = '"+b.getID()+"';";          
      stmt.executeUpdate(sql);    
      stmt.close();
        } catch (Exception e) {
                     e.printStackTrace();
        }     
          
          deleteFromCustomerBalance(b);
         
    }
     
      public void deleteFromCustomerBalance(Booking b){
    
    String sql = "UPDATE Accounts SET Outstanding_Balance = Outstanding_Balance - ? WHERE ID = ?;";
 
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setDouble(1, b.getBookingFee().doubleValue());
			preparedStatement.setInt(2, getAccountIDbyCustomerID(b.getVehicle().getCustomer().getCustomerID()) );
                        
			preparedStatement.executeUpdate();
                        preparedStatement.close();
		} catch (SQLException e) {
 
			System.out.println(e.getMessage());
 
		}    
                
    }
    
    public boolean dateOverlap(Calendar start, Calendar end){        
        return false;
    }
    
    private GregorianCalendar parseDate(String d){
        String[] datetime = d.split(" ");
        
        String[] date = datetime[0].split("-");
        int year = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1])-1; // -1 Because its 0 indexed.
        int day = Integer.parseInt(date[2]);
        // System.out.println("year:"+year+"| month:"+month+"| day:"+day);
         
        String[] time = datetime[1].split(":");
        int hour = Integer.parseInt(time[0]);
        int min = Integer.parseInt(time[1]);
      //  int sec = Integer.parseInt(time[2]);
      //  System.out.println("hour:"+hour+"| min:"+min);

        return new GregorianCalendar(year,month,day,hour,min);
    }
    
    
    public ArrayList<Booking> getAllBookings(){                       
        ArrayList<Booking> list = new ArrayList<Booking>();
         try {           
      stmt = connection.createStatement();     
      String sql = "SELECT * FROM Bookings";          
      ResultSet rows = stmt.executeQuery(sql);    
        
      while (rows.next()){
         Booking b = new Booking();       
         
         b.setID(rows.getInt("ID"));
         b.setStartDate(parseDate(rows.getString("Time_Start")));
         b.setEndDate(parseDate(rows.getString("Time_End")));
         b.setBookingType(rows.getInt("Booking_TypeID"));
         b.setBookingTypeName(getBookingTypeNameByID(b.getBookingType()));
         b.setBayNumber(rows.getInt("Bay_Number"));
         b.setVehicle(getVehicleByID(rows.getInt("VehiclesID")));
         b.setMechanic(getMechanicByID(rows.getInt("MechanicsID")));
         b.setBookingFee(getBookingPricebyID(b.getBookingType()));
         b.setBookingNotes(getServiceReportByBookingID(b.getID()).getDescription());
         list.add(b);
         }
      stmt.close();
      rows.close();
        } catch (Exception e) {
                     e.printStackTrace();
        }     
         
        return list;
    }
    
    public BigDecimal getBookingPricebyID(int id){
    BigDecimal bd = new BigDecimal("0.0");
         try {           
      stmt = connection.createStatement();     
      String sql = "SELECT * FROM Booking_Type WHERE ID ="+id+";";          
      ResultSet rows = stmt.executeQuery(sql); 
     
      while (rows.next()){
          bd = new BigDecimal(rows.getString("Price"));

         }
      stmt.close();
      rows.close();
        } catch (Exception e) { e.printStackTrace();  }     
         
         return bd;
    }
    
       public int getBookingTypeIDbyName(String name){
    int bd = 0;
         try {           
      stmt = connection.createStatement();     
      String sql = "SELECT * FROM Booking_Type WHERE Name ='"+name+"';";          
      ResultSet rows = stmt.executeQuery(sql); 
     
      while (rows.next()){
           bd = Integer.parseInt(rows.getString("ID"));
         }
      stmt.close();
      rows.close();
        } catch (Exception e) { e.printStackTrace();  }     
         
         return bd;
    }
    
    
    public Vehicle getVehicleByID(int id){
        Vehicle v = new Vehicle();
     try {           
      stmt = connection.createStatement();     
      String sql = "SELECT * FROM Vehicles WHERE id ="+id+";";          
      ResultSet rows = stmt.executeQuery(sql); 
     
      while (rows.next()){
         v.setID(rows.getInt("ID"));
         v.setCustomer(getCustomerByID(rows.getInt("CustomerID")));
         v.setCarType(rows.getInt("Car_Type"));
         v.setMake(getVehicleMakeByID(rows.getInt("MakeID")));
         v.setModel(getVehicleModelByID(rows.getInt("ModelID")));
         v.setEngineSize(getVehicleEngineByID(rows.getInt("Engine_SizeID")));
         v.setFuelType(getVehicleFuelByID(rows.getInt("Fuel_TypeID")));
         v.setColour(getVehicleColourByID(rows.getInt("ColourID")));
         v.setRegistration(rows.getString("Registration"));
         
         Calendar exp = Calendar.getInstance();
         exp.setTime(parseDate(rows.getString("MOT_Expire")+" 00:00:00").getTime());
         
         v.setMOTExpire(exp);
        // v.setYear(rows.getInt("Year"));//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
         v.setMileage(rows.getInt("Mileage"));
         
         Calendar ls = Calendar.getInstance();
         ls.setTime(parseDate(rows.getString("Last_Service")+" 00:00:00").getTime());
         
         v.setLastService(ls);           
         v.setWarrantyCompany(getVehicleWarrantyByID(rows.getInt("WarrantyID")));
         }
      stmt.close();
      rows.close();
        } catch (Exception e) { e.printStackTrace();  }     
     
     return v;
    }  
    
    public String getVehicleMakeByID(int id){
        String s = "";
     try {           
      stmt = connection.createStatement();     
      String sql = "SELECT * FROM Make WHERE ID ="+id+";";          
      ResultSet rows = stmt.executeQuery(sql); 
     
      while (rows.next()){
          s = rows.getString("Name");
         }
      stmt.close();
      rows.close();
        } catch (Exception e) { e.printStackTrace();  }     
     
     return s;
    }
    
    public String getVehicleModelByID(int id){
        String s = "";
     try {           
      stmt = connection.createStatement();     
      String sql = "SELECT * FROM Model WHERE ID ="+id+";";          
      ResultSet rows = stmt.executeQuery(sql); 
      
      while (rows.next()){
          s = rows.getString("Name");
         }
      stmt.close();
      rows.close();
        } catch (Exception e) {  e.printStackTrace(); }     
     
     return s;
    }
    
     public String getVehicleEngineByID(int id){
        String s = "";
     try {           
      stmt = connection.createStatement();     
      String sql = "SELECT * FROM Engine_Size WHERE ID ="+id+";";          
      ResultSet rows = stmt.executeQuery(sql); 
     
      while (rows.next()){
          s = rows.getString("Column");
         }
      stmt.close();
      rows.close();
        } catch (Exception e) {  e.printStackTrace(); }     
     
     return s;
    }
     
      public String getVehicleFuelByID(int id){
        String s = "";
     try {           
      stmt = connection.createStatement();     
      String sql = "SELECT * FROM Fuel_Type WHERE ID ="+id+";";          
      ResultSet rows = stmt.executeQuery(sql); 
  
      while (rows.next()){
          s = rows.getString("Size");
         }
      stmt.close();
      rows.close();
        } catch (Exception e) { e.printStackTrace();  }     
     
     return s;
    }
      
       public String getVehicleColourByID(int id){
        String s = "";
     try {           
      stmt = connection.createStatement();     
      String sql = "SELECT * FROM Colour WHERE ID ="+id+";";          
      ResultSet rows = stmt.executeQuery(sql); 
    
      while (rows.next()){
          s = rows.getString("Name");
         }
      stmt.close();
      rows.close();
        } catch (Exception e) {  e.printStackTrace(); }     
     
     return s;
    }
    
        public String getVehicleWarrantyByID(int id){
        String s = "";
     try {           
      stmt = connection.createStatement();     
      String sql = "SELECT * FROM Warranty WHERE ID ="+id+";";          
      ResultSet rows = stmt.executeQuery(sql); 
     
      while (rows.next()){
          s = rows.getString("Company");
         }
      stmt.close();
      rows.close();
        } catch (Exception e) { e.printStackTrace();  }     
     
     return s;
    }
               
      public CustomerAccount getCustomerByID(int id){
        CustomerAccount ca = new CustomerAccount();
     try {           
      stmt = connection.createStatement();     
      String sql = "SELECT * FROM Customer WHERE ID ="+id+";";          
      ResultSet rows = stmt.executeQuery(sql); 
      
      while (rows.next()){
         ca.setCustomerID(rows.getInt("ID"));
         ca.setTypeOfCustomer(rows.getInt("TypeID"));
         ca.setFirstName(rows.getString("First_Name"));
         ca.setLastName(rows.getString("Last_Name"));
         ca.setAddressLine1(rows.getString("Address_Line1"));
         ca.setAddressLine2(rows.getString("Address_Line2"));
         ca.setCounty(rows.getString("County"));
         ca.setPostCode(rows.getString("Postal_Code"));
         ca.setPhoneNumber(rows.getString("Phone_Number"));
         ca.setCompanyName(rows.getString("Company"));
         }
      stmt.close();
      rows.close();
        } catch (Exception e) { e.printStackTrace();  }     
     
     return ca;
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public ArrayList<Integer> getFreeBayNumber(Date start, Date end){
        ArrayList<Integer> list = new ArrayList<Integer>();
        
         java.sql.Timestamp sqlStart = new java.sql.Timestamp(start.getTime());
         java.sql.Timestamp sqlEnd = new java.sql.Timestamp(end.getTime());
         
        try {           
      stmt = connection.createStatement();     
      String sql = "SELECT * FROM Bookings WHERE (( Time_Start >= '"+sqlStart
              +"' And Time_Start <= '"+sqlEnd+"')  Or (Time_End >= '"+sqlStart
              +"' And Time_End <= '"+sqlEnd+"')"
              + " Or (Time_Start <= '"+sqlStart
              +"' And Time_End >= '"+sqlEnd+"' ) );";
      
      ResultSet rows = stmt.executeQuery(sql);      
      
      
      while (rows.next()){
          list.add(rows.getInt("Bay_Number"));
      }      
      
      stmt.close();           
      
        } catch (Exception e) {
            e.printStackTrace();
            
        }     
        
        return list;
    }

    public Mechanic getMechanicByID(int id) {
         Mechanic m = new Mechanic();
     try {           
      stmt = connection.createStatement();     
      String sql = "SELECT * FROM Mechanics WHERE ID ="+id+";";          
      ResultSet rows = stmt.executeQuery(sql); 
      
      
      while (rows.next()){
          m.setID(rows.getInt("ID"));
          m.setFirstName(rows.getString("First_Name"));
          m.setLastName(rows.getString("Last_Name"));
          m.setUsername(rows.getString("Username"));
          m.setPassword(rows.getString("Password"));
          m.setRate(rows.getDouble("Rate"));
         }
      stmt.close();
      rows.close();
        } catch (Exception e) { e.printStackTrace();  }     
     
     return m;
    }
    
    /*
     UPDATE LAST SERVICE DATE AND MOT AFTER BOOKING IS COMPLETE
    */
    
    public int getTotalBookingsTodayByMechanicID(int id){
        GregorianCalendar today = (GregorianCalendar) Calendar.getInstance();
        Timestamp now = parseToSQLDate(today);
        int count = 0;
    try {           
      stmt = connection.createStatement();     
      String sql = "SELECT * FROM Bookings WHERE MechanicsID = "+id+" AND  Time_End >= '"+now.toString()+"';";   
      ResultSet rows = stmt.executeQuery(sql); 
      while (rows.next()){
      count++;
      }
      
      stmt.close();
      rows.close();
        } catch (Exception e) { e.printStackTrace();  }     
     
     return count;
     
    }
    
    
    public ArrayList<Mechanic> getAllMechanics(){
    ArrayList<Mechanic> list = new ArrayList<Mechanic>();
        try {           
      stmt = connection.createStatement();     
      String sql = "SELECT * FROM Mechanics;";          
      ResultSet rows = stmt.executeQuery(sql); 
      
      
      while (rows.next()){
          Mechanic m = new Mechanic();
          m.setID(rows.getInt("ID"));
          m.setFirstName(rows.getString("First_Name"));
          m.setLastName(rows.getString("Last_Name"));
          m.setUsername(rows.getString("Username"));
          m.setPassword(rows.getString("Password"));
          m.setRate(rows.getDouble("Rate"));
          list.add(m);
         }
      stmt.close();
      rows.close();
        } catch (Exception e) { e.printStackTrace();  }     
     
       return list; 
    }
    
    public ArrayList<String> getBookingTypes(){
        ArrayList<String> list = new ArrayList<String>();
        
    try {           
      stmt = connection.createStatement();     
      String sql = "SELECT * FROM Booking_Type;";          
      ResultSet rows = stmt.executeQuery(sql); 
           
      while (rows.next()){
          list.add(rows.getString("Name"));
         }
      stmt.close();
      rows.close();
        } catch (Exception e) { e.printStackTrace();  }  
    
    return list;
    }
    
    public ArrayList<String> getBookingTypes(int i){
        ArrayList<String> list = new ArrayList<String>();
        
    try {           
      stmt = connection.createStatement();     
      String sql = "SELECT * FROM Booking_Type LIMIT "+i+";";          
      ResultSet rows = stmt.executeQuery(sql); 
           
      while (rows.next()){
          list.add(rows.getString("Name"));
         }
      stmt.close();
      rows.close();
        } catch (Exception e) { e.printStackTrace();  }  
    
    return list;
    }
    
    public BigDecimal getBookingPrice(String type){
        BigDecimal p = new BigDecimal("0.0");
                
    try {           
      stmt = connection.createStatement();     
      String sql = "SELECT * FROM Booking_Type WHERE Name ='"+type+"';";          
      ResultSet rows = stmt.executeQuery(sql); 
           
      while (rows.next()){
          p = new BigDecimal(rows.getString("Price"));
         }
      stmt.close();
      rows.close();
        } catch (Exception e) { e.printStackTrace();  } 
    
    return p;
    }  

    private java.sql.Timestamp parseToSQLDate(GregorianCalendar c) {
        return new java.sql.Timestamp(c.getTime().getTime());
    }
    
    public void insertServiceReport(ServiceReport sr){
    
      
		String sql = "INSERT INTO SERVICE_REPORTS"
				+ "(BookingsID, Result, Description, Hours_Spent, Mileage) VALUES"
				+ "(?,?,?,?,?)";
 
                System.out.println(sql);
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
 
			preparedStatement.setInt(1, sr.getBookingsID());
			preparedStatement.setInt(2, sr.isResult() ? 1 : 0);
			preparedStatement.setString(3, sr.getDescription());
			preparedStatement.setInt(4, sr.getHours());
                        preparedStatement.setInt(5, sr.getMileage());

			preparedStatement.executeUpdate();
                        preparedStatement.close();

		} catch (SQLException e) {
 
			System.out.println(e.getMessage());
 
		}           
    
    }
    
    public void updateLastService(Booking b){
    
        String sql = "UPDATE Vehicles SET "
				+ "Last_Service = ? "
				+ " WHERE ID = ?;";
 
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
 
                        String[] onlyDate = parseToSQLDate(b.getEndDate()).toString().split(" ");
			preparedStatement.setString(1, onlyDate[0]);
			preparedStatement.setInt(2, b.getVehicle().getID());
                        
			preparedStatement.executeUpdate();
                        preparedStatement.close();
		} catch (SQLException e) {
 
			System.out.println(e.getMessage());
 
		}           
        
    }
    
    public void updateMOTExpire(Booking b){
    

        String sql = "UPDATE Vehicles SET "
				+ "MOT_Expire = ? "
				+ " WHERE ID = ?;";
 
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
                        Date date = parseToSQLDate(b.getEndDate());
                        date.setYear(date.getYear()+1);
                        String[] onlyDate = date.toString().split(" ");
			preparedStatement.setString(1, onlyDate[0]);
			preparedStatement.setInt(2, b.getVehicle().getID());
                        
			preparedStatement.executeUpdate();
                        preparedStatement.close();
		} catch (SQLException e) {
 
			System.out.println(e.getMessage());
 
		}           

        
    }
    
    public int getAccountIDbyCustomerID(int id){
    int rID = 0;
    
     try {           
      stmt = connection.createStatement();     
      String sql = "SELECT * FROM Accounts WHERE CustomerID ="+id+" LIMIT 1;";          
      ResultSet rows = stmt.executeQuery(sql); 
      
       rID = rows.getInt("ID");
         
      stmt.close();
      rows.close();
        } catch (Exception e) { e.printStackTrace();  }     
     
     return rID;
     
    }
    
    public void updateCustomerBalance(Booking b){
    
    String sql = "UPDATE Accounts SET Outstanding_Balance = Outstanding_Balance + ? WHERE ID = ?;";
 
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setDouble(1, b.getBookingFee().doubleValue());
			preparedStatement.setInt(2, getAccountIDbyCustomerID(b.getVehicle().getCustomer().getCustomerID()) );
                        
			preparedStatement.executeUpdate();
                        preparedStatement.close();
		} catch (SQLException e) {
 
			System.out.println(e.getMessage());
 
		}    
                
    }
    
    public void createCustomerBill(Booking b){
    
    String sql = "INSERT INTO Bills"
				+ "(AccountsID, BookingsID, Amount_Due, Settled) VALUES"
				+ "(?,?,?,?)";
 
                System.out.println(sql);
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
 
			preparedStatement.setInt(1,getAccountIDbyCustomerID(b.getVehicle().getCustomer().getCustomerID()) );
			preparedStatement.setInt(2, b.getID());
			preparedStatement.setString(3, b.getBookingFee().toString());
			preparedStatement.setInt(4, 0);

			preparedStatement.executeUpdate();
                        preparedStatement.close();

		} catch (SQLException e) {
 
			System.out.println(e.getMessage());
 
		}           
    updateCustomerBalance(b);
            
    }
    
    
     public void updateServiceReport(ServiceReport sr){
    
         // CHECK IF SERVICE REPOR EXISTS
         try {
      stmt = connection.createStatement();     
      String sqlTest = "SELECT * FROM Service_Reports WHERE BookingsID ="+sr.getBookingsID()+";";          
      ResultSet rowsTest = stmt.executeQuery(sqlTest); 
      
      if (!rowsTest.next())
      {
          insertServiceReport(sr);
      stmt.close();
      rowsTest.close();
      return;
      }
      
      
        } catch (Exception e) { e.printStackTrace();  }  
         
         // SERVICE REPORT ALREADY EXISTS !!
     
		String sql = "UPDATE SERVICE_REPORTS SET "
				+ "Result = ?, Description = ?, Hours_Spent = ?, Mileage = ? "
				+ " WHERE BookingsID = ?;";
 
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
 

			preparedStatement.setInt(1, sr.isResult() ? 1 : 0);
			preparedStatement.setString(2, sr.getDescription());
			preparedStatement.setInt(3, sr.getHours());
                        preparedStatement.setInt(4, sr.getMileage());
                        preparedStatement.setInt(5, sr.getBookingsID());
                        
			preparedStatement.executeUpdate();
                        preparedStatement.close();
		} catch (SQLException e) {
 
			System.out.println(e.getMessage());
 
		}           
    
    }
    
    public ServiceReport getServiceReportByBookingID(int id){
        
      ServiceReport sr = new ServiceReport();
      sr.setID(-1);
     try {           
      stmt = connection.createStatement();     
      String sql = "SELECT * FROM Service_Reports WHERE BookingsID ="+id+";";          
      ResultSet rows = stmt.executeQuery(sql); 
      
      
      while (rows.next()){
          sr.setID(rows.getInt("ID"));
          sr.setBookingsID(id);
          sr.setDescription(rows.getString("Description"));
          sr.setHours(rows.getInt("Hours_Spent"));
          sr.setMileage(rows.getInt("Mileage"));
          sr.setResult( (rows.getInt("Result") == 1) ? true : false );
         }
      stmt.close();
      rows.close();
        } catch (Exception e) { e.printStackTrace();  }     
     
     return sr;
    }
    
    public ServiceReport getServiceReportByID(int id){
      ServiceReport sr = new ServiceReport();
     try {           
      stmt = connection.createStatement();     
      String sql = "SELECT * FROM Service_Reports WHERE ID ="+id+";";          
      ResultSet rows = stmt.executeQuery(sql); 
      
      
      while (rows.next()){
          sr.setID(rows.getInt("ID"));
          sr.setBookingsID(id);
          sr.setDescription(rows.getString("Description"));
          sr.setHours(rows.getInt("Hours_Spent"));
          sr.setMileage(rows.getInt("Mileage"));
          sr.setResult( (rows.getInt("Result") == 1) ? true : false );
         }
      stmt.close();
      rows.close();
        } catch (Exception e) { e.printStackTrace();  }     
     
     return sr;
    }
    
    public ArrayList<Part> getServicePartsBySRID(int id){
    
          ArrayList<Part> list = new ArrayList<Part>();
     try {           
      stmt = connection.createStatement();     
      String sql = "SELECT * FROM Service_Parts WHERE Service_ReportsID ="+id+";";          
      ResultSet rows = stmt.executeQuery(sql); 
      
      
      while (rows.next()){
         list.add(getPartByID(rows.getInt("PartsID")));
         }
      stmt.close();
      rows.close();
        } catch (Exception e) { e.printStackTrace();  }     
     
     return list;
     
    }
    
    public Part getPartByID(int id){
    
         Part p = null;
         
         
     try {           
      stmt = connection.createStatement();     
      String sql = "SELECT * FROM Parts WHERE ID ="+id+" LIMIT 1;";          
      ResultSet rows = stmt.executeQuery(sql); 
      
      
      while (rows.next()){
         p = new Part();
         p.setName(rows.getString("Name"));
         p.setDescription(rows.getString("Description"));
         }
      stmt.close();
      rows.close();
        } catch (Exception e) { e.printStackTrace();  }     
     
     return p;
     
    }

    public String getBookingTypeNameByID(int bookingType) {
        String  bd = "";
         try {           
      stmt = connection.createStatement();     
      String sql = "SELECT * FROM Booking_Type WHERE id ='"+bookingType+"';";          
      ResultSet rows = stmt.executeQuery(sql); 
     
      while (rows.next()){
          bd = rows.getString("Name");
          
         }
      stmt.close();
      rows.close();
        } catch (Exception e) { e.printStackTrace();  }     
         
         return bd;
    }


    
}
