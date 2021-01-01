package Controllers.DiagnosisRepairBookings;
import Controllers.Parts.BasketPart;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jackilrajnicant
 */
public class DatabaseHandler {

    Connection connection = null;

    public DatabaseHandler() {
        
        connection = DatabaseConnection.dbConnect();
    
    }

    public ArrayList<DRBooking> getAllBookings() throws ParseException {
        ArrayList<DRBooking> allBooking = new ArrayList<DRBooking>();
        String sqlQuery = "SELECT * FROM Bookings;";

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            while (rs.next()) {
                DRBooking booking = new DRBooking();
                booking.setID(rs.getInt("ID"));
                GregorianCalendar st = parseDate(rs.getString("Time_Start"));
                GregorianCalendar et = parseDate(rs.getString("Time_End"));
                Mechanic m =getMechanicByID(rs.getInt("MechanicsID"));
                booking.setStartDate(st);
                booking.setEndDate(et);
                booking.setBookingType(rs.getInt("Booking_TypeID"));
                booking.setBayNumber(rs.getInt("Bay_Number"));
                booking.setVehicle(getVehicleByID(rs.getInt("VehiclesID")));
                booking.setMechanic(m);
                booking.setFault(getService(rs.getInt("ID")));
                booking.setBookingFinished(getResults(rs.getInt("ID")));
                ObservableList<BasketPart> p = getParts(getServiceReportID(rs.getInt("ID")));
                booking.setPartList(p);
                if(booking.getVehicle().getWarranty()==0 ){
                    booking.setBookingFee(0);
                }else{
                    booking.setBookingFee(calculateBookingFee(st,et,m,p));
                }
                allBooking.add(booking);
            }
            stmt.close();
            rs.close();
            connection.close();
        } catch (SQLException ex) {
            System.out.println("DRBooking DatebaseHandle(Get All bookings) Class, "+ex);
        }
        return allBooking;
    }
    
    /////////////
    
    public int getLastBookingID(){
        try {
            connection = DatabaseConnection.dbConnect();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ID FROM Bookings "
                    + "ORDER BY ID DESC LIMIT 1");
            int id = rs.getInt("ID");
            rs.close();
            stmt.close();
            connection.close();
            return id;
        } catch (SQLException ex) {
            System.out.println("DRBooking DatebaseHandle Class, "+ex);
        }
        return 0;
    }
    
    private String getService(int id) {
        String description = "";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Service_Reports WHERE BookingsID =" + id + ";");
            while (rs.next()) {
                description = rs.getString("Description");
            }
            stmt.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("DRBooking DatebaseHandle Class, "+e);
        }
        return description;
    }
    
    private boolean getResults(int id){
    boolean result = false;
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Service_Reports WHERE BookingsID =" + id + ";");
            while (rs.next()) {
                result = rs.getBoolean("Result");
            }
            stmt.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("DRBooking DatebaseHandle Class, "+e);
        }
        return result;
    }
    
    public int getServiceReportID(int id){
        int serviceRptID=0;
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Service_Reports WHERE BookingsID =" + id + ";");
            while (rs.next()) {
                serviceRptID = rs.getInt("ID");
            }
            stmt.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("DRBooking DatebaseHandle Class, "+e);
        }
        return serviceRptID;
    }
    
    public ObservableList<BasketPart> getParts(int id){
   
        ObservableList<BasketPart> partsUse = FXCollections.observableArrayList();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Service_Parts WHERE Service_ReportsID =" + id + ";");
            while (rs.next()) {
                int partId = rs.getInt("PartsID");
                int quantity = rs.getInt("Quantity");
                BasketPart p = getEachPart(partId, quantity);
                partsUse.add(p);
            }
            stmt.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("DRBooking DatebaseHandle Class, "+e);
        }
        return partsUse;
    }
    
    public BasketPart getEachPart(int id,int q){
                
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Parts WHERE ID = " + id + ";");
            while (rs.next()) {
                int partId = rs.getInt("ID");
                String name = rs.getString("Name");
                String partdescription = rs.getString("Description");
                String type = rs.getString("Type");
                int stock  = rs.getInt("Stock");
                double price = rs.getDouble("Price");
                return new BasketPart(partId, name, partdescription, stock, price, Boolean.FALSE, Integer.toString(q));
            }
            stmt.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("DRBooking DatebaseHandle Class, "+e);
        }
        return null;
    }
   
    public double calculateBookingFee(GregorianCalendar st, GregorianCalendar et, Mechanic m ,ObservableList<BasketPart> p){
        int hour = et.getTime().getHours()-st.getTime().getHours();
        double workedPerHour = hour*m.getRate();
        double partSum=0;
        if(p==null){
            return workedPerHour;
        }else{
            for(int i=0;i<p.size();i++){
                partSum=partSum+(  p.get(i).getPrice()*Integer.parseInt(p.get(i).getWithdraw())  );
            }
            return partSum+workedPerHour;
        }
    }
 
    /////////////
    
    public Vehicle getVehicleByID(int id) throws ParseException {
        Vehicle vehicle = new Vehicle();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("Select * from Vehicles WHERE id =" + id + ";");
            while (rs.next()) {
                vehicle.setCustomer(getCustomerByID(rs.getInt("ID")));
                vehicle.setID(rs.getInt("ID"));
                vehicle.setCarType(rs.getInt("Car_Type"));
                vehicle.setMOTExpire(StringtoCalendar(rs.getString("MOT_Expire")));
                vehicle.setMileage(rs.getInt("Mileage"));
                vehicle.setRegistration(rs.getString("Registration"));
                vehicle.setLastService(StringtoCalendar(rs.getString("Last_Service")));
                vehicle.setMake(getVehicleMakeByID(rs.getInt("MakeID")));
                vehicle.setModel(getVehicleModelByID(rs.getInt("ModelID")));
                vehicle.setEngineSize(getVehicleEngineByID(rs.getInt("Engine_SizeID")));
                vehicle.setFuelType(getVehicleFuelByID(rs.getInt("Fuel_TypeID")));
                vehicle.setColour(getVehicleColourByID(rs.getInt("ColourID")));
                vehicle.setWarranty(rs.getInt("WarrantyID"));
                vehicle.setWarrantyCompany(getVehicleWarrantyByID(rs.getInt("WarrantyID"))); 
                vehicle.setWarrentyExpireDate(getVehicleWarrantyExpireDate(rs.getInt("WarrantyID")));
            }
            stmt.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("DRBooking DatebaseHandle Class, "+ex);
        }
        return vehicle;

    }
    
    public String getVehicleWarrantyExpireDate(int id) {
        String warrantyExDate = "";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Warranty WHERE ID =" + id + ";");
            while (rs.next()) {
                warrantyExDate = rs.getString("Expire_Date");
            }
            stmt.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("DRBooking DatebaseHandle Class, "+ex);
        }
        return warrantyExDate;
    }
    
    public Calendar StringtoCalendar(String dateStr) throws ParseException {
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
        Date dateObj = curFormater.parse(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateObj);
        return calendar;
    }
 
    public CustomerAccount getCustomerByID(int id) {
        CustomerAccount customer = new CustomerAccount();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("Select * from Customer WHERE ID =" + id + ";");
            while (rs.next()) {
                customer.setCustomerID(rs.getInt("ID"));
                customer.setTypeID(rs.getInt("TypeID"));
                customer.setFirstName(rs.getString("First_Name"));
                customer.setLastName(rs.getString("Last_Name"));
                customer.setAddressLine1(rs.getString("Address_Line1"));
                customer.setAddressLine2(rs.getString("Address_Line2"));
                customer.setCounty(rs.getString("County"));
                customer.setPostCode(rs.getString("Postal_Code"));
                customer.setPhoneNumber(rs.getString("Phone_Number"));
                if(rs.getString("Company")==null || rs.getString("Company").equalsIgnoreCase("0")){
                    customer.setCompanyName("N/A");
                }else{
                    customer.setCompanyName(rs.getString("Company"));
                }
            }
            stmt.close();
            rs.close();
        } catch (SQLException ex) {
           System.out.println("DRBooking DatebaseHandle Class, "+ex);
        }

        return customer;
    }

    public String getVehicleMakeByID(int id) {
        String make = "";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Make WHERE ID =" + id + ";");
            while (rs.next()) {
                make = rs.getString("Name");
            }
            stmt.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("DRBooking DatebaseHandle Class, "+ex);
        }

        return make;
    }

    public String getVehicleModelByID(int id) {
        String model = "";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Model WHERE ID =" + id + ";");

            while (rs.next()) {
                model = rs.getString("Name");
            }
            stmt.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("DRBooking DatebaseHandle Class, "+ex);
        }

        return model;
    }

    public String getVehicleEngineByID(int id) {
        String engineSize = "";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Engine_Size WHERE ID =" + id + ";");

            while (rs.next()) {
                engineSize = rs.getString("Column");
            }
            stmt.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("DRBooking DatebaseHandle Class, "+ex);
        }

        return engineSize;
    }
    
    public String getVehicleFuelByID(int id) {
        String fuelType = "";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Fuel_Type WHERE ID =" + id + ";");
            while (rs.next()) {
                fuelType = rs.getString("Size");
            }
            stmt.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("DRBooking DatebaseHandle Class, "+ex);
        }

        return fuelType;
    }

    public String getVehicleColourByID(int id) {
        String colour = "";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Colour WHERE ID =" + id + ";");
            while (rs.next()) {
                colour = rs.getString("Name");
            }
            stmt.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("DRBooking DatebaseHandle Class, "+ex);
        }

        return colour;
    }

    public String getVehicleWarrantyByID(int id) {
        String warranty = "";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Warranty WHERE ID =" + id + ";");
            while (rs.next()) {
                warranty = rs.getString("Company");
            }
            stmt.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("DRBooking DatebaseHandle Class, "+ex);
        }

        return warranty;
    }

    public String getBookingTypeByID(int id) {
        String type = "";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("Select * from Booking_Type WHERE ID =" + id + ";");
            while (rs.next()) {
                type = rs.getString("Name");
            }
            stmt.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("DRBooking DatebaseHandle Class, "+ex);
        }
        return type;
    }

    ///////////////
    
    public Mechanic getMechanicByID(int id) {
        Mechanic mechanic = new Mechanic();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("Select * from Mechanics WHERE ID =" + id + ";");
            while (rs.next()) {
                mechanic.setID(rs.getInt("ID"));
                mechanic.setFirstName(rs.getString("First_Name"));
                mechanic.setLastName(rs.getString("Last_Name"));
                mechanic.setUsername(rs.getString("Username"));
                mechanic.setPassword(rs.getString("Password"));
                mechanic.setRate(rs.getDouble("Rate"));
            }
            stmt.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("DRBooking DatebaseHandle Class, "+ex);
        }

        return mechanic;
    }
    
    public ArrayList<Mechanic> getAllMechanics() {
        ArrayList<Mechanic> allMechanic = new ArrayList<Mechanic>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("Select * from Mechanics;");
            while (rs.next()) {
                Mechanic mechanic = new Mechanic();
                mechanic.setID(rs.getInt("ID"));
                mechanic.setFirstName(rs.getString("First_Name"));
                mechanic.setLastName(rs.getString("Last_Name"));
                mechanic.setUsername(rs.getString("Username"));
                mechanic.setPassword(rs.getString("Password"));
                mechanic.setRate(rs.getDouble("Rate"));
                allMechanic.add(mechanic);
            }
            stmt.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return allMechanic;
    }

    public Mechanic getMechanicByName(String name) {
        String[] names = name.split(" ");

        Mechanic mechanic = new Mechanic();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("Select * from Mechanics WHERE First_Name LIKE '%" + names[0] + "%';");
            while (rs.next()) {
                mechanic.setID(rs.getInt("ID"));
                mechanic.setFirstName(rs.getString("First_Name"));
                mechanic.setLastName(rs.getString("Last_Name"));
                mechanic.setUsername(rs.getString("Username"));
                mechanic.setPassword(rs.getString("Password"));
                mechanic.setRate(rs.getDouble("Rate"));
            }
            stmt.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("DRBooking DatebaseHandle Class, "+ex);
        }
        return mechanic;
    }

    /////////////
    
    public void addBooking(DRBooking booking) {
        try {
            
            String sql1 = "INSERT INTO Bookings ("
                    + "VehiclesID, "
                    + "Booking_TypeID, "
                    + "Time_Start, "
                    + "Time_End, "
                    + "Bay_Number, "
                    + "MechanicsID)"
                    + " VALUES (?,?,?,?,?,?);";
            PreparedStatement ps1 = connection.prepareStatement(sql1);
            ps1.setInt(1, booking.getVehicle().getID());
            ps1.setInt(2, booking.getBookingType());
            ps1.setString(3, parseToSQLDate(booking.getStartDate()).toString());
            ps1.setString(4, parseToSQLDate(booking.getEndDate()).toString());
            ps1.setInt(5, booking.getBayNumber());
            ps1.setInt(6, booking.getMechanic().getID());
            ps1.executeUpdate();
            ps1.close();
            
            String sql2 = "UPDATE Vehicles SET Mileage = ? WHERE ID = ?;";
            PreparedStatement ps2 = connection.prepareStatement(sql2);
            ps2.setInt(1, booking.getVehicle().getMileage());
            ps2.setInt(2, booking.getVehicle().getID());
            ps2.executeUpdate();
            ps2.close();
            

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ID FROM Bookings "
                    + "ORDER BY ID DESC LIMIT 1");
            int id = rs.getInt("ID");
            rs.close();
            stmt.close();

            String sql3 = "INSERT INTO Service_Reports ("
                    + "BookingsID, "
                    + "Result, "
                    + "Description, "
                    + "Hours_Spent, "
                    + "Mileage) "
                    + "VALUES (?,?,?,?,?)";
            PreparedStatement ps3 = connection.prepareStatement(sql3);
            ps3.setInt(1, id);
            ps3.setBoolean(2, booking.isBookingFinished());
            ps3.setString(3, booking.getFault());
            ps3.setInt(4, booking.getEndDate().getTime().getHours()-booking.getStartDate().getTime().getHours());
            ps3.setInt(5, booking.getVehicle().getMileage());
            ps3.executeUpdate();
            ps3.close();

            Statement stmt1 = connection.createStatement();
            ResultSet rs1 = stmt1.executeQuery("SELECT ID FROM Service_Reports "
                    + "ORDER BY ID DESC LIMIT 1");
            int servicereportID = rs1.getInt("ID");
            rs1.close();
            stmt1.close();
            

            ObservableList<BasketPart> p = booking.getPartList();
            if (p != null) {
                for (int i = 0; i < p.size(); i++) {
                    String sql4 = "INSERT INTO Service_Parts ("
                            + "PartsID, "
                            + "Service_ReportsID, "
                            + "Quantity )"
                            + "VALUES (?,?,?)";
                    PreparedStatement ps4 = connection.prepareStatement(sql4);
                    ps4.setInt(1, p.get(i).getId());
                    ps4.setInt(2, servicereportID);
                    ps4.setInt(3, Integer.parseInt(p.get(i).getWithdraw()));
                    ps4.executeUpdate();
                    ps4.close();
                }
            }
    
            connection.close();
        } catch (SQLException ex) {
            System.out.println("DRBooking DatebaseHandle(Adding) Class, "+ex);
        }
    }
    
    public void updateBooking(DRBooking booking, int id) throws ParseException {
        int serviceRptID = getServiceReportID(id);
        try {
            connection.setAutoCommit(false);
            Statement stmt = connection.createStatement();
            connection.setAutoCommit(false);
            stmt.executeUpdate("DELETE FROM Service_Parts WHERE Service_ReportsID =" + serviceRptID + ";");
            stmt.close();

            Vehicle vehicle = booking.getVehicle();
            Mechanic mechanic = booking.getMechanic();

            String sql = "UPDATE Bookings SET "
                    + "VehiclesID = ?, "
                    + "Booking_TypeID = ?, "
                    + "Time_Start = ?, "
                    + "Time_End = ?, "
                    + "Bay_Number = ?, "
                    + "MechanicsID = ?"
                    + "WHERE ID = ?";
            PreparedStatement ps1 = connection.prepareStatement(sql);
            ps1.setInt(1, booking.getVehicle().getID());
            ps1.setInt(2, booking.getBookingType());
            ps1.setString(3, parseToSQLDate(booking.getStartDate()).toString());
            ps1.setString(4, parseToSQLDate(booking.getEndDate()).toString());
            ps1.setInt(5, booking.getBayNumber());
            ps1.setInt(6, booking.getMechanic().getID());
            ps1.setInt(7, id);
            ps1.executeUpdate();
            ps1.close();

            
            String sql2 = "UPDATE Vehicles SET Mileage = ? WHERE ID = ?;";
            PreparedStatement ps2 = connection.prepareStatement(sql2);
            ps2.setInt(1, booking.getVehicle().getMileage());
            ps2.setInt(2, booking.getVehicle().getID());
            ps2.executeUpdate();
            ps2.close();

            
            String sql3 = "UPDATE Service_Reports SET "
                    + "Result = ?, "
                    + "Description = ?, "
                    + "Mileage = ?, "
                    + "Hours_Spent = ? "
                    + "WHERE BookingsID = ?";
            PreparedStatement ps3 = connection.prepareStatement(sql3);
            ps3.setBoolean(1, booking.isBookingFinished());
            ps3.setString(2, booking.getFault());
            ps3.setInt(3,booking.getVehicle().getMileage());  
            ps3.setInt(4, booking.getEndDate().getTime().getHours()-booking.getStartDate().getTime().getHours()); 
            ps3.setInt(5, id);
            ps3.executeUpdate();
            ps3.close();

            
            ObservableList<BasketPart> p = booking.getPartList();
            if (p != null) {
                for (int i = 0; i < p.size(); i++) {
                    String sql4 = "INSERT INTO Service_Parts ("
                            + "PartsID, "
                            + "Service_ReportsID, "
                            + "Quantity)"
                            + "VALUES (?,?,?)";
                    PreparedStatement ps4 = connection.prepareStatement(sql4);
                    ps4.setInt(1, p.get(i).getId());
                    ps4.setInt(2, serviceRptID);
                    ps4.setInt(3, Integer.parseInt(p.get(i).getWithdraw()));
                    ps4.executeUpdate();
                    ps4.close();
                }
            }

            connection.commit();
            connection.close();
        } catch (SQLException ex) {
            System.out.println("DRBooking DatebaseHandle(Update) Class, "+ex);
        }  
    }
    
    public void delectBooking(DRBooking b,int id) throws SQLException {
        try {
            connection.setAutoCommit(false);
            
            Statement stmt =null;
            stmt = connection.createStatement();
            //stmt.executeUpdate("DELETE FROM Bills WHERE BookingsID ="+ id +";");
            stmt.executeUpdate("DELETE FROM Service_Parts WHERE Service_ReportsID ="+getServiceReportID(id)+";");
            stmt.executeUpdate("DELETE FROM Service_Reports WHERE BookingsID=" + id + ";");
            stmt.executeUpdate("DELETE FROM bookings WHERE id=" + id + ";");
            connection.commit();
            connection.close();
        } catch (SQLException ex) {
            System.out.println("DRBooking DatebaseHandle(Delete) Class, "+ex);
        } 
    }
    
    public void bookingCompleted(DRBooking booking,int id){
        try {
            double bill=0;
            connection = DatabaseConnection.dbConnect();
            connection.setAutoCommit(false);
           
            Statement stmt2 = connection.createStatement();
            ResultSet rs2 = stmt2.executeQuery("SELECT * FROM Accounts"
                    + " WHERE CustomerID = "+booking.getVehicle().getCustomer().getCustomerID());
            int accountID = rs2.getInt("ID");
            rs2.close();
            stmt2.close();
            
            
            String sql5 = "INSERT INTO Bills ("
                    + "AccountsID, "
                    + "BookingsID, "
                    + "Amount_Due, "
                    + "Settled )"
                    + " VALUES (?,?,?,?)";
            PreparedStatement ps5 = connection.prepareStatement(sql5);
            ps5.setInt(1, accountID);
            ps5.setInt(2, id);
            ps5.setDouble(3, booking.getBookingFee());
            ps5.setInt(4,0);
            ps5.executeUpdate();
            ps5.close();
            
            
            String sql = "UPDATE Accounts SET Outstanding_Balance = Outstanding_Balance + ? WHERE ID = ?;";
 
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setDouble(1, booking.getBookingFee());
			preparedStatement.setInt(2, accountID );
                        
			preparedStatement.executeUpdate();
                        preparedStatement.close();
		} catch (SQLException e) {
 
			System.out.println(e.getMessage());
 
		}    
 
            connection.commit();
            connection.close();
        } catch (SQLException ex) {
             System.out.println("DRBooking DatebaseHandle(Finish Booking) Class, "+ex);
        }
        
    }

    /////////////
    
    public ArrayList<Integer> getFreeBayNumber(java.util.Date start, java.util.Date end) {
        ArrayList<Integer> list = new ArrayList<>();
        java.sql.Timestamp sqlStart = new java.sql.Timestamp(start.getTime());
        java.sql.Timestamp sqlEnd = new java.sql.Timestamp(end.getTime());
        try {
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM Bookings WHERE (( Time_Start > '"+sqlStart
              +"' And Time_Start < '"+sqlEnd+"')  Or (Time_End > '"+sqlStart
              +"' And Time_End < '"+sqlEnd+"')"
              + " Or (Time_Start < '"+sqlStart
              +"' And Time_End > '"+sqlEnd+"' ) );";
            
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(rs.getInt("Bay_Number"));
            }
            stmt.close();
        } catch (Exception e) {
           System.out.println("DRBooking DatebaseHandle(Get Bay) Class, "+e);
        }
        return list;
    }
    
    private java.sql.Timestamp parseToSQLDate(GregorianCalendar c) {
        return new java.sql.Timestamp(c.getTime().getTime());
    }

    private GregorianCalendar parseDate(String d) {
        String[] datetime = d.split(" ");
        String[] date = datetime[0].split("-");
        int year = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]) - 1;
        int day = Integer.parseInt(date[2]);
        String[] time = datetime[1].split(":");
        int hour = Integer.parseInt(time[0]);
        int min = Integer.parseInt(time[1]);
        return new GregorianCalendar(year, month, day, hour, min);
    }

    public Date parseTime(String str) throws ParseException {
        DateFormat format = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        Date date = (Date) format.parse(str);
        return date;
    }
    
    public String datetoStringDate(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String datestring = dateFormat.format(date); 
        return datestring;
    }
    
    public String datetoStringTime(Date time){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        String datestring = dateFormat.format(time); 
        return datestring;
    }

}
