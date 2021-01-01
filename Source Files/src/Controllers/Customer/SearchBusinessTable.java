package Controllers.Customer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SearchBusinessTable extends Application {

    int selectedID;
    String customerBusiness;
    TableView<BusinessTable> table;
    Connection connection = null;
    CustomerDatabase access;
    
    @Override
    public void start(Stage stage) throws Exception {
    }

    @Override
    public void init() {
        table = new TableView<BusinessTable>();
        table.getColumns().addAll(BusinessTable.getColumn(table));

        try {
            table.setItems(getPersonDummy());
        } 
        catch (SQLException ex) {
            System.out.println(ex);
        }

        table.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 1) {
                        if (table.getSelectionModel().getSelectedIndex() >= 0) {
                                selectedID = table.getSelectionModel().getSelectedItem().getID();
                               ((Node)(event.getSource())).getScene().getWindow().hide();
                        }
                }
            }
        });
    }
        
    public ObservableList<BusinessTable> getPersonDummy() throws SQLException {
        ObservableList<BusinessTable> data = FXCollections.observableArrayList();

        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        access = new CustomerDatabase(connection);
        /* *** * *** * *** * *** * *** * */

        
        ArrayList<Integer> allbusinessIDs = access.getIDs(customerBusiness, true);
        ArrayList<Integer> businessIDs = new ArrayList<Integer>();

        for (int i = 0; i < allbusinessIDs.size(); i++) {
            if(access.getRegistrationStatus(allbusinessIDs.get(i)) == 1)
                businessIDs.add(allbusinessIDs.get(i));
        }

        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch(SQLException ex) {
            System.out.println("searchBusiness catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */

        ArrayList<String> allBusiness = getBusinessList(businessIDs);
        ArrayList<String> allAddressLine1 = getAddressLine1List(businessIDs);
        ArrayList<String> allPostcode = getPostcodeList(businessIDs);

        for(int i=0; i<allBusiness.size(); i++)
            data.addAll(new BusinessTable(businessIDs.get(i) ,allBusiness.get(i), allAddressLine1.get(i), allPostcode.get(i)));
        
        return data;
    }
        
    private ArrayList<String> getBusinessList(ArrayList<Integer> IDs)
    {
        ArrayList<String> nameList = new ArrayList<String>();
        
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        access = new CustomerDatabase(connection);
        /* *** * *** * *** * *** * *** * */

        for (Integer ID : IDs) {
            String name = access.getBusinessName(ID);
            nameList.add(name);
        }

        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch(SQLException ex) {
            System.out.println("getSurnameList catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */

        return nameList;   
    }

    private ArrayList<String> getAddressLine1List(ArrayList<Integer> IDs)
    {
        ArrayList<String> ad1List = new ArrayList<String>();
        
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        access = new CustomerDatabase(connection);
        /* *** * *** * *** * *** * *** * */

        for (Integer ID : IDs) {
            String addressLine = access.getAddressLine1(ID);
            ad1List.add(addressLine);
        }

        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch(SQLException ex) {
            System.out.println("getSurnameList catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */

        return ad1List;   
    }

    private ArrayList<String> getPostcodeList(ArrayList<Integer> IDs)
    {
        ArrayList<String> postcodeList = new ArrayList<String>();
        
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        access = new CustomerDatabase(connection);
        /* *** * *** * *** * *** * *** * */

        for (Integer ID : IDs) {
            String postcode = access.getPostcode(ID);
            postcodeList.add(postcode);
        }

        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch(SQLException ex) {
            System.out.println("getPostcodeList catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */

        return postcodeList;   
    }
    
    public int getSelectedID() {
        return selectedID;
    }
    
    public TableView<BusinessTable> getTable() {
        return table;
    }
   
    public SearchBusinessTable(String business) {
        customerBusiness = business;
    }     
}