package Controllers.Customer;

import java.util.ArrayList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class DRBTable {

    private String nameOrCompany;
    private String phoneNumber;
    private String bookingType;
    private String timeStart;
    private String registration;

    public DRBTable(String nameOrCompany, String registration, String phoneNumber, String bookingType, String timeStart) {
        this.nameOrCompany = nameOrCompany;
        this.phoneNumber = phoneNumber;
        this.bookingType = bookingType;
        this.timeStart = timeStart;
        this.registration = registration;
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

    public String getTimeStart() {
        return timeStart;
    }
    
    public String getRegistration() {
        return registration;
    }

    public static ArrayList<TableColumn<DRBTable, ?>> getColumn(TableView table) {
        String[] columnNames = { "Full Name or Company", "Registration Plate", "Phone number", "Booking Type", "Time & Date" };
        String[] variableNames = { "nameOrCompany", "registration", "phoneNumber", "bookingType", "timeStart" };
        Integer[] column_width = { 25, 15, 15, 20, 20 };

        int i = 0;
        TableColumn<DRBTable, String> nameCol = new TableColumn<>(columnNames[i++]);
        TableColumn<DRBTable, String> registrationCol = new TableColumn<>(columnNames[i++]);
        TableColumn<DRBTable, String> phoneNumberCol = new TableColumn<>(columnNames[i++]);
        TableColumn<DRBTable, String> bookingTypeCol = new TableColumn<>(columnNames[i++]);
        TableColumn<DRBTable, String> timeStartCol = new TableColumn<>(columnNames[i++]);

        i = 0;
        nameCol.prefWidthProperty().bind(table.widthProperty().divide(100 / column_width[i++]));
        registrationCol.prefWidthProperty().bind(table.widthProperty().divide(100 / column_width[i++]));
        phoneNumberCol.prefWidthProperty().bind(table.widthProperty().divide(100 / column_width[i++]));
        bookingTypeCol.prefWidthProperty().bind(table.widthProperty().divide(100 / column_width[i++]));
        timeStartCol.prefWidthProperty().bind(table.widthProperty().divide(100 / column_width[i++]));

        i = 0;
        nameCol.setCellValueFactory(new PropertyValueFactory<DRBTable, String>(variableNames[i++]));
        registrationCol.setCellValueFactory(new PropertyValueFactory<DRBTable, String>(variableNames[i++]));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<DRBTable, String>(variableNames[i++]));
        bookingTypeCol.setCellValueFactory(new PropertyValueFactory<DRBTable, String>(variableNames[i++]));
        timeStartCol.setCellValueFactory(new PropertyValueFactory<DRBTable, String>(variableNames[i++]));
        
        ArrayList<TableColumn<DRBTable, ?>> columns = new ArrayList<TableColumn<DRBTable, ?>>();

        columns.add(nameCol);
        columns.add(registrationCol);
        columns.add(bookingTypeCol);
        columns.add(timeStartCol);
        columns.add(phoneNumberCol);

        return columns;
    }
}
