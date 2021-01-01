/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.VehicleRecords;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author ibraaheem
 */
public class WarrantyController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    Connection connection = null;
    Statement stmt = null;
   
    @FXML
    private TableView mytable;
    @FXML
    private TableColumn wid;
    @FXML
    private TableColumn name;
    @FXML
    private TableColumn address;
    @FXML
    private TableColumn expire;
    private int reg;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        wid.setCellValueFactory(new PropertyValueFactory<parts, String>("wid"));
        name.setCellValueFactory(new PropertyValueFactory<parts, Integer>("name"));
        address.setCellValueFactory(new PropertyValueFactory<parts, String>("address"));
        expire.setCellValueFactory(new PropertyValueFactory<parts, String>("expire"));
  
    }    
   
    public void setTable() throws SQLException {
        
        mytable.getItems().clear();
        ObservableList<warranty> data =
       FXCollections.observableArrayList();
        
        connection = DBConnection.dbConnect();       
      
        String sql = "SELECT * FROM Warranty WHERE ID = ?";      
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, Integer.toString(reg));
        ResultSet set = ps.executeQuery();
        mytable.getItems().clear();
        
        while(set.next()) {  
        
            data.add(new warranty(set.getInt("ID"),set.getString("Company"),set.getString("Address"),set.getString("Expire_Date")));
            set.next();
        }
        
            mytable.setItems(data);
        
        
    }
    
    public void setReg(int ID) throws SQLException {
        
        reg = ID;
         this.setTable();
    }
    
}
