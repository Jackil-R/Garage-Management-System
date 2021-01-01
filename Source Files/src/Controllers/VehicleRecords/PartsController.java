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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author ibraaheem
 */
public class PartsController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    Connection connection = null;
    Statement stmt = null;
   
    @FXML
    private TableView mytable;
    @FXML
    private TableColumn vid;
    @FXML
    private TableColumn bid;
    @FXML
    private TableColumn pid;
    @FXML
    private TableColumn date;
    private String reg;
    private int id;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        vid.setCellValueFactory(new PropertyValueFactory<parts, String>("vid"));
        bid.setCellValueFactory(new PropertyValueFactory<parts, Integer>("bid"));
        pid.setCellValueFactory(new PropertyValueFactory<parts, String>("pid"));
        date.setCellValueFactory(new PropertyValueFactory<parts, String>("date"));
       
    }    
      
     public void handleaction(String r, int i) throws SQLException {
      
        mytable.getItems().clear();
        ObservableList<parts> data =
       FXCollections.observableArrayList();
        
        connection = DBConnection.dbConnect();       
      
        String sql = "SELECT * FROM Bookings WHERE VehiclesID = ?";      
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, Integer.toString(i));
        ResultSet set = ps.executeQuery();
        
        while(set.next()) {  
        
        String sql2 = "SELECT * FROM Service_Reports WHERE BookingsID = ?";      
        PreparedStatement ps2 = connection.prepareStatement(sql2);
        ps2.setString(1, Integer.toString(set.getInt("ID")));
        ResultSet set2 = ps2.executeQuery();
     
        String sql3 = "SELECT * FROM Service_Parts"; 
        Statement stmt = connection.createStatement(); 
        ResultSet row = stmt.executeQuery(sql3); 
        
        String sql4 = "SELECT Name FROM Parts WHERE ID = ?";      
        PreparedStatement ps4 = connection.prepareStatement(sql4);
        ps4.setString(1, Integer.toString(row.getInt("PartsID")));
        ResultSet set4 = ps4.executeQuery();
             
            mytable.getItems().clear();
        
            data.add(new parts(r,set.getInt(1),set4.getString(1),set.getString("Time_Start")));
            set.next();
        }
        
            mytable.setItems(data);
     } 
     
     @FXML
    public void setReg(String senta, int sentb) throws SQLException{
        reg = senta;
        id = sentb;
        this.handleaction(senta, sentb);
    } 
    
}