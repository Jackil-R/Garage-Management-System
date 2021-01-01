package Controllers.Customer;

import java.util.ArrayList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class PostcodeTable {
    
    private int ID;
    private String firstname;
    private String lastname;
    private String business;
    private String addressline1;
    private String postcode;
        
    public PostcodeTable(int ID, String firstname, String lastname, String business, String addressline1, String postcode) {
        super();
        this.ID = ID;
        this.firstname = firstname;
        this.lastname = lastname;
        this.business = business;
        this.addressline1 = addressline1;
        this.postcode = postcode;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public int getID() {
        return ID;
    }

    public String getAddressline1() {
        return addressline1;
    }

    public void setAddressline1(String addressline1) {
        this.addressline1 = addressline1;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public static ArrayList<TableColumn<PostcodeTable, ?>> getColumn(TableView table) {
        String[] columnNames = { "First Name", "Last Name","Company Name", "Address Line 1", "Postcode" };
        String[] variableNames = { "firstname", "lastname", "business", "addressline1", "postcode" };
        Integer[] column_width = { 20, 20, 20, 20, 20 };

        int i = 0;
        TableColumn<PostcodeTable, String> firstnameCol = new TableColumn<>(columnNames[i++]);
        TableColumn<PostcodeTable, String> lastnameCol = new TableColumn<>(columnNames[i++]);
        TableColumn<PostcodeTable, String> businessCol = new TableColumn<>(columnNames[i++]);
        TableColumn<PostcodeTable, String> addressline1Col = new TableColumn<>(columnNames[i++]);
        TableColumn<PostcodeTable, String> postcodeCol = new TableColumn<>(columnNames[i++]);

        i = 0;
        firstnameCol.prefWidthProperty().bind(table.widthProperty().divide(100 / column_width[i++]));
        lastnameCol.prefWidthProperty().bind(table.widthProperty().divide(100 / column_width[i++]));
        businessCol.prefWidthProperty().bind(table.widthProperty().divide(100 / column_width[i++]));
        addressline1Col.prefWidthProperty().bind(table.widthProperty().divide(100 / column_width[i++]));
        postcodeCol.prefWidthProperty().bind(table.widthProperty().divide(100 / column_width[i++]));

        i = 0;
        firstnameCol.setCellValueFactory(new PropertyValueFactory<PostcodeTable, String>(variableNames[i++]));
        lastnameCol.setCellValueFactory(new PropertyValueFactory<PostcodeTable, String>(variableNames[i++]));
        businessCol.setCellValueFactory(new PropertyValueFactory<PostcodeTable, String>(variableNames[i++]));
        addressline1Col.setCellValueFactory(new PropertyValueFactory<PostcodeTable, String>(variableNames[i++]));
        postcodeCol.setCellValueFactory(new PropertyValueFactory<PostcodeTable, String>(variableNames[i++]));
        
        ArrayList<TableColumn<PostcodeTable, ?>> columns = new ArrayList<TableColumn<PostcodeTable, ?>>();

        columns.add(firstnameCol);
        columns.add(lastnameCol);
        columns.add(businessCol);
        columns.add(addressline1Col);
        columns.add(postcodeCol);

        return columns;
    }
}