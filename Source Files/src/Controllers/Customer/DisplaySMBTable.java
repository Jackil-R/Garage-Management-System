package Controllers.Customer;

import java.sql.SQLException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class DisplaySMBTable extends Application {

    TableView<SMBTable> table;
    ArrayList<String> smbName;
    ArrayList<String> smbRegistration;
    ArrayList<String> smbType;
    ArrayList<String> smbPhoneNumber;
    ArrayList<String> smbDate;
    
    @Override
    public void start(Stage stage) throws Exception {
    }

    public void init(ArrayList<String> name, ArrayList<String> registration, ArrayList<String> bookingType, ArrayList<String> phoneNumber, ArrayList<String> date) {
        smbName = name;
        smbRegistration = registration;
        smbType = bookingType;
        smbPhoneNumber = phoneNumber;
        smbDate = date;
        
        table = new TableView<SMBTable>();
        table.getColumns().addAll(SMBTable.getColumn(table));

        try {
            table.setItems(getRows());
        } 
        catch (SQLException e) {
            System.out.println(e);
        }
    }
        
    public ObservableList<SMBTable> getRows() throws SQLException {
        ObservableList<SMBTable> data = FXCollections.observableArrayList();

        for(int i=0; i<smbName.size(); i++)
            data.addAll(new SMBTable(smbName.get(i), smbRegistration.get(i), smbType.get(i), smbPhoneNumber.get(i), smbDate.get(i)));
        
        return data;
    }

    public TableView<SMBTable> getTable() {
        return table;
    }
   
    public DisplaySMBTable(ArrayList<String> name, ArrayList<String> registration, ArrayList<String> bookingType, ArrayList<String> phoneNumber, ArrayList<String> date) {
        smbName = name;
        smbRegistration = registration;
        smbType = bookingType;
        smbPhoneNumber = phoneNumber;
        smbDate = date;
    }     
}