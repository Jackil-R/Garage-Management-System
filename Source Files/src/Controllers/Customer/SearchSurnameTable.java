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

public class SearchSurnameTable extends Application {

    int selectedID;
    String customerSurname;
    TableView<SurnameTable> table;
    Connection connection = null;
    CustomerDatabase access; 
        
    @Override
    public void start(Stage stage) throws Exception {              
    }
            
    public SearchSurnameTable(String surname) {
        customerSurname = surname;
    }

    @Override
    public void init() {
        table = new TableView<SurnameTable>();
        table.getColumns().addAll(SurnameTable.getColumn(table));

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
        
    public int getSelectedID() {
        return selectedID;
    }
    
    public TableView<SurnameTable> getTable() {
        return table;
    }

    public ObservableList<SurnameTable> getPersonDummy() throws SQLException {
        ObservableList<SurnameTable> data = FXCollections.observableArrayList();

        /*Now connecting to the database */
        connection = CustomerDatabaseConnection.dbConnect();
        access = new CustomerDatabase(connection);
        /* *** * *** * *** * *** * *** * */

        ArrayList<Integer> allSurnameIDs = access.getIDs(customerSurname, false);
        ArrayList<Integer> surnameIDs = new ArrayList<Integer>();

        for (int i = 0; i < allSurnameIDs.size(); i++) {
            if(access.getRegistrationStatus(allSurnameIDs.get(i)) == 1)
                surnameIDs.add(allSurnameIDs.get(i));
        }
        
        /*Now closing the connection to the database */
        try {
            connection.close();
        } 
        catch(SQLException ex) {
            System.out.println("searchBusiness catch: "+ex);
        }
        /* *** * *** * *** * *** * *** * *** * *** * */

        ArrayList<String> allForenames = getNameList(surnameIDs, false);
        ArrayList<String> allSurnames = getNameList(surnameIDs, true);
        ArrayList<String> allAddressLine1 = getAddressLine1List(surnameIDs);
        ArrayList<String> allPostcode = getPostcodeList(surnameIDs);

        for(int i=0; i<allForenames.size(); i++) 
            data.addAll(new SurnameTable(surnameIDs.get(i) ,allForenames.get(i), allSurnames.get(i), allAddressLine1.get(i), allPostcode.get(i)));
        
        return data;
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
}