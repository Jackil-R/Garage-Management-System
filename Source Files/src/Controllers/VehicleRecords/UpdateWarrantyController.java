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
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author ibraaheem
 */
public class UpdateWarrantyController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private TextField name;
    @FXML
    private TextField address;
    @FXML
    private DatePicker expire;
    private String reg;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void update (ActionEvent event) throws Exception {
        
        LocalDate hello = LocalDate.now();
        
         if(expire.getValue() != null) {
         if(expire.getValue().isBefore(hello)) {
             
             JOptionPane.showMessageDialog(null, "Warranty can not expire in the past");
         }
         }
        
        if(!(name.getText().equals(" "))&&!(address.getText().equals(" ")) && expire.getValue() != null) {
        
         Connection connection = DBConnection.dbConnect();    
     
        String sql = "UPDATE Vehicles SET WarrantyID = ? WHERE Registration = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        
        String sqlb = "INSERT INTO Warranty " +
                   "VALUES(?,?,?,?)";
         PreparedStatement psg = connection.prepareStatement(sqlb);
         
         Statement stmt = connection.createStatement();
             String max = "SELECT * FROM Vehicles"; 
             ResultSet row = stmt.executeQuery(max); 
             
             int maxnum =0;
             
             while(row.next()){
                 if(row.getInt("ID") > maxnum) {
                   maxnum = row.getInt("WarrantyID"); 
                 }           
             }
              maxnum = maxnum + 1;
             stmt.close();
         
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");    
         
         LocalDate dated = expire.getValue();
         String warexpire = dated.format(formatter);
             
         ps.setInt(1,maxnum);
         psg.setString(2,name.getText());
         psg.setString(4,address.getText());
         psg.setString(3,warexpire);
        
         ps.executeUpdate();
         psg.executeUpdate();
         ps.close();
         psg.close();
         connection.close();
             
        }
        
         JOptionPane.showMessageDialog(null, "Warranty update!");
        
    }
    
    
    public void setReg(String a) {
        
        reg = a;
        
    }
}
