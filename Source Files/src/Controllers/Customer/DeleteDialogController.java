package Controllers.Customer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class DeleteDialogController {
    int customerIDToBeDeleted;
    boolean deleteSuccess;    
    boolean warningIssued = false;    
    boolean bookingWarning = false;    
    Connection connection = null;
    AccountDatabase balanceAccess = null;
    BookingsDatabase bookingAccess = null;
    EditDatabase access = null;
    VehicleDatabase vehicleAccess = null;
    @FXML Label bodyMessageLbl;
    
    public void initialData(int customerID, String fullName) {
        customerIDToBeDeleted = customerID;
        bodyMessageLbl.setText("Are you sure you want to remove "+fullName+"?");
    }
    
    @FXML
    private void deleteAccount(ActionEvent event) throws IOException, ParseException {
        
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        balanceAccess = new AccountDatabase(connection);
        /* *** * *** * *** * *** * *** * */
        
        int accountBalance = balanceAccess.getCurrentOutstanding(customerIDToBeDeleted);
        
        /*Now closing the connection to the database */
        try {
            connection.close(); 
        } 
        catch (SQLException ex) {
            System.out.println("searchBusiness catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */
        
        boolean hasCurrentOrFutureBookings;
        ArrayList<Integer> vehicleIDs = getAssociatedVehicles();
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        bookingAccess = new BookingsDatabase(connection);
        /* *** * *** * *** * *** * *** * */
        
        ArrayList<Integer> allBookingIDs = new ArrayList<Integer>();
        for (int i = 0; i < vehicleIDs.size(); i++) {
            ArrayList<Integer> bookingIDs = bookingAccess.getBookingIDsFromVehicleID(vehicleIDs.get(i));
            for (int j = 0; j < bookingIDs.size(); j++) {
                allBookingIDs.add(bookingIDs.get(j));
            }
        }
        
        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch (SQLException ex) {
            System.out.println("searchBusiness catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */
        
        
        if(vehicleIDs.isEmpty()) {
            System.out.println("isempty");
            hasCurrentOrFutureBookings = false;
        }
        else {
            hasCurrentOrFutureBookings = checkForCurrentOrFutureBookings(allBookingIDs);
        }
        
        if(accountBalance!=0) {
            warningIssued = true;
            deleteSuccess = false;
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        else if(hasCurrentOrFutureBookings) {
            bookingWarning = true;
            deleteSuccess = false;
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        else {
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        access = new EditDatabase(connection);
        /* *** * *** * *** * *** * *** * */
        
        access.deregisterCustomer(customerIDToBeDeleted);
        System.out.println("Customer deregistered.");

        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch (SQLException ex) {
            System.out.println("searchBusiness catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */
       
        if(vehicleIDs.isEmpty()) {
            deleteSuccess = true;
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        vehicleAccess = new VehicleDatabase(connection);
        /* *** * *** * *** * *** * *** * */

        for (int i = 0; i < vehicleIDs.size(); i++) {
            vehicleAccess.deregisterVehicle(vehicleIDs.get(i));
        }
        System.out.println("Associated customer vehicles deregistered.");
        
        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch (SQLException ex) {
            System.out.println("searchBusiness catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */
        deleteSuccess = true;
        ((Node)(event.getSource())).getScene().getWindow().hide();
        }
    }
    
    private ArrayList<Integer> getAssociatedVehicles() {
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        vehicleAccess = new VehicleDatabase(connection);
        /* *** * *** * *** * *** * *** * */
        ArrayList<Integer> vehicleList = vehicleAccess.getCustomerVehicles(customerIDToBeDeleted);
        
        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch (SQLException ex) {
            System.out.println("searchBusiness catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */
        return vehicleList;
    }
    
    private boolean checkForCurrentOrFutureBookings(ArrayList<Integer> bookingIDs) throws ParseException {
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        bookingAccess = new BookingsDatabase(connection);
        /* *** * *** * *** * *** * *** * */
        
        for (int i = 0; i < bookingIDs.size(); i++) {
            String timeEnd = bookingAccess.getTimeEndFromID(bookingIDs.get(i));
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date timeEndDate = format.parse(timeEnd);
            Date today = getTodaysDate();
            
            if(today.before(timeEndDate)) {
                /*Now closing the connection to the database */
                try {
                    connection.close();
                } 
                catch (SQLException ex) {
                    System.out.println("checkForCurrentOrFutureBookings catch: "+ex);
                }
                /* *** * *** * *** * *** * *** * *** * *** * */
                
                return true;
            }
        }
        
        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch (SQLException ex) {
            System.out.println("checkForCurrentOrFutureBookings catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */

        return false;
    }
    
    private Date getTodaysDate() {
        Calendar calendarForToday = Calendar.getInstance();
        calendarForToday.setTime(new Date());
        return calendarForToday.getTime();
    }
    
    @FXML
    private void cancelDelete(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    public boolean isAccountDeleted() {
        return deleteSuccess;
    }
    
    public boolean getWarning() {
        return warningIssued;
    }
    
    public boolean getBookingWarning() {
        return bookingWarning;
    }
}
