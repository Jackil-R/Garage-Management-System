package Controllers.Customer;

import java.sql.SQLException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class DisplayDRBTable extends Application {

    TableView<DRBTable> table;
    ArrayList<String> drbName;
    ArrayList<String> drbPhoneNumber;
    ArrayList<String> timeStartDRBPhoneCall;
    ArrayList<String> drbRegistration;
    
    @Override
    public void start(Stage stage) throws Exception {
    }

    public void init(ArrayList<String> name, ArrayList<String> registration, ArrayList<String> phoneNumber, String bookingType, ArrayList<String> timeStart) {
        drbName = name;
        drbRegistration = registration;
        drbPhoneNumber = phoneNumber;
        timeStartDRBPhoneCall = timeStart;
        table = new TableView<DRBTable>();
        table.getColumns().addAll(DRBTable.getColumn(table));

        try {
            table.setItems(getRows());
        } 
        catch (SQLException e) {
            System.out.println(e);
        }
    }
        
    public ObservableList<DRBTable> getRows() throws SQLException {
        ObservableList<DRBTable> data = FXCollections.observableArrayList();

        for(int i=0; i<drbName.size(); i++)
            data.addAll(new DRBTable(drbName.get(i), drbRegistration.get(i),drbPhoneNumber.get(i), "Diagnosis and Repair", timeStartDRBPhoneCall.get(i)));
        
        return data;
    }

    public TableView<DRBTable> getTable() {
        return table;
    }
   
    public DisplayDRBTable(ArrayList<String> name, ArrayList<String> registration, ArrayList<String> phoneNumber, String bookingType, ArrayList<String> timeStart) {
        drbName = name;
        drbRegistration = registration;
        drbPhoneNumber = phoneNumber;
        timeStartDRBPhoneCall = timeStart;
    }     
}