package Controllers.Customer;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class OutstandingBillsController {
    int customerID;
    
    @FXML private TableView outstandingBillsTable;
    @FXML private TableColumn billIDColumn;
    @FXML private TableColumn dateOfBillColumn;
    @FXML private TableColumn timeOfBillColumn;
    @FXML private TableColumn billAmountColumn;
    
    TableView<OutstandingBills> table;
    String stringID;
    int clickedID;
    Connection connection;
    AccountDatabase balanceAccess;
    BillsDatabase billsAccess;
    BookingsDatabase bookingAccess;
    
    private ArrayList<Integer> allBills;
    private ArrayList<String> BillsID = new ArrayList<String>();
    private ArrayList<String> dateOfBill = new ArrayList<String>();
    private ArrayList<String> timeOfBill = new ArrayList<String>();
    private ArrayList<String> amountForBill = new ArrayList<String>();
    private ArrayList<String> timeAndDateOfBill = new ArrayList<String>();

    public void initialize(URL url, ResourceBundle rb) {
    } 

    public void initialData(int customer_ID) {  
        customerID = customer_ID;
        allBills = getBillsID();
        loadColumnInfo();
        setTable();
    }
    
    @FXML
    private void billSettledPressed(ActionEvent event) {
        OutstandingBills rowSelection = (OutstandingBills) outstandingBillsTable.getSelectionModel().getSelectedItem();
        int selectedTableBillID = Integer.parseInt(rowSelection.getId());
        
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        billsAccess = new BillsDatabase(connection);
        /* *** * *** * *** * *** * *** * */
        
        //get bill amount
        int billAmount = billsAccess.getBillAmount(selectedTableBillID);
        //set bill to settled
        billsAccess.setBillAsSettled(selectedTableBillID);
        
        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch (SQLException ex) {
            System.out.println("billSettledPressed catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */ 
        
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        balanceAccess = new AccountDatabase(connection);
        /* *** * *** * *** * *** * *** * */
        
        int currentBalance = balanceAccess.getCurrentOutstandingUsingCustomerID(customerID);
        int newBalance = currentBalance - billAmount; 
        balanceAccess.setOutstandingBalance(newBalance, customerID);

        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch (SQLException ex) {
            System.out.println("billSettledPressed catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */ 
        
        allBills = getBillsID();
        if(allBills.isEmpty()) {
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        else {
            BillsID.clear();
            timeOfBill.clear();
            dateOfBill.clear();
            amountForBill.clear();
            loadColumnInfo();
            setTable();
        }
        
    }

    private ArrayList<Integer> getBillsID() {

       /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        balanceAccess = new AccountDatabase(connection);
        /* *** * *** * *** * *** * *** * */
    
        int accountID = balanceAccess.getAccountIDFromCustomerID(customerID);
        
        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch (SQLException ex) {
            System.out.println("getBillsID catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */ 
        
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        billsAccess = new BillsDatabase(connection);
        /* *** * *** * *** * *** * *** * */
        
        ArrayList<Integer> everyBill = billsAccess.getBillID(accountID);
        ArrayList<Integer> bills = new ArrayList<Integer>();
        
        for (int i = 0; i < everyBill.size(); i++) {
            if(billsAccess.getBillSettled(everyBill.get(i)) == 0) {
                bills.add(everyBill.get(i));
            }          
        }
  
        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch (SQLException ex) {
            System.out.println("getBillsID catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */   

        return bills;
    }
    
    private void loadColumnInfo() {
        for (int i = 0; i < allBills.size(); i++) {
            BillsID.add(Integer.toString(allBills.get(i)));
        }
 
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        billsAccess = new BillsDatabase(connection);
        /* *** * *** * *** * *** * *** * */
        
        ArrayList<Integer> bookingIDs = new ArrayList<Integer>();
        for (int i = 0; i < BillsID.size(); i++) {
            bookingIDs.add(billsAccess.getBookingID(allBills.get(i)));
        }
        
        timeAndDateOfBill = getTimeAndDate(bookingIDs);
        getTimeFromBoth();
        getDateFromBoth();
        
        
        for (int i = 0; i < BillsID.size(); i++) {
            int amount = billsAccess.getBillAmount(allBills.get(i));
            amountForBill.add(Integer.toString(amount));
        }
        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch (SQLException e) {
            System.out.println("loadColumnInfo catch: "+e);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */  
    }
    
    private void getTimeFromBoth() {
        for (int i = 0; i < timeAndDateOfBill.size(); i++) {
            String formattedString = timeAndDateOfBill.get(i).substring(11,16);
            timeOfBill.add(formattedString);
        }
    }
    
    private void getDateFromBoth() {
        for (int i = 0; i < timeAndDateOfBill.size(); i++) {
            String formattedString = timeAndDateOfBill.get(i).substring(0,10);
            dateOfBill.add(formattedString);
        }
    }
    private ArrayList<String> getTimeAndDate(ArrayList<Integer> bookingIDs) {
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        bookingAccess = new BookingsDatabase(connection);
        /* *** * *** * *** * *** * *** * */
        
        ArrayList<String> timeAndDate = new ArrayList<String>();
        
        for (int i = 0; i < BillsID.size(); i++) {
            String timeDate = bookingAccess.getTimeStart(bookingIDs.get(i));
            timeAndDate.add(timeDate);
        }
        
        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch (SQLException e) {
            System.out.println("getTimeAndDate catch: "+e);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */  
        return timeAndDate;
    }
    
    private void setTable() {      
        billIDColumn.setCellValueFactory(new PropertyValueFactory<OutstandingBills, String>("id"));
        dateOfBillColumn.setCellValueFactory(new PropertyValueFactory<OutstandingBills, Integer>("date"));
        timeOfBillColumn.setCellValueFactory(new PropertyValueFactory<OutstandingBills, String>("time"));
        billAmountColumn.setCellValueFactory(new PropertyValueFactory<OutstandingBills, String>("amount"));
        
        ObservableList<OutstandingBills> data = FXCollections.observableArrayList();
        
        for (int i = 0; i < allBills.size(); i++) {
            data.add(new OutstandingBills(BillsID.get(i), dateOfBill.get(i), timeOfBill.get(i), amountForBill.get(i)));
        }  
        outstandingBillsTable.setItems(data);
    }

    @FXML
    private void finishedBtnPressed(ActionEvent event) { 
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
}

