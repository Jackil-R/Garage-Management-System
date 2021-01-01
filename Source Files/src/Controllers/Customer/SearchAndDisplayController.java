package Controllers.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SearchAndDisplayController implements Initializable {
    int selectedID;
    boolean businessAccountInUse;
    boolean drbWindowOpen = false;
    boolean smbWindowOpen = false;
    boolean smbBookingsPhoneCall = false;
    ArrayList<Integer> smbExpiryMOTVehicleIDs;
    ArrayList<String> motExpiryDates;
    ArrayList<String> smbMotExpiryDates;
    ArrayList<Integer> motVehicleIDs;
    
    ArrayList<Integer> smbLastServiceVehicleIDs;
    ArrayList<String> lastServiceDates;
    ArrayList<String> smbLastServiceDates;
    ArrayList<Integer> lastServiceVehicleIDs;
    @FXML Button smbBookingsPhoneCallBtn;
    
    boolean drbBookingsPhoneCall = false;
    ArrayList<Integer> drbPhoneCallBookingIDs; 
    @FXML Button drbBookingsPhoneCallBtn;
    
    Connection connection;
    AccountDatabase balanceAccess;
    CustomerDatabase access;
    BookingsDatabase bookingAccess;
    VehicleDatabase vehicleAccess;
    CustomerAccount account;
    
    @FXML Button editBtn;
    @FXML Button removeBtn;
    @FXML Button payBillsBtn;
    @FXML Button vehiclesOwnedBtn;
    @FXML Button pastBookingsBtn;
    @FXML Button futureBookingsBtn;
    @FXML Button loadDetailsBtn;
    
    @FXML TextField searchCustomerTxtField;
    @FXML TextField searchBusinessTxtField;
    @FXML TextField businessTxtField;
    
    @FXML Label componentTitle;
    @FXML Label nameLbl;
    @FXML Label customerIDLbl;
    @FXML Label addressLine1Lbl;
    @FXML Label addressLine2Lbl;
    @FXML Label countyLbl;
    @FXML Label postCodeLbl;
    @FXML Label phoneNumberLbl;
    @FXML Label customerIDsolidLbl;
    @FXML Label outstandingBalanceLbl;
    @FXML Label balancePlaceHolderLbl;
    
    @FXML AnchorPane vehiclesOwnedTable;
    @FXML AnchorPane pastBookingsTable;
    @FXML AnchorPane futureBookingsTable;
    
    @FXML private SearchModuleController searchController;
    
    @FXML private TableView vehiclesTable;
    @FXML private TableColumn vehicleName;
    @FXML private TableColumn vehicleMake;
    @FXML private TableColumn vehicleModel;
    
    @FXML private TableView pastTable;
    @FXML private TableColumn pastBookingName;
    @FXML private TableColumn pastBookingCost;
    @FXML private TableColumn pastBookingDate;
    @FXML private TableColumn pastBookingStartTime;
    @FXML private TableColumn pastBookingEndTime;
    
    @FXML private TableView futureTable;
    @FXML private TableColumn futureBookingName;
    @FXML private TableColumn futureBookingCost;
    @FXML private TableColumn futureBookingDate;
    @FXML private TableColumn futureBookingStartTime;
    @FXML private TableColumn futureBookingEndTime;

    @Override
    public void initialize(URL url, ResourceBundle rb) {  
        hideAllTables();
        try {
            checkDiagonisisRepairBookings();    
        } 
        catch (ParseException ex) {
        }
        try {
            checkSMB_MOT_Expiry();    
        } 
        catch (ParseException ex) {
        }
        try {
            checkLastServiceExpiry();    
        } 
        catch (ParseException ex) {
        }
        
        String alertText = "";
        if(drbBookingsPhoneCall) {
            drbBookingsPhoneCallBtn.setVisible(true);
            alertText = alertText + "You have phone calls to make for Diagnosis and Repair Bookings.\n";
        }
        if(smbBookingsPhoneCall) {
            smbBookingsPhoneCallBtn.setVisible(true);
            alertText = alertText + "You have phone calls to make for Scheduled Maintenance Bookings.";
        }
        try {
            if(!alertText.isEmpty()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Customer/PhoneCallRemindersDialog.fxml"));   
                Parent root = (Parent)loader.load();          
                PhoneCallRemindersDialogController controller = loader.<PhoneCallRemindersDialogController>getController();
                controller.initialData(alertText);
                Stage phoneCallStage = new Stage();
                Scene phoneCallScene = new Scene(root); 
                phoneCallStage.setScene(phoneCallScene);
                phoneCallStage.setResizable(false);
                phoneCallStage.setTitle("Phone Call Reminders");
                phoneCallStage.getIcons().add(new Image("/images/gears_icon.png"));
                phoneCallStage.showAndWait();
            }
        }
        catch(IOException e){
        }     
    }
    
    private void checkSMB_MOT_Expiry() throws ParseException {
        
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        vehicleAccess = new VehicleDatabase(connection);
        /* *** * *** * *** * *** * *** * */
        
        int totalVehicles = vehicleAccess.getTotalVehicles();

        motVehicleIDs = new ArrayList<Integer>();
        //we want all of the vehicleIDs for bookings which are smb type=1/2 
        
        for (int i = 1; i <= totalVehicles; i++) {
            motVehicleIDs.add(i);
        }
                
        motExpiryDates = new ArrayList<String>();
        
        for (int i = 0; i < motVehicleIDs.size(); i++) {
            motExpiryDates.add(vehicleAccess.getMOTExpire(motVehicleIDs.get(i)));
        }
        
        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch (SQLException ex) {
            System.out.println("checkSMB_MOT_Expiry catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */
        
        
        ArrayList<Date> motExpiryDatesFormatted = new ArrayList<Date>();
        
        for (int i = 0; i < motExpiryDates.size(); i++) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            motExpiryDatesFormatted.add(format.parse(motExpiryDates.get(i))); 
        }
        
        Date today = getTodaysDate();
        smbExpiryMOTVehicleIDs = new ArrayList<Integer>();
        smbMotExpiryDates = new ArrayList<String>();
        
        for (int i = 0; i < motExpiryDates.size(); i++) {
            Calendar expiryMinusOneMonth = Calendar.getInstance();
            expiryMinusOneMonth.setTime(motExpiryDatesFormatted.get(i));
            expiryMinusOneMonth.add(Calendar.MONTH, -1);
            Date expiryMinusMonth = expiryMinusOneMonth.getTime();
            if(expiryMinusMonth.equals(today)) {           
                DateFormat dfFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                Date ftDate = motExpiryDatesFormatted.get(i);
                String formatString = dfFormat.format(ftDate);

                smbMotExpiryDates.add(formatString);
                smbExpiryMOTVehicleIDs.add(motVehicleIDs.get(i));
                smbBookingsPhoneCall = true;
            }
        }
    }
    
    private void checkLastServiceExpiry() throws ParseException {
        
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        vehicleAccess = new VehicleDatabase(connection);
        /* *** * *** * *** * *** * *** * */
        int totalVehicles = vehicleAccess.getTotalVehicles();
        
        lastServiceVehicleIDs = new ArrayList<Integer>();
        for (int i = 1; i <= totalVehicles; i++) {
            lastServiceVehicleIDs.add(i);
        }
        
        lastServiceDates = new ArrayList<String>();
        
        for (int i = 0; i < lastServiceVehicleIDs.size(); i++) {
            lastServiceDates.add(vehicleAccess.getLastService(lastServiceVehicleIDs.get(i)));
        }
        
        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch (SQLException ex) {
            System.out.println("checkLastServiceExpiry catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */
        ArrayList<Date> lastServiceDatesFormatted = new ArrayList<Date>();
        
        for (int i = 0; i < lastServiceDates.size(); i++) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            lastServiceDatesFormatted.add(format.parse(lastServiceDates.get(i)));            
        }
        
        Date today = getTodaysDate();
        smbLastServiceVehicleIDs = new ArrayList<Integer>();
        
        smbLastServiceDates = new ArrayList<String>();
        
        //need to minus 11 months and check if equal
        
        for (int i = 0; i < lastServiceDates.size(); i++) {
            Calendar lastServicePlusElevenMonths = Calendar.getInstance();
            lastServicePlusElevenMonths.setTime(lastServiceDatesFormatted.get(i));
            lastServicePlusElevenMonths.add(Calendar.MONTH, +11);
            Date lastServicePlusMonths = lastServicePlusElevenMonths.getTime();
            if(lastServicePlusMonths.equals(today)) {
                smbLastServiceVehicleIDs.add(lastServiceVehicleIDs.get(i));
                
                DateFormat dfFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                Date ftDate = lastServiceDatesFormatted.get(i);
                String formatString = dfFormat.format(ftDate);               
                smbLastServiceDates.add(formatString);
                smbBookingsPhoneCall = true;
            }
        }
    }
    
    private Date getTodaysDate() {
        Calendar calendarForToday = Calendar.getInstance();
        calendarForToday.setTime(new Date());
        calendarForToday.set(Calendar.HOUR_OF_DAY, 0);
        calendarForToday.set(Calendar.MINUTE, 0);
        calendarForToday.set(Calendar.SECOND, 0);
        calendarForToday.set(Calendar.MILLISECOND, 0);
        return calendarForToday.getTime();
    }
    
    private void checkDiagonisisRepairBookings() throws ParseException {
        ArrayList<Integer> bookingIDs = new ArrayList<Integer>();
        
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        bookingAccess = new BookingsDatabase(connection);
        /* *** * *** * *** * *** * *** * */
        
        int totalBookings = bookingAccess.getTotalBookings();
        
        ArrayList<String> bookingTimeStart = new ArrayList<String>();
        ArrayList<Integer> bookingTypeIDs = new ArrayList<Integer>();
        
        for (int i = 1; i <= totalBookings; i++) {
            if(bookingAccess.getBookingExists(i)) {
                bookingTimeStart.add(bookingAccess.getTimeStart(i));
                bookingTypeIDs.add(bookingAccess.getBookingTypeIDByID(i)); 
                bookingIDs.add(i); 
            }
        }

        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch (SQLException ex) {
            System.out.println("checkDiagonisisRepairBookings catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */

        ArrayList<Date> bookingDatesFormatted = new ArrayList<Date>();
        
        for (int i = 0; i < bookingTimeStart.size(); i++) {
            String[] temp = bookingTimeStart.get(i).split(" ");
            bookingTimeStart.set(i, temp[0]);
        }
        
        for (int i = 0; i < bookingTimeStart.size(); i++) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            bookingDatesFormatted.add(format.parse(bookingTimeStart.get(i)));            
        }

        Date today = getTodaysDate();

        drbPhoneCallBookingIDs = new ArrayList<Integer>();
        
        for (int i = 0; i < bookingTimeStart.size(); i++) {
            Calendar timeStartMinusOneDay = Calendar.getInstance();
            timeStartMinusOneDay.setTime(bookingDatesFormatted.get(i));
            timeStartMinusOneDay.add(Calendar.DAY_OF_MONTH, -1);
            Date bookingMinusDay = timeStartMinusOneDay.getTime();
            if( (bookingMinusDay.equals(today)) && (bookingTypeIDs.get(i)==3) ) {
                drbPhoneCallBookingIDs.add(bookingIDs.get(i));
                drbBookingsPhoneCall = true;
            }
        }
    }

    @FXML
    private void loadDetails(ActionEvent event) {   
        firstStageButtonDisabler();
        selectedID = searchController.getSelectedID();
        if(selectedID == -1) {
            componentTitle.setText("Please search for a customer before loading details.");
            return;
        }
        else if(selectedID == 0) {
            componentTitle.setText("No customer found with that search term.");
            return;
        }
        
        account = searchController.getCustomerAccountFromSearch();
        setSecondStageText(account);
    }
    
    @FXML
    private void drbBookingBtnPressed(ActionEvent event) {
        if(drbWindowOpen)
            return;
        drbWindowOpen = true;
        
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        bookingAccess = new BookingsDatabase(connection);
        /* *** * *** * *** * *** * *** * */
        
        ArrayList<Integer> vehicleIDsForDRBPhoneCall = new ArrayList<Integer>();
        ArrayList<String> timeStartDRBPhoneCall = new ArrayList<String>();
        
        for (int i = 0; i < drbPhoneCallBookingIDs.size(); i++) {
            int vehicleID = bookingAccess.getVehicleID(drbPhoneCallBookingIDs.get(i));
            vehicleIDsForDRBPhoneCall.add(vehicleID);
            timeStartDRBPhoneCall.add(bookingAccess.getTimeStart(drbPhoneCallBookingIDs.get(i)));
        }
        
        for (int i = 0; i < drbPhoneCallBookingIDs.size(); i++) {
            String newString = timeStartDRBPhoneCall.get(i).substring(0,16);
            timeStartDRBPhoneCall.set(i, newString);
        }
        
        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch (SQLException ex) {
            System.out.println("drbBookingBtnPressed (booking) catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */
        
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        vehicleAccess = new VehicleDatabase(connection);
        /* *** * *** * *** * *** * *** * */
        
        ArrayList<Integer> customerIDsForDRBPhoneCall = new ArrayList<Integer>();
        ArrayList<String> vehicleRegistrationsForDRBPhoneCall = new ArrayList<String>();
        
        for (int i = 0; i < drbPhoneCallBookingIDs.size(); i++) {
            int drbCustomerID = vehicleAccess.getCustomerID(vehicleIDsForDRBPhoneCall.get(i));
            String vehicleReg = vehicleAccess.getVehicleRegistrationNumber(vehicleIDsForDRBPhoneCall.get(i));
            customerIDsForDRBPhoneCall.add(drbCustomerID);
            vehicleRegistrationsForDRBPhoneCall.add(vehicleReg);
        }
        
        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch (SQLException ex) {
            System.out.println("drbBookingBtnPressed (vehicle) catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */
        
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        access = new CustomerDatabase(connection);
        /* *** * *** * *** * *** * *** * */

        ArrayList<String> drbName = new ArrayList<String>();       
        ArrayList<String> drbPhoneNumber = new ArrayList<String>();
        
        for (int i = 0; i < drbPhoneCallBookingIDs.size(); i++) {
            int type = 1;
            if((access.getBusinessName(customerIDsForDRBPhoneCall.get(i))).equals(""))
                type = 2;

            CustomerAccount drbCustomerAccount = new CustomerAccount(customerIDsForDRBPhoneCall.get(i), type);
            
            if(type==1)
                drbName.add(drbCustomerAccount.getCompanyName());
            else
                drbName.add(drbCustomerAccount.getFullName());
            
            drbPhoneNumber.add(drbCustomerAccount.getPhoneNumber());
        }
        
        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch (SQLException ex) {
            System.out.println("drbBookingBtnPressed (customer) catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */
        String empty = "";
        DisplayDRBTable drbTbl = new DisplayDRBTable(drbName, vehicleRegistrationsForDRBPhoneCall, drbPhoneNumber, empty, timeStartDRBPhoneCall);
        drbTbl.init(drbName, vehicleRegistrationsForDRBPhoneCall, drbPhoneNumber, empty, timeStartDRBPhoneCall);
        VBox root = new VBox();
        root.getChildren().addAll(drbTbl.getTable());
        Scene scene = new Scene(root, 700, 400);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Diagnosis and Repair required phone calls");
        stage.getIcons().add(new Image("/images/gears_icon.png"));
        stage.setAlwaysOnTop(true);
        stage.showAndWait();
        drbWindowOpen = false;
    }
    
    @FXML
    private void smbBookingBtnPressed(ActionEvent event) {
        if(smbWindowOpen)
            return;
        drbWindowOpen = true;
        
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        vehicleAccess = new VehicleDatabase(connection);
        /* *** * *** * *** * *** * *** * */
        
        ArrayList<Integer> customerIDsForMOTPhoneCall = new ArrayList<Integer>();
        ArrayList<Integer> customerIDsForLastServicePhoneCall = new ArrayList<Integer>();
        ArrayList<String> smbVehicleRegistrations = new ArrayList<String>();
        
        for (int i = 0; i < smbExpiryMOTVehicleIDs.size(); i++) {
            int smbCustomerID = vehicleAccess.getCustomerID(smbExpiryMOTVehicleIDs.get(i));
            String vehicleReg = vehicleAccess.getVehicleRegistrationNumber(smbExpiryMOTVehicleIDs.get(i));
            System.out.println(smbExpiryMOTVehicleIDs);
            System.out.println(vehicleReg);
            customerIDsForMOTPhoneCall.add(smbCustomerID);  
            smbVehicleRegistrations.add(vehicleReg);
        }
        
        for (int i = 0; i < smbLastServiceVehicleIDs.size(); i++) {
            int smbCustomerID = vehicleAccess.getCustomerID(smbLastServiceVehicleIDs.get(i));
            String vehicleReg = vehicleAccess.getVehicleRegistrationNumber(smbLastServiceVehicleIDs.get(i));
            customerIDsForLastServicePhoneCall.add(smbCustomerID);    
            smbVehicleRegistrations.add(vehicleReg);
            
        }
        
        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch (SQLException ex) {
            System.out.println("smbBookingBtnPressed (vehicle) catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */
        
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        access = new CustomerDatabase(connection);
        /* *** * *** * *** * *** * *** * */

        ArrayList<String> smbExpiryName = new ArrayList<String>();       
        ArrayList<String> smbExpiryPhoneNumber = new ArrayList<String>();
        //add more table column's here

        for (int i = 0; i < smbExpiryMOTVehicleIDs.size(); i++) {
            int type = 1;
            if(access.getBusinessName(customerIDsForMOTPhoneCall.get(i)).equals(""))
                type = 2;

            CustomerAccount smbExpiryCustomerAccount = new CustomerAccount(customerIDsForMOTPhoneCall.get(i), type);
            
            if(type==1)
                smbExpiryName.add(smbExpiryCustomerAccount.getCompanyName());
            else
                smbExpiryName.add(smbExpiryCustomerAccount.getFullName());
            
            smbExpiryPhoneNumber.add(smbExpiryCustomerAccount.getPhoneNumber());
        }
        
        ArrayList<String> smbLastServiceName = new ArrayList<String>();       
        ArrayList<String> smbLastServicePhoneNumber = new ArrayList<String>();
        //add more table column's here

        for (int i = 0; i < smbLastServiceVehicleIDs.size(); i++) {
            int type = 1;
            if(access.getBusinessName(customerIDsForLastServicePhoneCall.get(i)).equals(""))
                type = 2;

            CustomerAccount smbLastServiceCustomerAccount = new CustomerAccount(customerIDsForLastServicePhoneCall.get(i), type);
            
            if(type==1)
                smbLastServiceName.add(smbLastServiceCustomerAccount.getCompanyName());
            else
                smbLastServiceName.add(smbLastServiceCustomerAccount.getFullName());
            
            smbLastServicePhoneNumber.add(smbLastServiceCustomerAccount.getPhoneNumber());
        }
        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch (SQLException ex) {
            System.out.println("smbBookingBtnPressed (customer) catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */

        //smbExpiryMOTBookingIDs
        //motExpiryDates
        //motVehicleIDs
        
        //merge
        ArrayList<String> smbName = new ArrayList<String>();
        ArrayList<String> smbBookingType = new ArrayList<String>();
        ArrayList<String> smbPhoneNumber = new ArrayList<String>();
        ArrayList<String> smbDate = new ArrayList<String>();
        
        for (int i = 0; i < smbExpiryName.size(); i++) {
            smbName.add(smbExpiryName.get(i));
            smbBookingType.add("MOT");
            smbPhoneNumber.add(smbExpiryPhoneNumber.get(i));
//            smbDate.add(motExpiryDates.get(i));
            smbDate.add(smbMotExpiryDates.get(i));
        }
        
        for (int i = 0; i < smbLastServiceName.size(); i++) {
            smbName.add(smbLastServiceName.get(i));
            smbBookingType.add("Service");
            smbPhoneNumber.add(smbLastServicePhoneNumber.get(i));
//            smbDate.add(lastServiceDates.get(i));
            smbDate.add(smbLastServiceDates.get(i));
        }
        
        DisplaySMBTable smbTbl = new DisplaySMBTable(smbName, smbVehicleRegistrations, smbBookingType, smbPhoneNumber, smbDate);
        smbTbl.init(smbName, smbVehicleRegistrations, smbBookingType, smbPhoneNumber, smbDate);
        VBox root = new VBox();
        root.getChildren().addAll(smbTbl.getTable());
        Scene scene = new Scene(root, 700, 400);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Scheduled Maintenance Bookings required phone calls");
        stage.getIcons().add(new Image("/images/gears_icon.png"));
        stage.setAlwaysOnTop(true);
        stage.showAndWait();
        smbWindowOpen = false;
    }
    
    @FXML
    private void vehiclesOwnedPressed(ActionEvent event) throws IOException {
        hideAllTables();
        componentTitle.setText("Vehicles Owned");
        
        ArrayList<Integer> vehicleIDs = getVehicleIDs();
        
        if(vehicleIDs.isEmpty()) {
            componentTitle.setText("No registered vehicles found for this customer.");
            return;
        }
        
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        vehicleAccess = new VehicleDatabase(connection);
        /* *** * *** * *** * *** * *** * */
        
        ArrayList<Integer> carTypeIDs = new ArrayList<Integer>();
        ArrayList<Integer> makeIDs = new ArrayList<Integer>();
        ArrayList<Integer> modelIDs = new ArrayList<Integer>();

        for (int i = 0; i < vehicleIDs.size(); i++) {
            ArrayList<Integer> componentIDs = vehicleAccess.getVehicleComponentIDs(vehicleIDs.get(i));
            carTypeIDs.add(componentIDs.get(0));
            makeIDs.add(componentIDs.get(1));
            modelIDs.add(componentIDs.get(2));
        }

        ArrayList<String> vehicleNames = new ArrayList<String>();
        ArrayList<String> vehicleMakes = new ArrayList<String>();
        ArrayList<String> vehicleModels = new ArrayList<String>();

        for(int i=0; i<vehicleIDs.size(); i++) {
            vehicleNames.add(vehicleAccess.getVehicleName(carTypeIDs.get(i)));
            vehicleMakes.add(vehicleAccess.getMakeName(makeIDs.get(i)));
            vehicleModels.add(vehicleAccess.getModelName(modelIDs.get(i)));
        }

        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch (SQLException ex) {
            System.out.println("vehiclesOwnedPressed catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */
        
        vehicleName.setCellValueFactory(new PropertyValueFactory<VehiclesOwned, String>("name"));
        vehicleMake.setCellValueFactory(new PropertyValueFactory<VehiclesOwned, String>("make"));
        vehicleModel.setCellValueFactory(new PropertyValueFactory<VehiclesOwned, String>("model"));
        
        ObservableList<VehiclesOwned> data = FXCollections.observableArrayList();
        
        for (int i = 0; i < vehicleNames.size(); i++) {
            data.add(new VehiclesOwned(vehicleNames.get(i), vehicleMakes.get(i), vehicleModels.get(i)));
        }  
        vehiclesTable.setItems(data);
        vehiclesOwnedTable.setVisible(true);

    }
    
    @FXML
    private void pastBookingsPressed(ActionEvent event) throws ParseException {
        hideAllTables();
        componentTitle.setText("Past Bookings");
         
        ArrayList<Integer> bookingIDs = getBookingIDs();
        
        if(bookingIDs.isEmpty()) {
            componentTitle.setText("No past bookings found for this customer.");
            return;
        }
        
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        bookingAccess = new BookingsDatabase(connection);
        /* *** * *** * *** * *** * *** * */
        
        ArrayList<Integer> bookingTypeIDs = new ArrayList<Integer>();
        ArrayList<String> bookingDates = new ArrayList<String>();
        ArrayList<String> bookingTimeStart = new ArrayList<String>();
        ArrayList<String> bookingTimeEnd = new ArrayList<String>();
        ArrayList<String> bookingName = new ArrayList<String>();
        ArrayList<Integer> bookingPrice = new ArrayList<Integer>();
 
        for (int i = 0; i < bookingIDs.size(); i++) {
            bookingTypeIDs.add(bookingAccess.getBookingTypeIDByID(bookingIDs.get(i)));
            bookingDates.add(bookingAccess.getTimeStart(bookingIDs.get(i)));
            bookingTimeStart.add(bookingAccess.getTimeStart(bookingIDs.get(i)));
            bookingTimeEnd.add(bookingAccess.getTimeEndFromID(bookingIDs.get(i)));  
        }
        
        for (int i = 0; i < bookingTypeIDs.size(); i++) {
            bookingName.add(bookingAccess.getBookingName(bookingTypeIDs.get(i))); 
            bookingPrice.add(bookingAccess.getBookingPrice(bookingTypeIDs.get(i))); 
        }

        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch (SQLException ex) {
            System.out.println("pastBookingsPressed catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */

        for (int i = 0; i < bookingDates.size(); i++) {
            bookingDates.set(i, (bookingDates.get(i).substring(0,10)));
        }
        
        ArrayList<Date> bookingDatesFormatted = new ArrayList<Date>();
        for (int i = 0; i < bookingDates.size(); i++) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            bookingDatesFormatted.add(format.parse(bookingDates.get(i)));
        }
        
        //todays date
        Calendar calendarForToday = Calendar.getInstance();
        calendarForToday.setTime(new Date());
        calendarForToday.set(Calendar.HOUR_OF_DAY, 0);
        calendarForToday.set(Calendar.MINUTE, 0);
        calendarForToday.set(Calendar.SECOND, 0);
        calendarForToday.set(Calendar.MILLISECOND, 0);
        Date today = calendarForToday.getTime();
        
        for (int i = 0; i < bookingDatesFormatted.size(); i++) {
            String tempString = bookingTimeStart.get(i);
            String tempString2 = bookingTimeEnd.get(i);
            String[] splitString = tempString.split(" ");
            String[] splitString2 = tempString2.split(" ");   
            String newString = splitString[1];
            String newString2 = splitString2[1];     
            bookingTimeStart.set(i, newString);
            bookingTimeEnd.set(i, newString2);
        }
        
        for (int i = 0; i < bookingDatesFormatted.size(); i++) {
            String newStringStart = bookingTimeStart.get(i).substring(0,5);
            String newStringEnd = bookingTimeEnd.get(i).substring(0,5);
            bookingTimeStart.set(i, newStringStart);
            bookingTimeEnd.set(i, newStringEnd);
        }
        
        for (int i = 0; i < bookingDatesFormatted.size(); i++) {
            if(bookingDatesFormatted.get(i).after(today)) {
                try{
                    bookingDates.remove(i);
                    bookingTimeStart.remove(i);
                    bookingTimeEnd.remove(i);
                    bookingName.remove(i);
                    bookingPrice.remove(i);
                    bookingDatesFormatted.remove(i);
                }
                catch(IndexOutOfBoundsException e) {
                    componentTitle.setText("No past bookings found for this customer.");
                    return;
                }
                if(bookingDates.isEmpty()) {
                    componentTitle.setText("No past bookings found for this customer.");
                    return;
                }
            }
        }
        
        pastBookingName.setCellValueFactory(new PropertyValueFactory<PastBookings, String>("name"));
        pastBookingCost.setCellValueFactory(new PropertyValueFactory<PastBookings, Integer>("price"));
        pastBookingDate.setCellValueFactory(new PropertyValueFactory<PastBookings, String>("date"));
        pastBookingStartTime.setCellValueFactory(new PropertyValueFactory<PastBookings, String>("timeStart"));
        pastBookingEndTime.setCellValueFactory(new PropertyValueFactory<PastBookings, String>("timeEnd"));
        
        ObservableList<PastBookings> data = FXCollections.observableArrayList();
        
        for (int i = 0; i < bookingName.size(); i++) {
            data.add(new PastBookings(bookingName.get(i), bookingPrice.get(i), bookingDates.get(i), bookingTimeStart.get(i), bookingTimeEnd.get(i)));
        }  
        pastTable.setItems(data);
        pastBookingsTable.setVisible(true);
    }
    
    @FXML
    private void futureBookingsPressed(ActionEvent event) throws ParseException {
        hideAllTables();
        componentTitle.setText("Future Bookings");
        
        ArrayList<Integer> bookingIDs = getBookingIDs();

        if(bookingIDs.isEmpty()) {
            componentTitle.setText("No future bookings found for this customer.");
            return;
        }
        
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        bookingAccess = new BookingsDatabase(connection);
        /* *** * *** * *** * *** * *** * */
        
        ArrayList<Integer> bookingTypeIDs = new ArrayList<Integer>();
        ArrayList<String> bookingDates = new ArrayList<String>();
        ArrayList<String> bookingTimeStart = new ArrayList<String>();
        ArrayList<String> bookingTimeEnd = new ArrayList<String>();
        ArrayList<String> bookingName = new ArrayList<String>();
        ArrayList<Integer> bookingPrice = new ArrayList<Integer>();

        for (int i = 0; i < bookingIDs.size(); i++) {
            bookingTypeIDs.add(bookingAccess.getBookingTypeIDByID(bookingIDs.get(i)));
            bookingDates.add(bookingAccess.getTimeStart(bookingIDs.get(i)));
            bookingTimeStart.add(bookingAccess.getTimeStart(bookingIDs.get(i)));
            bookingTimeEnd.add(bookingAccess.getTimeEndFromID(bookingIDs.get(i)));  
        }

        for (int i = 0; i < bookingTypeIDs.size(); i++) {
            bookingName.add(bookingAccess.getBookingName(bookingTypeIDs.get(i))); 
            bookingPrice.add(bookingAccess.getBookingPrice(bookingTypeIDs.get(i))); 
        }

        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch (SQLException ex) {
            System.out.println("futureBookingsPressed catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */        
        
        for (int i = 0; i < bookingDates.size(); i++) {
            bookingDates.set(i, (bookingDates.get(i).substring(0,10)));
        }
        
        ArrayList<Date> bookingDatesFormatted = new ArrayList<Date>();
        for (int i = 0; i < bookingDates.size(); i++) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            bookingDatesFormatted.add(format.parse(bookingDates.get(i)));
        }
        
        Calendar calendarForToday = Calendar.getInstance();
        calendarForToday.setTime(new Date());
        calendarForToday.set(Calendar.HOUR_OF_DAY, 0);
        calendarForToday.set(Calendar.MINUTE, 0);
        calendarForToday.set(Calendar.SECOND, 0);
        calendarForToday.set(Calendar.MILLISECOND, 0);
        Date today = calendarForToday.getTime();
        
        for (int i = 0; i < bookingDatesFormatted.size(); i++) {
            String tempString = bookingTimeStart.get(i);
            String tempString2 = bookingTimeEnd.get(i);
            String[] splitString = tempString.split(" ");
            String[] splitString2 = tempString2.split(" ");   
            String newString = splitString[1];
            String newString2 = splitString2[1];     
            bookingTimeStart.set(i, newString);
            bookingTimeEnd.set(i, newString2);
        }
        
        for (int i = 0; i < bookingDatesFormatted.size(); i++) {
            String newStringStart = bookingTimeStart.get(i).substring(0,5);
            String newStringEnd = bookingTimeEnd.get(i).substring(0,5);
            bookingTimeStart.set(i, newStringStart);
            bookingTimeEnd.set(i, newStringEnd);
        }

        for (int i = 0; i < bookingDatesFormatted.size(); i++) {
            if(bookingDatesFormatted.get(i).before(today)) {
                try {
                    bookingDates.remove(i);
                    bookingTimeStart.remove(i);
                    bookingTimeEnd.remove(i);
                    bookingName.remove(i);
                    bookingPrice.remove(i);
                    bookingDatesFormatted.remove(i);
                }
                catch(IndexOutOfBoundsException e) {
                    componentTitle.setText("No future bookings found for this customer.");
                    return;
                }
                if(bookingDates.isEmpty()) {
                    componentTitle.setText("No future bookings found for this customer.");
                    return;
                }
            }
        }
        
        futureBookingName.setCellValueFactory(new PropertyValueFactory<FutureBookings, String>("name"));
        futureBookingCost.setCellValueFactory(new PropertyValueFactory<FutureBookings, Integer>("price"));
        futureBookingDate.setCellValueFactory(new PropertyValueFactory<FutureBookings, String>("date"));
        futureBookingStartTime.setCellValueFactory(new PropertyValueFactory<FutureBookings, String>("timeStart"));
        futureBookingEndTime.setCellValueFactory(new PropertyValueFactory<FutureBookings, String>("timeEnd"));
        
        ObservableList<FutureBookings> data = FXCollections.observableArrayList();
        
        for (int i = 0; i < bookingName.size(); i++) {
            data.add(new FutureBookings(bookingName.get(i), bookingPrice.get(i), bookingDates.get(i), bookingTimeStart.get(i), bookingTimeEnd.get(i)));
        }  
        futureTable.setItems(data);
        futureBookingsTable.setVisible(true);
    }
    
    @FXML
    private void openEditContext (ActionEvent event) throws IOException, Exception  {  
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Customer/EditCustomerDetails.fxml"));   
        Parent root = (Parent)loader.load();          
        EditCustomerDetailsController controller = loader.<EditCustomerDetailsController>getController();
        
        controller.initialData(selectedID);
        
        Stage editStage = new Stage();
        Scene editScene = new Scene(root); 
        editStage.setScene(editScene);
        editStage.setResizable(false);
        editStage.setTitle("Edit customer details");
        editStage.getIcons().add(new Image("/images/gears_icon.png"));
        
        editStage.showAndWait();
        if(controller.isSuccessfulEdit()) {
            reset(false);
            componentTitle.setText("Details are out of date, please search for the customer again and load their details.");
        }
    }
       
    @FXML
    private void removeCustomAccountDialog (ActionEvent event) throws IOException, Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Customer/DeleteDialog.fxml"));   
        Parent root = (Parent)loader.load();          
        DeleteDialogController controller = loader.<DeleteDialogController>getController();
        String name;
        
        if(businessAccountInUse)
            name = account.getCompanyName();
        else
            name = account.getFullName();
        
        controller.initialData(selectedID, name);
        
        Stage deleteStage = new Stage();
        Scene deleteScene = new Scene(root); 
        deleteStage.setScene(deleteScene);
        deleteStage.setResizable(false);
        deleteStage.setTitle("Delete customer");        
        deleteStage.getIcons().add(new Image("/images/gears_icon.png")); 
        deleteStage.showAndWait();
        if(controller.getWarning()) {
            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/FXML/Customer/DeleteWarningDialog.fxml"));   
            Parent root2 = (Parent)loader2.load();          
            Stage stage = new Stage();
            Scene scene = new Scene(root2); 
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Warning");        
            stage.getIcons().add(new Image("/images/gears_icon.png")); 
            stage.show();
        }
        else if(controller.getBookingWarning()) {
            FXMLLoader loader3 = new FXMLLoader(getClass().getResource("/FXML/Customer/DeleteWarningDialogForBooking.fxml"));   
            Parent root3 = (Parent)loader3.load();          
            Stage stage = new Stage();
            Scene scene = new Scene(root3); 
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Warning");        
            stage.getIcons().add(new Image("/images/gears_icon.png")); 
            stage.show();
        }

        if(controller.isAccountDeleted())
            reset(true);
    }
    
    @FXML
    private void outstandingBillsPayment(ActionEvent event) throws IOException, Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Customer/OutstandingBills.fxml"));   
        Parent root = (Parent)loader.load();          
        OutstandingBillsController controller = loader.<OutstandingBillsController>getController();
        
        controller.initialData(selectedID);
        
        Stage billsStage = new Stage();
        Scene billsScene = new Scene(root); 
        billsStage.setScene(billsScene);
        billsStage.setResizable(false);
        billsStage.setTitle("Pay for outstanding bills");
        billsStage.getIcons().add(new Image("/images/gears_icon.png"));
        billsStage.showAndWait();
        setOutstandingBalance(selectedID);
    }
    
    @FXML
    private void resetDetails(ActionEvent event) {
        firstStageButtonDisabler();
        try {
            searchController.reset();
        }
        catch (Exception ex) {
        }
    }

    private void setOutstandingBalance(int ID) {
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        balanceAccess = new AccountDatabase(connection);
        /* *** * *** * *** * *** * *** * */
        
        int accountBalance = balanceAccess.getCurrentOutstandingUsingCustomerID(ID);
        
        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch (SQLException ex) {
            System.out.println("setOutstandingBalance catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */
        
        
        if(accountBalance == 0) {
            payBillsBtn.setDisable(true);
            balancePlaceHolderLbl.setText("OUTSTANDING BALANCE:  ");
            outstandingBalanceLbl.setText("SETTLED");
        }
        else {
            payBillsBtn.setDisable(false);   
            balancePlaceHolderLbl.setText("OUTSTANDING BALANCE: £");
            outstandingBalanceLbl.setText(Integer.toString(accountBalance));
        
        }
    } 
    
    private ArrayList<Integer> getVehicleIDs()
    {
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        vehicleAccess = new VehicleDatabase(connection);
        /* *** * *** * *** * *** * *** * */
        
        ArrayList<Integer> vehicleIDs = vehicleAccess.getCustomerVehicles(selectedID);
        
        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch (SQLException ex) {
            System.out.println("getVehicleIDs catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */
        return vehicleIDs;
    }
    
    private ArrayList<Integer> getBookingIDs()
    {
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        vehicleAccess = new VehicleDatabase(connection);
        /* *** * *** * *** * *** * *** * */
        
        int vehicleID = vehicleAccess.getVehicleID(selectedID);
        
        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch (SQLException ex) {
            System.out.println("getBookingIDs catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */
        
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        bookingAccess = new BookingsDatabase(connection);
        /* *** * *** * *** * *** * *** * */
        
        ArrayList<Integer> bookingIDs = bookingAccess.getBookingIDsFromVehicleID(vehicleID);
        
        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch (SQLException ex) {
            System.out.println("getBookingIDs catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */
        return bookingIDs;
    }

    private void hideAllTables() {
        vehiclesOwnedTable.setVisible(false);
        pastBookingsTable.setVisible(false);
        futureBookingsTable.setVisible(false);
    }
    
    private void setSecondStageText(CustomerAccount account) {
        if(account.getTypeOfCustomer()==1) { //business 
            nameLbl.setText(account.getCompanyName());
            businessAccountInUse = true;
        }
        else {
            nameLbl.setText(account.getFullName());
            businessAccountInUse = false;
        }
        customerIDsolidLbl.setOpacity(100);
        customerIDLbl.setText(Integer.toString(account.getCustomerID()));
        addressLine1Lbl.setText(account.getAddressLine1());
        addressLine2Lbl.setText(account.getAddressLine2());
        countyLbl.setText(account.getCounty());
        postCodeLbl.setText(account.getPostCode());
        phoneNumberLbl.setText(account.getPhoneNumber()); 
        setOutstandingBalance(selectedID);
        secondStageButtonEnabler(false);
    }

    private void reset(boolean resetSearch) {
        resetDetails(new ActionEvent());
        if(resetSearch) {
            try {
                searchController.reset();
            }
            catch (Exception ex) {
            }
        }     
    }

    private void firstStageButtonDisabler() {
        vehiclesOwnedBtn.setDisable(true);
        pastBookingsBtn.setDisable(true);
        futureBookingsBtn.setDisable(true);
        editBtn.setVisible(false);
        removeBtn.setVisible(false);
        payBillsBtn.setVisible(false);
        vehiclesOwnedBtn.setVisible(false);
        pastBookingsBtn.setVisible(false);
        futureBookingsBtn.setVisible(false);
        hideAllTables();
        clearCustomerLabels();
    }

    private void secondStageButtonEnabler(boolean flag) {
        vehiclesOwnedBtn.setDisable(flag);
        pastBookingsBtn.setDisable(flag);
        futureBookingsBtn.setDisable(flag);
        editBtn.setVisible(!flag);
        removeBtn.setVisible(!flag);
        payBillsBtn.setVisible(!flag);
        vehiclesOwnedBtn.setVisible(!flag);
        pastBookingsBtn.setVisible(!flag);
        futureBookingsBtn.setVisible(!flag);
    }
    
    private void clearCustomerLabels() {
        componentTitle.setText("");
        customerIDsolidLbl.setOpacity(0);
        nameLbl.setText("");
        customerIDLbl.setText("");
        addressLine1Lbl.setText("");
        addressLine2Lbl.setText("");
        countyLbl.setText("");
        postCodeLbl.setText("");
        phoneNumberLbl.setText("");
        outstandingBalanceLbl.setText("");
        balancePlaceHolderLbl.setText("");
    }
}