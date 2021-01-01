package Controllers.Customer;

import java.util.ArrayList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SMBTable {

    private String nameOrCompany;
    private String vehicleRegistration;
    private String phoneNumber;
    private String bookingType;
    private String date;

    public SMBTable(String nameOrCompany, String vehicleRegistration, String bookingType, String phoneNumber, String date) {
        this.nameOrCompany = nameOrCompany;
        this.vehicleRegistration = vehicleRegistration;
        this.bookingType = bookingType;
        this.phoneNumber = phoneNumber;
        this.date = date;
    }

    public String getNameOrCompany() {
        return nameOrCompany;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getBookingType() {
        return bookingType;
    }

    public String getDate() {
        return date;
    }
    
    public String getVehicleRegistration() {
        return vehicleRegistration;
    }

    public static ArrayList<TableColumn<SMBTable, ?>> getColumn(TableView table) {
        String[] columnNames = { "Full Name or Company", "Registration Plate", "Booking Type", "Phone Number", "Last Service/MOT Expiry Date" };
        String[] variableNames = { "nameOrCompany", "vehicleRegistration", "bookingType", "phoneNumber", "date" };
        Integer[] column_width = { 20, 18, 15, 15, 25 };

        int i = 0;
        TableColumn<SMBTable, String> nameCol = new TableColumn<>(columnNames[i++]);
        TableColumn<SMBTable, String> registrationCol = new TableColumn<>(columnNames[i++]);
        TableColumn<SMBTable, String> phoneNumberCol = new TableColumn<>(columnNames[i++]);
        TableColumn<SMBTable, String> bookingTypeCol = new TableColumn<>(columnNames[i++]);
        TableColumn<SMBTable, String> dateCol = new TableColumn<>(columnNames[i++]);

        i = 0;
        nameCol.prefWidthProperty().bind(table.widthProperty().divide(100 / column_width[i++]));
        registrationCol.prefWidthProperty().bind(table.widthProperty().divide(100 / column_width[i++]));
        phoneNumberCol.prefWidthProperty().bind(table.widthProperty().divide(100 / column_width[i++]));
        bookingTypeCol.prefWidthProperty().bind(table.widthProperty().divide(100 / column_width[i++]));
        dateCol.prefWidthProperty().bind(table.widthProperty().divide(100 / column_width[i++]));

        i = 0;
        nameCol.setCellValueFactory(new PropertyValueFactory<SMBTable, String>(variableNames[i++]));
        registrationCol.setCellValueFactory(new PropertyValueFactory<SMBTable, String>(variableNames[i++]));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<SMBTable, String>(variableNames[i++]));
        bookingTypeCol.setCellValueFactory(new PropertyValueFactory<SMBTable, String>(variableNames[i++]));
        dateCol.setCellValueFactory(new PropertyValueFactory<SMBTable, String>(variableNames[i++]));
        
        ArrayList<TableColumn<SMBTable, ?>> columns = new ArrayList<TableColumn<SMBTable, ?>>();

        columns.add(nameCol);
        columns.add(registrationCol);
        columns.add(bookingTypeCol);
        columns.add(phoneNumberCol);
        columns.add(dateCol);
        
        return columns;
    }
}
