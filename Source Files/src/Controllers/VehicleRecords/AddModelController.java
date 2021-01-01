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
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author ibraaheem
 */
public class AddModelController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private Button add;
    @FXML
    private TextField model;
    @FXML
    private ComboBox make;
    @FXML
    private ComboBox type;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.getType();
             this.getMake();
        } catch (SQLException ex) {
            Logger.getLogger(AddModelController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    @FXML
    private void addmodel (ActionEvent event) throws Exception {
    
          if((!model.getText().equals("")) && (type.getValue() != null) && (make.getValue() != null)) {
          Connection connection = DBConnection.dbConnect();
          
                String geta = "SELECT Count(*) FROM Model WHERE Name = ?";    
              PreparedStatement preparedStatementa = connection.prepareStatement(geta);
              preparedStatementa.setString(1, model.getText());
              ResultSet getar = preparedStatementa.executeQuery();
             
             if(getar.getInt(1) == 1) {
                 JOptionPane.showMessageDialog(null, "Model already in entred in system");  
                 return;
             }
 
          String sql = "INSERT INTO Model " +
                   "VALUES(?,?,?,?)";
          PreparedStatement ps = connection.prepareStatement(sql);
          
          Statement stmt = connection.createStatement();
             String max = "SELECT * FROM Model"; 
             ResultSet row = stmt.executeQuery(max); 
             
             int maxnum =0;
             
             while(row.next()){
                 if(row.getInt("ID") > maxnum) {
                   maxnum = row.getInt("ID"); 
                 }           
             }
              maxnum = maxnum + 1;
             stmt.close();
   
          ps.setString(1, Integer.toString(maxnum));
          ps.setString(2, model.getText());
          ps.setString(3, (String) make.getValue());
          ps.setString(4, (String) type.getValue());
          
          
          ps.close();
          connection.close();
          } else {
             
              JOptionPane.showMessageDialog(null, "Plese enter all fields"); 
              
          }
          JOptionPane.showMessageDialog(null, "New Model Inserted"); 
          ((Node)(event.getSource())).getScene().getWindow().hide();
        
    }
    
     public void getMake() throws SQLException {
        
        Connection connection = DBConnection.dbConnect();       
        Statement stmt = connection.createStatement();     
         
         try {
        connection = DBConnection.dbConnect();       
        stmt = connection.createStatement();     
        String sql = "SELECT COUNT(*) FROM Make";      
        ResultSet row = stmt.executeQuery(sql); 
        String[] rows = new String[row.getInt(1)];
        
        String sql1 = "SELECT * FROM Make";      
        ResultSet row1 = stmt.executeQuery(sql1); 
        
        int i =0;
        make.getItems().clear();
        while(row1.next()) {
             rows[i] = row1.getString("Name");
            i++;
        }
        
        Arrays.sort(rows);
        make.getItems().addAll(rows);
        stmt.close();
        connection.close();
        
       } catch (Exception e) {
           JOptionPane.showMessageDialog(null, "makes could not be found");   
           //JOptionPane.showMessageDialog(null, e);   
       } 
         
         
     }
     
      public void getType() throws SQLException {
        
        Connection connection = DBConnection.dbConnect();       
        Statement stmt = connection.createStatement();     
         
         try {
        connection = DBConnection.dbConnect();       
        stmt = connection.createStatement();     
        String sql = "SELECT COUNT(*) FROM Car_Type";      
        ResultSet row = stmt.executeQuery(sql); 
        String[] rows = new String[row.getInt(1)];
        
        String sql1 = "SELECT * FROM Car_Type";      
        ResultSet row1 = stmt.executeQuery(sql1); 
        
        int i =0;
        type.getItems().clear();
        while(row1.next()) {
             rows[i] = row1.getString("Name");
            i++;
        }
        
        Arrays.sort(rows);
        type.getItems().addAll(rows);
        stmt.close();
        connection.close();
        
       } catch (Exception e) {
           JOptionPane.showMessageDialog(null, "Car Types could not be found");   
           //JOptionPane.showMessageDialog(null, e);   
       } 
         
         
     }
    
}
