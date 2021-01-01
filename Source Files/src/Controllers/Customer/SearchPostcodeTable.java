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

public class SearchPostcodeTable extends Application {

    int selectedID;
    String customerPostcode;
    boolean businessSelected;
    TableView<PostcodeTable> table;
    Connection connection = null;
    CustomerDatabase access;
 
    @Override
    public void start(Stage stage) throws Exception {              
    }  
   
    @Override
    public void init() {	
        table = new TableView<PostcodeTable>();
        table.getColumns().addAll(PostcodeTable.getColumn(table));

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
                            businessSelected = table.getSelectionModel().getSelectedItem().getBusiness().length()>1;
                           ((Node)(event.getSource())).getScene().getWindow().hide();
                    }
                }
            }
        });
    }
    
    public ObservableList<PostcodeTable> getPersonDummy() throws SQLException {
        ObservableList<PostcodeTable> data = FXCollections.observableArrayList();

        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        access = new CustomerDatabase(connection);
        /* *** * *** * *** * *** * *** * */

        ArrayList<Integer> allPostcodeIDs = access.getPostcodeIDs(customerPostcode);
        ArrayList<Integer> postcodeIDs = new ArrayList<Integer>();

        for (int i = 0; i < allPostcodeIDs.size(); i++) {
            if(access.getRegistrationStatus(allPostcodeIDs.get(i)) == 1)
                postcodeIDs.add(allPostcodeIDs.get(i));
        }

        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch(SQLException ex) {
            System.out.println("searchBusiness catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */

        ArrayList<String> allForenames = getNameList(postcodeIDs, false);
        ArrayList<String> allSurnames = getNameList(postcodeIDs, true);
        ArrayList<String> allBusiness = getBusinessList(postcodeIDs);
        ArrayList<String> allAddressLine1 = getAddressLine1List(postcodeIDs);
        ArrayList<String> allPostcode = getPostcodeList(postcodeIDs);

        for(int i=0; i<allBusiness.size(); i++)
            data.addAll(new PostcodeTable(postcodeIDs.get(i) ,allForenames.get(i), allSurnames.get(i), allBusiness.get(i), allAddressLine1.get(i), allPostcode.get(i)));
        
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

    private ArrayList<String> getNameList(ArrayList<Integer> IDs, boolean lastName)
    {
        ArrayList<String> nameList = new ArrayList<String>();
        
        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        access = new CustomerDatabase(connection);
        /* *** * *** * *** * *** * *** * */

        for (Integer ID : IDs) {
            String name = access.getFirstOrLastName(ID, lastName);
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
    
    public boolean getBusinessSelected() {
        return businessSelected;
    }
    
    public TableView<PostcodeTable> getTable() {
        return table;
    }
    
    public SearchPostcodeTable(String postcode) {
        customerPostcode = postcode;
    }
}