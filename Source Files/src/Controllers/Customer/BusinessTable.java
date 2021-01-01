package Controllers.Customer;

import java.util.ArrayList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class BusinessTable {

    private int ID = -1;
    private String business;
    private String addressline1;
    private String postcode;
        
    public BusinessTable(int ID, String business, String addressline1, String postcode) {
        super();
        this.ID = ID;
        this.business = business;
        this.addressline1 = addressline1;
        this.postcode = postcode;
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

    public static ArrayList<TableColumn<BusinessTable, ?>> getColumn(TableView table) {
        String[] columnNames = { "Company Name", "Address Line 1", "Postcode" };
        String[] variableNames = { "business", "addressline1", "postcode" };
        Integer[] column_width = { 30, 30, 15 };

        int i = 0;
        TableColumn<BusinessTable, String> businessCol = new TableColumn<>(columnNames[i++]);
        TableColumn<BusinessTable, String> addressline1Col = new TableColumn<>(columnNames[i++]);
        TableColumn<BusinessTable, String> postcodeCol = new TableColumn<>(columnNames[i++]);

        i = 0;
        businessCol.prefWidthProperty().bind(table.widthProperty().divide(100 / column_width[i++]));
        addressline1Col.prefWidthProperty().bind(table.widthProperty().divide(100 / column_width[i++]));
        postcodeCol.prefWidthProperty().bind(table.widthProperty().divide(100 / column_width[i++]));

        i = 0;
        businessCol.setCellValueFactory(new PropertyValueFactory<BusinessTable, String>(variableNames[i++]));
        addressline1Col.setCellValueFactory(new PropertyValueFactory<BusinessTable, String>(variableNames[i++]));
        postcodeCol.setCellValueFactory(new PropertyValueFactory<BusinessTable, String>(variableNames[i++]));
        
        ArrayList<TableColumn<BusinessTable, ?>> columns = new ArrayList<TableColumn<BusinessTable, ?>>();

        columns.add(businessCol);
        columns.add(addressline1Col);
        columns.add(postcodeCol);

        return columns;
    }
}